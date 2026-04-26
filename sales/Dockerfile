# ===== Build Stage =====
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copiar archivos de dependencias primero (cache)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar jar
RUN mvn clean package -DskipTests

# ===== Run Stage =====
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]