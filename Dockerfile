FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY build/libs/kiron-0.0.1-SNAPSHOT.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]

EXPOSE 8080

