FROM openjdk:21
ARG JAR_FILE=target/2.2_CollectionsListSet-1.0-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
#COPY src/main/resources/integer_list_input.txt II_JavaCoreMiddle/2.2_CollectionsListSet/src/main/resources/integer_list_input.txt
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]