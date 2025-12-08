# VibeTrack - Backend (TCC)

## Descrição do Projeto

O VibeTrack Backend é o servidor da aplicação responsável por gerenciar usuários (pesquisadores e participantes), experimentos, autenticação/autorização via JWT e a captura de dados de dispositivos.

A aplicação é desenvolvida em **Java** com o framework **Spring Boot** (versão 3.3.2).

### Tecnologias Principais

* **Linguagem:** Java 21
* **Framework:** Spring Boot 3.3.2
* **Gerenciador de Dependências:** Maven
* **Banco de Dados (Produção/Externa):** PostgreSQL
* **Banco de Dados (Desenvolvimento Local):** H2 Database (em memória)

***

## 1. Pré-requisitos

* **Java Development Kit (JDK):** Versão 21 ou superior.
* **Apache Maven:** Versão 3.x ou superior.

***

## 2. Execução Local (Perfil de Desenvolvimento)

Por padrão, a aplicação está configurada para iniciar com o perfil de desenvolvimento (`dev`), que utiliza um banco de dados H2 em memória.

### 2.1. Configuração

O perfil `dev` é configurado em `src/main/resources/application-dev.properties` e possui as seguintes características:

* **Banco de Dados:** H2 em memória (`jdbc:h2:mem:testdb`).
* **Comportamento do JPA:** O DDL (Data Definition Language) está configurado como `create-drop`, o que significa que o esquema do banco de dados é recriado a cada inicialização, e todos os dados são perdidos ao desligar.
* **Console H2:** O console web do H2 está ativado e acessível em `/h2-console`.

### 2.2. Iniciar a Aplicação

Navegue até o diretório `backend` e execute:

```bash
# Executa a aplicação Spring Boot usando o plugin Maven
mvn spring-boot:run
```
A aplicação estará acessível em http://localhost:8080/ (a porta padrão do Spring Boot, também exposta no Dockerfile).

### 3.Construção (Build)
Para criar o arquivo JAR executável do projeto (pronto para produção ou Dockerização), navegue até o diretório backend e execute:

```bash
# Compila e empacota o projeto em um arquivo .jar na pasta target/
mvn clean package -DskipTests
```
O arquivo JAR será gerado no diretório backend/target/.

### 4.Dockerização (Opcional)
O projeto inclui um Dockerfile que permite a criação de uma imagem Docker para ambientes isolados ou de produção.

### 4.1. Construir a Imagem Docker
A partir da raiz do projeto VibeTrack (o diretório que contém o subdiretório backend), execute:

```bash
# O build utiliza um estágio multi-stage para gerar uma imagem otimizada.
# O Dockerfile está na raiz, mas copia os arquivos do subdiretório 'backend'.
docker build -t vibetrack-backend:latest .
```

### 4.2. Executar o Contêiner Docker
Após construir a imagem, você pode iniciá-la mapeando a porta local 8080 para a porta 8080 do contêiner:

```bash
# O contêiner expõe a porta 8080.
docker run -d -p 8080:8080 --name vibetrack-server vibetrack-backend:latest
```

A API estará acessível em http://localhost:8080/.
