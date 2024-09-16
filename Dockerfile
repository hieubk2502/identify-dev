FROM maven:3.9.9-amazoncorretto-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# => build jar
RUN mvn package -DskipTest

FROM amazoncorretto:17.0.12

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT["java","-jar", "app.jar"]

