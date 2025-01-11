FROM openjdk:21
ARG JAR_FILE=target/2.3_CollectionsHashMap-1.0-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
#COPY src/main/resources/hash_map_input.txt II_JavaCoreMiddle/2.3_CollectionsHashMap/src/main/resources/hash_map_input.txt
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]