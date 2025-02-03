FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /CreditBank
COPY ./pom.xml ./pom.xml

WORKDIR /CreditBank/Deal

COPY ./Deal/pom.xml ./pom.xml
COPY ./Deal/src ./src

RUN mvn clean install

FROM eclipse-temurin:21-jre AS runtime

WORKDIR /CreditBank/Deal

COPY --from=build /CreditBank/Deal/target/Deal-0.0.1-SNAPSHOT.jar Deal.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "Deal.jar"]