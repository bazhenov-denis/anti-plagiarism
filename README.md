# Anti‑Plagiarism

> **Проект для автоматического поиска текстовых совпадений (плагиата) в загруженных документах.**  
> Стек: _Spring Boot 3 + PostgreSQL + React / Vite + Docker .

---

## 1. Что делает система

1. Пользователь загружает файл через web‑интерфейс или REST‑запрос `POST /files`. если вдруг front что то не так делает попробуй пару раз перезагрузить если всё ещё нет то уже тестируй через postman всё таки в требованиях его не было и он не очень стабильный
    
2. Сервис «File‑Storing» сохраняет файл на диск, метаданные – в PostgreSQL.
    
3. Далее он вызывает внутренний REST‐метод «File‑Analysis», который
    
    - считает статистику (символы, слова, параграфы),
        
    - вычисляет коэффициент совпадения с уже сохранёнными файлами (хотелось сделать реальный антиплагиат, но в итоге не хватило времени и там немного случайные совпадения)
        
        
4. UI отображает статистику и список «похожих» документов в модальном окне.
    
5. Файлы можно **скачать** (`GET /files/{id}`) или удалить (`DELETE /files/{id}`).
    

---

## 2. Архитектура


Backend часть состоит из 3 микросервисов

api-gateway - для единой входной точки и маршрутизации запросов

file-storing-service - для загрузки и работы с файлами

file-analysis-service - для аналитики и генерации облака слов
    
- **Контейнеризация**: всё разворачивается одной командой `docker compose up --build`.
    

---

## 3. Быстрый старт

```
# клонируем репозиторий
$ git clone https://github.com/bazhenov-denis/anti-plagiarism.git
$ cd anti-plagiarism

$ docker compose up --build

```


---

## 4. REST‑API (основные маршруты)

все запросы отправляются просто на localhost:8080 далее api-gateway сам распределяет

| Метод      | URL                        | Описание                                                   |
| ---------- | -------------------------- | ---------------------------------------------------------- |
| **POST**   | `/files`                   | Загрузить файл _(multipart/form‑data, поле_ `_file_`_)_    |
| **GET**    | `/files`                   | Список всех файлов                                         |
| **GET**    | `/files/{id}`              | Скачать файл (binary)                                      |
| **DELETE** | `/files/{id}`              | Удалить файл и результаты анализа                          |
| **POST**   | `/analysis/{id}`           | Запустить анализ выбранного файла и вернуть JSON‑результат |
| **GET**    | `/analysis/{id}/wordcloud` | Получение облака слов                                      |

### Пример ответа `/analysis/3`

```
{
  "stats": {
    "fileId": "3",
    "chars": 267,
    "words": 37,
    "paragraphs": 2
  },
  "similarity": [
    { "fileId": "1", "score": 3.7394 }
  ]
}
```

Полная спецификация OpenAPI экспортируется в `docs/openapi.yaml` (или прямо из Postman‑коллекции `docs/postman/anti-plagiarism.postman_collection.json`). Импортируйте файл в Postman или Swagger UI, чтобы попробовать запросы.

---

## 5. Структура репозитория

```
anti-plagiarism/
├─ file-storing-service/   # Spring‑Boot приложение (REST + JPA)
│  ├─ src/main/java/
│  └─ Dockerfile
├─ file-analysis-service/  # Spring‑Boot приложение (анализ текста)
│  └─ Dockerfile
├─ frontend/               # Vite + React (TypeScript)
│  └─ src/
├─ docker-compose.yml
└─ docs/
    ├─ openapi.yaml
    └─ postman/
```

---
