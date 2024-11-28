# Use uma imagem que tenha tanto o Maven quanto o JDK
FROM maven:3.8.3-openjdk-17 as builder

WORKDIR /app

# Copie o pom.xml e os arquivos de código fonte para o contêiner
COPY pom.xml .
COPY src src

# Rode o Maven, isso irá construir o projeto e gerar o JAR
RUN mvn clean install -P no-tests

# Agora, construa a imagem real para executar a aplicação
FROM openjdk:17-jdk

# Defina variáveis de ambiente para serem substituídas
ARG DATABASE_USER
ARG DATABASE_PASSWORD
ARG DATABASE_HOST
ARG DATABASE_PORT
ARG DATABASE_NAME
ARG URL_SERVER

# Adicione essas variáveis de ambiente ao container
ENV DATABASE_USER=${DATABASE_USER}
ENV DATABASE_PASSWORD=${DATABASE_PASSWORD}
ENV DATABASE_HOST=${DATABASE_HOST}
ENV DATABASE_PORT=${DATABASE_PORT}
ENV DATABASE_NAME=${DATABASE_NAME}
ENV URL_SERVER=${URL_SERVER}

# Copie o JAR da fase de construção anterior
COPY --from=builder /app/target/donation-ha-api-0.0.1-SNAPSHOT.jar /app.jar

# Execute a aplicação Java
ENTRYPOINT ["java","-jar","/app.jar"]
