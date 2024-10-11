FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/taelim.jar
COPY $JAR_FILE app.jar
COPY libezio.so.1.1.0 /gen/libezio.so.1.1.0
RUN chmod 755 /gen/libezio.so.1.1.0
EXPOSE 8080
ENV LD_LIBRARY_PATH=/gen
ENTRYPOINT ["java", "-Djava.library.path=/gen", "-jar", "./app.jar"]