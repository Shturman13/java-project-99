# Этап сборки
FROM gradle:8.14-jdk21 AS build
WORKDIR /home/gradle
COPY . .
RUN gradle bootJar --no-daemon

# Этап запуска
FROM openjdk:22-jdk-slim
WORKDIR /project
COPY --from=build /home/gradle/build/libs/app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]