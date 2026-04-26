# ===========================
#   EUREKA DISCOVERY SERVER
# ===========================

FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn -q dependency:go-offline -B

COPY src ./src
RUN mvn -q clean package -DskipTests

# ===========================
FROM eclipse-temurin:21-jre-alpine

# Herramientas para healthcheck
RUN apk add --no-cache curl

RUN addgroup -g 1000 -S eureka && \
    adduser -u 1000 -S eureka -G eureka

WORKDIR /app

COPY --from=builder --chown=eureka:eureka /app/target/*.jar app.jar

EXPOSE 8761

HEALTHCHECK --interval=30s --timeout=5s --start-period=45s --retries=3 \
  CMD curl -f http://localhost:8761/actuator/health || exit 1

USER eureka

ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-jar", \
    "app.jar"]

ENV PORT=8761