FROM openjdk:21
ARG JAR_FILE=target/2.7_Attestation02-1.0-SNAPSHOT-jar-with-dependencies.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
COPY application.properties application.properties
EXPOSE 9999
ENTRYPOINT ["java", "-jar", "app.jar"]