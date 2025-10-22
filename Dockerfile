# Multi-stage build para aplicação Spring Boot
# Usando imagens oficiais Eclipse Temurin para melhor compatibilidade
FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar apenas pom.xml primeiro para cachear dependências
COPY pom.xml .

# Baixar dependências (essa camada será cacheada se pom.xml não mudar)
RUN mvn dependency:go-offline -B

# Agora copiar o código fonte
COPY src ./src

# Build da aplicação COM testes unitários
RUN mvn clean package

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Criar usuário não-root para segurança
RUN addgroup -g 1001 -S spring && \
    adduser -S spring -u 1001

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Ajustar permissões
RUN chown spring:spring app.jar
USER spring:spring

# Expose port
EXPOSE 8080

# Health check melhorado
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
