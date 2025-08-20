FROM gradle:8.5 AS build
WORKDIR /app
COPY . .
RUN gradle build -x test

FROM openjdk:21-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]