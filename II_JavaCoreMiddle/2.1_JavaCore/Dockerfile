FROM openjdk:21
ARG JAR_FILE=target/2.1_JavaCore-1.0-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
#COPY src/main/resources/figures_set.json II_JavaCoreMiddle/2.1_JavaCore/src/main/resources/figures_set.json
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]