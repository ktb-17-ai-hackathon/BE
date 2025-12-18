#FROM gradle:8.5-jdk21 AS builder
#
#WORKDIR /app
#
#COPY build.gradle settings.gradle ./
#COPY gradle ./gradle
#RUN gradle dependencies --no-daemon || true
#
#COPY . .
#RUN gradle clean bootJar --no-daemon
FROM eclipse-temurin:21-jre-alpine

# 타임존 설정 추가
RUN apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/Asia/Seoul /etc/localtime \
    && echo "Asia/Seoul" > /etc/timezone

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]