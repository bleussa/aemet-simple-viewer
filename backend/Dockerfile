FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN ./mvnw -q -B dependency:go-offline
COPY src src
RUN ./mvnw -q -B package -DskipTests

FROM eclipse-temurin:25-jre
RUN useradd --system --no-create-home --uid 1001 aemet
WORKDIR /app
COPY --from=build /app/target/aemet-*.jar app.jar
USER aemet
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
