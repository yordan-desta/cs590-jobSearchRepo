FROM maven:3.5.2-jdk-8-alpine AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn package  -Dmaven.test.skip

FROM openjdk:8-jdk-alpine

ARG JAR_FILE=target/*.jar
COPY --from=MAVEN_TOOL_CHAIN /tmp/target/*.jar salah.jar
ENTRYPOINT ["java","-jar","/salah.jar"]