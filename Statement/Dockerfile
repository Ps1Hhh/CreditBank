FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

COPY ../pom.xml ../pom.xml
COPY ./pom.xml ./pom.xml
COPY ./src ./src

#RUN mvn clean install

FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app

#COPY --from=build /app/target/Statement-0.0.1-SNAPSHOT.jar Statement.jar

EXPOSE 8084

#ENTRYPOINT ["java", "-jar", "Statement.jar"]