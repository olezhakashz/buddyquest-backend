FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /app

# копируем проект внутрь контейнера
COPY . .

# собираем jar через gradle (для Windows будет gradlew.bat, но внутри linux-образа используем ./gradlew)
RUN ./gradlew bootJar

# второй, "лёгкий" образ только для запуска
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# копируем собранный jar из первого этапа
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
