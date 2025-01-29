FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app
COPY ./pom.xml ./pom.xml

WORKDIR /app/Gateway

COPY ./Gateway/pom.xml ./pom.xml
COPY ./Gateway/src ./src

RUN mvn clean install

FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app/Gateway

COPY --from=build /app/Gateway/target/Gateway-0.0.1-SNAPSHOT.jar Gateway.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "Gateway.jar"]