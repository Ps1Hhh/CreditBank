FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app
COPY ./pom.xml ./pom.xml

WORKDIR /app/Dossier

COPY ./Dossier/pom.xml ./pom.xml
COPY ./Dossier/src ./src

RUN mvn clean install

FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app/Dossier

COPY --from=build /app/Dossier/target/Dossier-0.0.1-SNAPSHOT.jar Dossier.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "Dossier.jar"]