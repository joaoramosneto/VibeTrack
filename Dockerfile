FROM maven:3.9.6-openjdk-21 AS builder

# Define o diretório de trabalho dentro do contêiner
WORKDIR /build

# Copia o pom.xml do subdiretório 'backend'
COPY backend/pom.xml .

# Baixa todas as dependências (isto otimiza o cache em builds futuros)
RUN mvn dependency:go-offline

# Copia o código-fonte do subdiretório 'backend'
# Copiamos 'src' para './src' (relativo ao WORKDIR)
COPY backend/src ./src

# Compila a aplicação, constrói o .jar e pula os testes
# O .jar será criado em /build/target/
RUN mvn package -DskipTests


# --- ESTÁGIO 2: Run (Criar a imagem final) ---
# Usamos uma imagem JRE (Java Runtime) otimizada e pequena
FROM eclipse-temurin:17-jre-jammy

# Define o diretório de trabalho final
WORKDIR /app

# Copia APENAS o .jar funcional do estágio de build anterior
# e o renomeia para app.jar
COPY --from=builder /build/target/*.jar app.jar

# Expõe a porta 8080 (padrão do Spring Boot)
EXPOSE 8080

# Comando final para rodar a aplicação
# (As variáveis de ambiente do Cloud Run serão injetadas aqui)
CMD ["java", "-jar", "app.jar"]
