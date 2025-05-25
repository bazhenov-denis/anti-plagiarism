# Используем Temurin 21 вместо 17
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Берём Maven wrapper и описания зависимостей
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Предзагружаем зависимости, чтобы кэшировать слой
RUN ./mvnw -B dependency:go-offline

# Копируем исходники проекта
COPY src ./src

# Запускаем Spring Boot
CMD ["./mvnw", "-B", "spring-boot:run"]