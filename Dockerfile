FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

COPY target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","target/job-tracker-api-0.0.1-SNAPSHOT.jar"]