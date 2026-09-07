FROM eclipse-temurin:21-jre-jammy
LABEL org.opencontainers.image.authors="dk"
COPY app/build/libs/app-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]