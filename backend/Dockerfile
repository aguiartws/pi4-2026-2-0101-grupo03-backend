FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copia só o pom.xml e baixa as dependências antes de copiar o código.
# Assim essa camada fica em cache e só é refeita quando o pom.xml muda.
COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

# A aplicação não precisa rodar como root dentro do container.
RUN groupadd --system app && useradd --system --gid app app

COPY --from=build /app/target/*.jar app.jar

USER app

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
