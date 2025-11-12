# --- ESTÁGIO 1: Build (Construir o .jar) ---
# IMAGEM CORRIGIDA: Usa uma etiqueta (tag) completa e válida
FROM eclipse-temurin:21-jdk AS builder

# Define o diretório de trabalho dentro do contêiner
WORKDIR /build

# Copia o pom.xml do subdiretório 'backend'
COPY backend/pom.xml .

# Baixa todas as dependências (isto otimiza o cache em builds futuros)
RUN mvn dependency:go-offline

# Copia o código-fonte do subdiretório 'backend'
COPY backend/src ./src

# Compila a aplicação, constrói o .jar e pula os testes
RUN mvn package -DskipTests


# --- ESTÁGIO 2: Run (Criar a imagem final) ---
# Esta imagem está correta e é otimizada
FROM openjdk:21-jre-slim

# Define o diretório de trabalho final
WORKDIR /app

# Copia APENAS o .jar funcional do estágio de build anterior
# e o renomeia para app.jar
COPY --from=builder /build/target/*.jar app.jar

# Expõe a porta 8080 (padrão do Spring Boot)
EXPOSE 8080

# Comando final para rodar a aplicação
CMD ["java", "-jar", "app.jar"]
