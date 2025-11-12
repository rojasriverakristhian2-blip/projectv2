# Etapa 1: Construcción del JAR con Maven
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar archivos del backend
COPY pom.xml .
COPY src ./src

# Compilar el proyecto (sin tests para acelerar)
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final para ejecutar Spring Boot
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiar el JAR generado
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto del backend
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
