# Build stage
FROM openjdk:17-jdk-slim AS builder

WORKDIR /app

# Copiar los archivos necesarios del wrapper y la configuración de Gradle
COPY gradlew gradlew.bat gradle/ ./

# Copiar el resto del proyecto
COPY server .

# Dar permisos de ejecución al wrapper
RUN chmod +x ./gradlew

# Construir el fat jar usando el wrapper
RUN ./gradlew :server:buildFatJar --no-daemon

# Runtime stage
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar el JAR compilado desde el builder
COPY --from=builder /app/server/build/libs/*-all.jar app.jar

# Exponer puerto
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]