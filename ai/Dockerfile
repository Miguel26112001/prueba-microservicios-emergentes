# Dockerfile optimizado
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar pom.xml y descargar dependencias primero (capa cacheable)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código y compilar
COPY src ./src
RUN mvn clean package -DskipTests -Dmaven.test.skip=true

# Etapa de ejecución
FROM eclipse-temurin:21-jre-alpine

# Instalar curl para healthcheck
RUN apk add --no-cache curl

# Crear usuario no-root
RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

# Copiar JAR
COPY --from=build /app/target/*.jar app.jar

# Crear directorio de logs
RUN mkdir -p /app/logs && chown -R spring:spring /app

USER spring:spring

EXPOSE 8090

ENV JAVA_OPTS="-Xmx512m -Xms256m"

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8090/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]