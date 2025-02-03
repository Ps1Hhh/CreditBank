FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /CreditBank
COPY ./pom.xml ./pom.xml

WORKDIR /CreditBank/Statement

COPY ./Statement/pom.xml ./pom.xml
COPY ./Statement/src ./src

RUN mvn clean install

FROM eclipse-temurin:21-jre AS runtime

WORKDIR /CreditBank/Statement

COPY --from=build /CreditBank/Statement/target/Statement-0.0.1-SNAPSHOT.jar Statement.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "Statement.jar"]