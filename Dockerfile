FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/management-*.jar app.jar

EXPOSE 8080

RUN echo "Spring Boot JAR copied successfully"
RUN ls -la /app/

CMD ["java", "-jar", "app.jar"]