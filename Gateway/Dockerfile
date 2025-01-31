FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /CreditBank
COPY ./pom.xml ./pom.xml

WORKDIR /CreditBank/Gateway

COPY ./Gateway/pom.xml ./pom.xml
COPY ./Gateway/src ./src

RUN mvn clean install

FROM eclipse-temurin:21-jre AS runtime

WORKDIR /CreditBank/Gateway

COPY --from=build /CreditBank/Gateway/target/Gateway-0.0.1-SNAPSHOT.jar Gateway.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "Gateway.jar"]