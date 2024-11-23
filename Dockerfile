FROM maven:3.9.8-eclipse-temurin-21 AS build
COPY pom.xml /app/
COPY src /app/src/
COPY /src/main/resources/config/firebase_key.json /app/src/main/resources/config/firebase_key.json
WORKDIR /app
ENV MAVEN_CONFIG=/root/.m2
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/src/main/resources/config/firebase_key.json /app/config/firebase_key.json
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=classpath:/application-docker.properties"]