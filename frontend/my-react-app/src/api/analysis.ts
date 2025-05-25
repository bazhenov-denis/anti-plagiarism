import base from './files';                    // тот же axios instance

export interface Similarity {
    fileId: string;
    score: number;
}

export interface FileStats {
    words: number;      // ← примеры, поправьте под DTO
    paragraphs: number;
    chars: number;
}

export interface AnalysisResult {
    stats: FileStats;
    similarity: Similarity[];
}

/** GET /analysis/{id} — backend сразу отдаёт результат (или 202/404) */
export async function getAnalysis(id: string): Promise<AnalysisResult> {
    const r = await base.get<AnalysisResult>(`/analysis/${id}`);
    return r.data;
}
