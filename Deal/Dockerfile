FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

COPY ./pom.xml ./pom.xml
COPY ./src ./src

RUN mvn clean install -DskipTests

FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app

COPY --from=build /app/target/dataBaseService-0.0.1-SNAPSHOT.jar Deal.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "Deal.jar"]