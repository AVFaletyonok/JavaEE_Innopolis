FROM openjdk:21
ARG JAR_FILE=db-api.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
COPY application.properties application.properties
EXPOSE 9999
ENTRYPOINT ["java", "-jar", "app.jar"]