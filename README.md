# Report Service

O **Report Service** é um microsserviço desenvolvido em Java com Spring Boot, responsável pelo gerenciamento e processamento de denúncias (reports) relacionadas a leilões (Auctions) e mensagens (Messages) [cite: 1].

## Visão Geral

Este serviço fornece uma API REST para o registro e consulta de denúncias, além de integrar-se ativamente com o Apache Kafka para processamento assíncrono de eventos de denúncias [cite: 1]. Ele conta com persistência em banco de dados relacional, métricas de consumo e infraestrutura pronta para conteinerização [cite: 1].

## Principais Funcionalidades

* **Gerenciamento de Denúncias de Leilões:** Endpoints (`AuctionReportController`) e lógica de negócios (`AuctionReportService`) para reportar problemas em leilões [cite: 1].
* **Gerenciamento de Denúncias de Mensagens:** Endpoints (`MessageReportController`) e lógica de negócios (`MessageReportService`) para reportar mensagens inadequadas [cite: 1].
* **Processamento Assíncrono:** Integração com Kafka (`KafkaReportConsumer`, `KafkaService`) para reagir a eventos do sistema (`AuctionReported`, `MessageReported`) [cite: 1].
* **Métricas de Sistema:** Coleta de métricas de consumo e de relatórios gerados através dos pacotes de métricas internos [cite: 1].
* **Deploy Facilitado:** Configurações prontas para Docker (`Dockerfile`) e Docker Compose (`docker-compose.yml`) [cite: 1].

## Tecnologias Utilizadas

* Java [cite: 1]
* Spring Boot [cite: 1]
* Apache Maven [cite: 1]
* Apache Kafka [cite: 1]
* Spring Data JPA / SQL [cite: 1]
* Docker & Docker Compose [cite: 1]

## Estrutura do Projeto

A arquitetura do projeto segue um padrão modular com divisões claras de responsabilidade:

* `controller/`: Controladores REST para exposição de endpoints [cite: 1].
* `domain/`: Entidades de banco de dados e modelos de domínio [cite: 1].
* `dto/`: Objetos de transferência de dados (`AuctionReportRequest`, `MessageReportRequest`) [cite: 1].
* `exception/`: Tratamento de erros e exceções personalizadas como `ReportNotFoundException` [cite: 1].
* `kafka/`: Serviços, consumidores e eventos para integração com mensageria [cite: 1].
* `metrics/`: Componentes para coleta e exposição de métricas da aplicação [cite: 1].
* `repository/`: Interfaces de acesso a dados [cite: 1].
* `service/`: Lógica de negócios central do microsserviço [cite: 1].

## Como Executar

### Pré-requisitos

* Java Development Kit (JDK)
* Docker e Docker Compose (para rodar serviços auxiliares como Kafka e Banco de Dados) [cite: 1]

### Passos para execução local

1. Clone o repositório.
2. Copie o arquivo de ambiente de exemplo e ajuste as variáveis necessárias:
   ```bash
   cp .env.example .env
   ```
3. Inicie a infraestrutura necessária utilizando o Docker Compose [cite: 1]:
   ```bash
   docker-compose up -d
   ```
4. Execute a aplicação usando o Maven Wrapper [cite: 1]:
   ```bash
   ./mvnw spring-boot:run
   ```

### Executando com Docker

Para construir e rodar a imagem da aplicação inteiramente via Docker [cite: 1]:

```bash
docker build -t report-service .
docker run -p 8080:8080 report-service
```