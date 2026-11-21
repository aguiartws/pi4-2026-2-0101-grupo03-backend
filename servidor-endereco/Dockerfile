# ---- build ----
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY src/ServidorValidador.java src/ValidadorEndereco.java ./
RUN javac -encoding UTF-8 *.java

# ---- runtime ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/*.class .
EXPOSE 5000
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "ServidorValidador"]