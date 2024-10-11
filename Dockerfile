FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/taelim.jar
COPY $JAR_FILE app.jar
EXPOSE 8080
ENV LD_LIBRARY_PATH=/gen/x64
ENTRYPOINT ["java", "-Djava.library.path=/gen/x64", "-jar", "./app.jar"]