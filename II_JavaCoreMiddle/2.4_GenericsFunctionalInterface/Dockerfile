FROM openjdk:21
ARG JAR_FILE=target/2.4_GenericsFunctionalInterface-1.0-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
#COPY src/main/resources/functional_interface_input.txt II_JavaCoreMiddle/2.4_GenericsFunctionalInterface/src/main/resources/functional_interface_input.txt
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]