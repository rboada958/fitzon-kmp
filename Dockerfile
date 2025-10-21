FROM openjdk:17-jdk-slim AS builder

WORKDIR /app

# Copiar archivos de configuración de Gradle
COPY gradle gradle
COPY gradlew gradlew.bat gradle.properties settings.gradle.kts ./

# Copiar archivos de construcción
COPY build.gradle.kts ./
COPY server/build.gradle.kts server/
COPY composeApp/build.gradle.kts composeApp/

# Copiar el código fuente
COPY server/src server/src
COPY shared shared

# Dar permisos de ejecución
RUN chmod +x ./gradlew

# Construir el fat jar
RUN ./gradlew :server:buildFatJar --no-daemon

# Runtime stage
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar el JAR compilado
COPY --from=builder /app/server/build/libs/*-all.jar app.jar

# Exponer puerto
EXPOSE 8080

# Variables de entorno opcionales
ENV PORT=8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]