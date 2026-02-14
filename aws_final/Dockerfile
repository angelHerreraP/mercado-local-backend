
#Compilacion (build)
FROM maven:3.9.9-eclipse-temurin-23 AS build
WORKDIR /app

#POM para chacar a Maven
COPY pom.xml .
RUN mvn dependency:go-offline

#Copiamos codigo y compilamos
COPY src ./src
RUN mvn clean package -DskipTest

#Una imagen mas ligera
FROM eclipse-temurin:23-jre
WORKDIR /app


#Traemos el jar de la carpeta anterio
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]

