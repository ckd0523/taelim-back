FROM openjdk:17-jdk-alpine
ARG JAR_FILE=build/libs/taelim.jar
COPY $JAR_FILE app.jar
EXPOSE 8080
# 필수 라이브러리 설치
RUN apt-get update && apt-get install -y \
    libudev1 \
    libc6 \
    && rm -rf /var/lib/apt/lists/*
ENV LD_LIBRARY_PATH=/gen/x64
ENTRYPOINT ["java", "-Djava.library.path=/gen/x64", "-jar", "./app.jar"]