FROM eclipse-temurin:17-jre-jammy
ARG APP_VERSION=local
ENV APP_VERSION=${APP_VERSION}
WORKDIR /app
COPY target/*.jar app.jar
USER 10001
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
