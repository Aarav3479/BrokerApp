# syntax=docker/dockerfile:1.4

################################
# Build stage
################################
FROM eclipse-temurin:21-jdk@sha256:ec2005c536f3661c6ef1253292c9c623e582186749a3ef2ed90903d1aaf74640 AS build
ENV HOME=/usr/app
WORKDIR ${HOME}
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN if [ -f ./mvnw ]; then chmod +x ./mvnw; fi
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B dependency:go-offline
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B -f pom.xml clean package -DskipTests

################################
# Runtime stage
################################
FROM eclipse-temurin:21-jre@sha256:4332b7939ba5b7fabde48f4da21ebe45a4f8943d5b3319720c321ac577e65fb1 AS runtime
RUN groupadd -r appgroup \
 && useradd -r -g appgroup -s /usr/sbin/nologin -M appuser \
 && mkdir -p /app \
 && chown appuser:appgroup /app

WORKDIR /app

ARG JAR_FILE=/usr/app/target/*.jar

COPY --from=build --chown=appuser:appgroup ${JAR_FILE} /app/runner.jar

EXPOSE 8080

USER appuser
ENTRYPOINT ["java","-jar","/app/runner.jar"]
