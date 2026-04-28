# ===========================
#   MULTI-STAGE BUILD OPTIMIZADO
# ===========================

# ----- Build Stage -----
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Optimización: Capa de dependencias (se cachea)
COPY pom.xml .
RUN mvn -q dependency:go-offline -B

# Capa de código fuente
COPY src ./src

# Build con optimizaciones
RUN mvn -q clean package -DskipTests \
    -Dmaven.javadoc.skip=true \
    -Dmaven.source.skip=true

# ----- Run Stage -----
FROM eclipse-temurin:21-jre-alpine AS runner

# Instalar herramientas útiles (opcional)
RUN apk add --no-cache curl wget

# Crear usuario no-root
RUN addgroup -g 1000 -S appgroup && \
    adduser -u 1000 -S appuser -G appgroup

WORKDIR /app

# Copiar jar con permisos
COPY --from=builder --chown=appuser:appgroup /app/target/*.jar app.jar

# Puerto
EXPOSE 8080

# Healthcheck (mejorado)
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8085/actuator/health || exit 1

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

# Pasar variable de puerto por defecto
ENV PORT=8080
CMD ["--server.port=${PORT}"]