FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app
COPY ./pom.xml ./pom.xml

WORKDIR /app/Statement

COPY ./Statement/pom.xml ./pom.xml
COPY ./Statement/src ./src

RUN mvn clean install

FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app/Statement

COPY --from=build /app/Statement/target/Statement-0.0.1-SNAPSHOT.jar Statement.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "Statement.jar"]