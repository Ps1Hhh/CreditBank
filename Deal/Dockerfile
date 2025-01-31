FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app
COPY ./pom.xml ./pom.xml

WORKDIR /app/Deal

COPY ./Deal/pom.xml ./pom.xml
COPY ./Deal/src ./src

RUN mvn clean install

FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app/Deal

COPY --from=build /app/Deal/target/Deal-0.0.1-SNAPSHOT.jar Deal.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "Deal.jar"]