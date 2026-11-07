FROM eclipse-temurin:17-jre-jammy
USER root
RUN apt-get update \
 && apt-get install -y --only-upgrade apt libapt-pkg6.0 \
 && rm -rf /var/lib/apt/lists/*
ARG APP_VERSION=local
ENV APP_VERSION=${APP_VERSION}
WORKDIR /app
COPY target/*.jar app.jar
USER 10001
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
