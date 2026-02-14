
# Usamos la imagen base directamente porque ya compilamos afuera
FROM eclipse-temurin:23-jre
WORKDIR /app

# Copiamos el JAR generado por GitHub Actions
COPY target/*.jar app.jar

EXPOSE 8081

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
