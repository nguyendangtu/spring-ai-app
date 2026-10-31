FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw package -DskipTests

EXPOSE 8081

ENTRYPOINT ["java","-jar","/app/target/spring-boot-app-0.0.1-SNAPSHOT.jar"]

