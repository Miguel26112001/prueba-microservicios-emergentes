# ============================================
# STAGE 1: Build con Maven
# ============================================
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copiar pom.xml primero (mejor caché)
COPY pom.xml .

# Descargar dependencias (se cachea si pom.xml no cambia)
RUN mvn -q dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Construir el JAR
RUN mvn -q clean package -DskipTests

# ============================================
# STAGE 2: Runtime con JRE
# ============================================
FROM eclipse-temurin:21-jre-alpine

# Instalar herramientas necesarias
RUN apk add --no-cache curl wget

# Crear usuario no-root por seguridad
RUN addgroup -g 1000 -S appgroup && \
    adduser -u 1000 -S appuser -G appgroup

WORKDIR /app

# Copiar JAR desde el stage builder
COPY --from=builder --chown=appuser:appgroup /app/target/*.jar app.jar

# Exponer puerto
EXPOSE 8070

# Healthcheck
HEALTHCHECK --interval=30s --timeout=5s --start-period=45s --retries=3 \
  CMD curl -f http://localhost:8070/actuator/health || exit 1

# Cambiar a usuario no-root
USER appuser

# JVM optimizada para contenedores
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:InitialRAMPercentage=50.0", \
    "-XX:+UseG1GC", \
    "-XX:+UseStringDeduplication", \
    "-XX:+OptimizeStringConcat", \
    "-jar", \
    "app.jar"]

# Pasar variable de puerto
ENV PORT=8070
ENV SERVER_PORT=8070