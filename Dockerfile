# Etapa de construcción
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar archivos de configuración de Maven
COPY leban_challenge/pom.xml .
COPY leban_challenge/src ./src

# Construir la aplicación
RUN mvn clean package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiar el JAR desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto
EXPOSE ${SERVER_PORT:-8083}

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]