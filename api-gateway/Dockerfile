# ===========================
#   API GATEWAY
# ===========================

FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn -q dependency:go-offline -B

COPY src ./src
RUN mvn -q clean package -DskipTests

# ===========================
FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache curl

RUN addgroup -g 1000 -S gateway && \
    adduser -u 1000 -S gateway -G gateway

WORKDIR /app

COPY --from=builder --chown=gateway:gateway /app/target/*.jar app.jar

EXPOSE 8040

HEALTHCHECK --interval=30s --timeout=3s --start-period=20s --retries=3 \
  CMD curl -f http://localhost:8040/actuator/health || exit 1

USER gateway

ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=70.0", \
    "-XX:+UseG1GC", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", \
    "app.jar"]

ENV PORT=8040