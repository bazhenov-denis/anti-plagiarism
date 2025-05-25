package com.example.file_analysis_service.lucene;

import com.example.file_analysis_service.DTO.Similarity;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class LuceneIndexer {

  private Directory index;
  private StandardAnalyzer analyzer;

  @PostConstruct
  public void init() {
    // Современная in-memory реализация (замена старому RAMDirectory)
    this.index    = new ByteBuffersDirectory();
    this.analyzer = new StandardAnalyzer();
  }

  /** Индексация документа с данным id и содержимым */
  public void indexDocument(String id, String content) {
    IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
    try (IndexWriter writer = new IndexWriter(index, cfg)) {
      Document doc = new Document();
      doc.add(new StringField("id",      id,      Field.Store.YES));
      doc.add(new TextField  ("content", content, Field.Store.YES));
      writer.updateDocument(new Term("id", id), doc);   // upsert
      writer.commit();                                   // явный commit полезен
    } catch (IOException e) {
      log.error("Ошибка индексации файла {}", id, e);
    }
  }

  /** Поиск topN похожих документов на основе содержимого файла fileId */
  public List<Similarity> findSimilar(String fileId, int topN) {
    List<Similarity> result = new ArrayList<>();

    try (DirectoryReader reader = DirectoryReader.open(index)) {
      IndexSearcher searcher    = new IndexSearcher(reader);
      StoredFields  stored      = searcher.storedFields();     // <-- новый способ

      /* 1. Находим нужный документ по id */
      TermQuery idQuery = new TermQuery(new Term("id", fileId));
      TopDocs idHits = searcher.search(idQuery, 1);

      if (idHits.totalHits.value() == 0L) {                      // TotalHits.value доступен
        log.warn("Документ не найден: {}", fileId);
        return result;
      }
      int docId = idHits.scoreDocs[0].doc;

      /* 2. Читаем его содержимое */
      Document sourceDoc = stored.document(docId);
      String content     = sourceDoc.get("content");

      /* 3. Строим полнотекстовый запрос по этому содержимому */
      QueryParser parser = new QueryParser("content", analyzer);
      Query       query  = parser.parse(QueryParser.escape(content));

      /* 4. Ищем похожие документы */
      TopDocs topDocs = searcher.search(query, topN + 1);      // +1 чтобы исключить сам себя
      for (ScoreDoc sd : topDocs.scoreDocs) {
        if (sd.doc == docId) continue;                      // пропускаем источник
        Document d = stored.document(sd.doc);
        result.add(new Similarity(d.get("id"), sd.score));
      }

    } catch (Exception e) {
      log.error("Ошибка поиска похожих документов для {}", fileId, e);
    }
    return result;
  }
}
