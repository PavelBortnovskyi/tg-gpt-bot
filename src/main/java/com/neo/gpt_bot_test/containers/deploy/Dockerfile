FROM maven:3.8.4-openjdk-17-slim AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/gpt_bot_test-1.0-SNAPSHOT.jar app.jar
ENV SPRING_PROFILES_ACTIVE=deploy
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
