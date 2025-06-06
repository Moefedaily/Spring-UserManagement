FROM openjdk:21-jdk-slim

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

RUN cp target/management-*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]