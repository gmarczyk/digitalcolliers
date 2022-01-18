FROM openjdk:17.0.1
ARG JAR_PATH=build/libs/*.jar
COPY ${JAR_PATH} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]