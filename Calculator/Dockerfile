FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app
COPY ./pom.xml ./pom.xml

WORKDIR /app/Calculator

COPY ./Calculator/pom.xml ./pom.xml
COPY ./Calculator/src ./src

RUN mvn clean install

FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app/Calculator

COPY --from=build /app/Calculator/target/Calculator-0.0.1-SNAPSHOT.jar Calculator.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "Calculator.jar"]