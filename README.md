# Sistema de Revenda de Veículos

## 🔧 Especificações Técnicas

### Tecnologias Utilizadas

#### Backend
- **Java**: 21 (LTS)
- **Spring Boot**: 3.3.4
- **Spring Data JPA**: Persistência de dados
- **Spring Validation**: Validação de dados
- **Spring Actuator**: Monitoramento e health checks
- **Maven**: 3.9.5+ (Gerenciamento de dependências)

#### Banco de Dados
- **MySQL**: 8.0
  - Porta padrão: 3306
  - Database: `revenda_veiculos`
  - Charset: UTF-8
- **H2 Database**: Para testes (em memória)
- **Hibernate**: ORM (Object-Relational Mapping)
  - Dialeto: MySQLDialect
  - DDL Auto: update

#### Documentação
- **SpringDoc OpenAPI**: 2.6.0
- **Swagger UI**: Interface interativa da API
- **OpenAPI 3.0**: Especificação da API

#### Ferramentas de Desenvolvimento
- **Lombok**: Redução de código boilerplate
- **MySQL Connector/J**: Driver JDBC para MySQL

#### Testes
- **JUnit 5**: Framework de testes
- **Mockito**: Mocks para testes unitários
- **Spring Boot Test**: Testes de integração
- **Testcontainers**: Testes com containers Docker
  - Testcontainers MySQL
  - Testcontainers JUnit Jupiter
- **Cobertura**: 207 testes (100% dos casos de uso cobertos)

#### Containerização e Orquestração
- **Docker**: Containerização da aplicação
  - Base Image Build: `maven:3.9.5-eclipse-temurin-21`
  - Base Image Runtime: `eclipse-temurin:21-jre-alpine`
  - Multi-stage build para otimização de tamanho
- **Docker Compose**: Orquestração local (app + MySQL)
- **Kubernetes**: Orquestração em produção
  - Minikube para ambiente local
  - Deployments, Services, ConfigMaps, Secrets
  - HPA (Horizontal Pod Autoscaler)
  - Ingress para roteamento

#### Logs e Monitoramento
- **Logback**: Framework de logs
- **SLF4J**: API de logging
- **Spring Actuator**: Health checks e métricas
  - Endpoint: `/actuator/health`

### Requisitos do Ambiente

#### Desenvolvimento Local
```
- Java 21 (JDK)
- Maven 3.9.5+
- MySQL 8.0
- IDE (IntelliJ IDEA, Eclipse, VS Code)
```

#### Docker
```
- Docker Engine 20.10+
- Docker Compose 2.0+
```

#### Kubernetes (opcional)
```
- Minikube 1.30+
- kubectl 1.27+
- Driver: Docker ou VirtualBox
```

### Portas Utilizadas

| Serviço | Porta | Descrição |
|---------|-------|-----------|
| Aplicação Spring Boot | 8080 | API REST |
| MySQL | 3306 | Banco de dados |
| Swagger UI | 8080/swagger-ui.html | Documentação interativa |
| Actuator | 8080/actuator | Monitoramento |

### Variáveis de Ambiente

#### Aplicação
```properties
SPRING_PROFILES_ACTIVE=local|docker|kubernetes
SPRING_DATASOURCE_URL=jdbc:mysql://host:3306/revenda_veiculos
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root
SERVER_PORT=8080
```

#### MySQL (Docker)
```properties
MYSQL_ROOT_PASSWORD=root
MYSQL_DATABASE=revenda_veiculos
MYSQL_USER=app
MYSQL_PASSWORD=app123
```

## 📋 Descrição do Projeto

Este é um sistema completo de revenda de veículos automotores desenvolvido seguindo os princípios de **Clean Architecture** e **SOLID**. A aplicação fornece uma API REST robusta para gerenciar o cadastro, edição, venda e listagem de veículos, além de processar webhooks de pagamento.

## 🏗️ Arquitetura

O projeto foi desenvolvido seguindo os princípios da Clean Architecture, organizando o código em camadas bem definidas:

### Estrutura de Camadas (Código de Produção)

```
src/main/java/org/com/revenda/
├── application/                    # Camada de Aplicação
│   ├── gateway/                   # Interfaces de Gateway (portas)
│   │   ├── VeiculoPersistenceGateway.java
│   │   └── VendaPersistenceGateway.java
│   ├── service/                   # Application Services (orquestração)
│   │   ├── BuscarVeiculoPorIdService.java
│   │   ├── CadastrarVeiculoService.java
│   │   ├── EditarVeiculoService.java
│   │   ├── ListarTodosVeiculosService.java
│   │   ├── ListarVeiculosDisponiveisService.java
│   │   ├── ListarVeiculosPorStatusService.java
│   │   ├── ListarVeiculosVendidosService.java
│   │   ├── ProcessarPagamentoService.java
│   │   └── VenderVeiculoService.java
│   └── usecase/                   # Interfaces de Casos de Uso
│
├── domain/                         # Camada de Domínio (Core)
│   ├── entity/                    # Entidades do domínio
│   │   ├── Veiculo.java
│   │   └── Venda.java
│   ├── enums/                     # Enumerações
│   │   ├── StatusVeiculo.java
│   │   └── StatusPagamento.java
│   └── exception/                 # Exceções do domínio
│       └── VeiculoNaoEncontradoException.java
│
├── infrastructure/                 # Camada de Infraestrutura
│   ├── config/                    # Configurações
│   │   ├── OpenApiConfig.java
│   │   └── LoggingConfig.java
│   ├── persistence/               # Persistência de dados
│   │   ├── adapter/              # Adaptadores (implementam gateways)
│   │   │   ├── VeiculoPersistenceAdapter.java
│   │   │   └── VendaPersistenceAdapter.java
│   │   ├── entity/               # Entidades JPA
│   │   │   ├── VeiculoJpaEntity.java
│   │   │   └── VendaJpaEntity.java
│   │   ├── mapper/               # Mappers (Domain ↔ JPA)
│   │   │   ├── VeiculoMapper.java
│   │   │   └── VendaMapper.java
│   │   └── repository/           # Repositórios Spring Data JPA
│   │       ├── VeiculoJpaRepository.java
│   │       └── VendaJpaRepository.java
│   └── web/                       # Camada Web (API REST)
│       ├── controller/            # Controllers REST
│       │   ├── VeiculoController.java
│       │   └── WebhookController.java
│       ├── dto/                   # DTOs de request/response
│       │   ├── request/          # DTOs de entrada
│       │   │   ├── CadastrarVeiculoRequest.java
│       │   │   ├── VenderVeiculoRequest.java
│       │   │   └── WebhookPagamentoRequest.java
│       │   └── response/         # DTOs de saída
│       │       ├── VeiculoResponse.java
│       │       ├── VendaResponse.java
│       │       ├── VeiculoVendidoResponse.java
│       │       └── VendaComVeiculoResponse.java
│       ├── mapper/               # Mappers de DTO (Domain ↔ DTO)
│       │   ├── VeiculoDtoMapper.java
│       │   └── VendaDtoMapper.java
│       └── exception/            # Tratamento global de exceções
│           └── GlobalExceptionHandler.java
│
└── RevendaVeiculosApplication.java # Classe principal
```

### Estrutura de Testes (Espelhando o Código de Produção)

```
src/test/java/org/com/revenda/
├── application/                    # ✅ Testes da Camada de Aplicação
│   └── service/                   # Testes de Services (8 arquivos)
│       ├── BuscarVeiculoPorIdServiceTest.java
│       ├── CadastrarVeiculoServiceTest.java
│       ├── EditarVeiculoServiceTest.java
│       ├── ListarTodosVeiculosServiceTest.java
│       ├── ListarVeiculosPorStatusServiceTest.java
│       ├── ListarVeiculosVendidosServiceTest.java
│       ├── ProcessarPagamentoServiceTest.java
│       └── VenderVeiculoServiceTest.java
│
├── domain/                         # ✅ Testes da Camada de Domínio
│   └── entity/                    # Testes de Entidades (2 arquivos)
│       ├── VeiculoTest.java
│       └── VendaTest.java
│
├── infrastructure/                 # ✅ Testes da Camada de Infraestrutura
│   ├── persistence/
│   │   ├── adapter/              # Testes de Adaptadores (2 arquivos)
│   │   │   ├── VeiculoPersistenceAdapterTest.java (renomeado)
│   │   │   └── VendaPersistenceAdapterTest.java (renomeado)
│   │   └── mapper/               # Testes de Mappers (2 arquivos)
│   │       ├── VeiculoMapperTest.java
│   │       └── VendaMapperTest.java
│   └── web/
│       ├── controller/            # Testes de Controllers (2 arquivos)
│       │   ├── VeiculoControllerTest.java
│       │   └── WebhookControllerTest.java
│       ├── dto/
│       │   ├── request/          # Testes de Validação (3 arquivos - NOVOS!)
│       │   │   ├── CadastrarVeiculoRequestValidationTest.java
│       │   │   ├── VenderVeiculoRequestValidationTest.java ⭐ NOVO
│       │   │   └── WebhookPagamentoRequestValidationTest.java ⭐ NOVO
│       │   └── response/         # Testes de Response (4 arquivos - NOVOS!)
│       │       ├── VeiculoResponseTest.java ⭐ NOVO
│       │       ├── VendaResponseTest.java ⭐ NOVO
│       │       ├── VendaComVeiculoResponseTest.java ⭐ NOVO
│       │       └── VeiculoVendidoResponseTest.java ⭐ NOVO
│       └── mapper/               # Testes de Mappers Web (1 arquivo)
│           └── VeiculoDtoMapperExtendedTest.java
```

### 📊 Cobertura de Testes

**Total: 207 testes passando com sucesso! ✅**

#### Distribuição por Camada:
- **Application Layer**: 14 testes (Services)
- **Domain Layer**: 11 testes (Entities)
- **Infrastructure - Persistence**: 17 testes (Adapters + Mappers)
- **Infrastructure - Web**: 165 testes
  - Controllers: 6 testes
  - DTOs Request: 80 testes (validações completas)
  - DTOs Response: 37 testes (getters/setters/equals/toString)
  - Mappers: 16 testes

#### Novos Testes Criados (143 testes):
- ✅ **VenderVeiculoRequestValidationTest** - 30 testes de validação
- ✅ **WebhookPagamentoRequestValidationTest** - 26 testes de validação
- ✅ **VeiculoResponseTest** - 9 testes (construtor, getters/setters, equals, toString)
- ✅ **VendaResponseTest** - 9 testes (construtor, getters/setters, equals, toString)
- ✅ **VendaComVeiculoResponseTest** - 8 testes (construtor, getters/setters)
- ✅ **VeiculoVendidoResponseTest** - 11 testes (construtor, getters/setters, equals, toString)
- ✅ **CadastrarVeiculoRequestValidationTest** - 50 testes existentes (organizado com @Nested)

### Princípios SOLID Aplicados

- **SRP** (Single Responsibility): Cada classe tem uma única responsabilidade
  - Use Cases encapsulam uma única regra de negócio
  - Controllers apenas recebem requisições e retornam respostas
  - Adapters fazem apenas a conversão entre camadas
  
- **OCP** (Open/Closed): Extensível para modificações sem alterar código existente
  - Novos use cases podem ser adicionados sem modificar os existentes
  - Novos adapters podem ser criados sem alterar a lógica de negócio
  
- **LSP** (Liskov Substitution): Substituição de implementações via interfaces
  - Repositórios domain podem ser substituídos por diferentes implementações
  - Facilita testes com mocks
  
- **ISP** (Interface Segregation): Interfaces específicas para cada necessidade
  - Repositórios específicos para cada entidade
  - DTOs específicos para cada endpoint
  
- **DIP** (Dependency Inversion): Dependências invertidas através de interfaces
  - Use Cases dependem de interfaces (repository), não de implementações
  - Controllers dependem de Use Cases, não de repositórios

### Fluxo de Dados (Clean Architecture)

```
Controller → Use Case → Repository (interface) → Adapter → JPA Repository → Database
    ↓           ↓            ↓                      ↓           ↓
  DTO       Domain        Domain                 JPA        MySQL
 (JSON)     Entity        Entity                Entity      
```

## ✨ Funcionalidades

### Principais Features

1. ✅ **Cadastrar Veículo** - Adicionar novos veículos ao sistema
2. ✏️ **Editar Veículo** - Modificar dados de veículos disponíveis
3. 💰 **Vender Veículo** - Processar venda com geração de código de pagamento e reserva do veículo
4. 📋 **Listar Veículos** - Filtrar por status (disponível/vendido/reservado) ordenados por preço
5. 🔍 **Buscar Veículo por ID** - Obter detalhes de um veículo específico
6. 📊 **Listar Vendas** - Filtrar vendas por status de pagamento (pendente/confirmado/cancelado)
7. 🔔 **Webhook de Pagamento** - Confirmar ou cancelar pagamentos

### 🌐 Endpoints da API

#### Veículos

- `GET /api/veiculos` - Listar todos os veículos ou filtrar por status
  - Query param: `?status=DISPONIVEL` ou `?status=VENDIDO` ou `?status=RESERVADO`
- `GET /api/veiculos/{id}` - Buscar veículo por ID
- `POST /api/veiculos` - Cadastrar novo veículo
- `PUT /api/veiculos/{id}` - Editar veículo existente
- `POST /api/veiculos/{id}/venda` - Iniciar venda de veículo (marca como RESERVADO)

#### Vendas ⭐ NOVO

- `GET /api/vendas` - Listar todas as vendas ou filtrar por status de pagamento
  - Query param: `?status=PENDENTE` ou `?status=CONFIRMADO` ou `?status=CANCELADO`

#### Webhook

- `POST /api/webhook/pagamento` - Processar status de pagamento (confirma venda ou libera veículo)

### 📚 Documentação da API (Swagger)

A API possui documentação interativa completa via **Swagger UI**, onde você pode visualizar todos os endpoints, modelos de dados e testar as requisições diretamente pelo navegador.

#### Como acessar o Swagger UI:

1. **Certifique-se que a aplicação está rodando** (porta 8080)
2. **Abra seu navegador** (Chrome, Firefox, Edge, etc.)
3. **Acesse a URL**: http://localhost:8080/swagger-ui.html

#### Recursos disponíveis no Swagger:

- 📖 **Documentação completa** de todos os endpoints
- 🧪 **Testar requisições** diretamente pelo navegador (Try it out)
- 📝 **Visualizar modelos** de Request e Response
- ✅ **Validações** e regras de cada campo
- 📊 **Códigos de resposta HTTP** e exemplos

#### Endpoints disponíveis no Swagger:

**Veículos:**
- GET /api/veiculos - Listar/filtrar veículos por status
- GET /api/veiculos/{id} - Buscar veículo por ID
- POST /api/veiculos - Cadastrar novo veículo
- PUT /api/veiculos/{id} - Editar veículo
- POST /api/veiculos/{id}/venda - Vender veículo

**Vendas:** ⭐
- GET /api/vendas - Listar todas as vendas ou filtrar por status de pagamento
  - Filtros disponíveis: PENDENTE, CONFIRMADO, CANCELADO

**Webhook:**
- POST /api/webhook/pagamento - Confirmar ou cancelar pagamento

#### URLs alternativas:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs
- **OpenAPI YAML**: http://localhost:8080/api-docs.yaml

#### Ambientes diferentes:

**Local (desenvolvimento):**
```
http://localhost:8080/swagger-ui.html
```

**Docker:**
```
http://localhost:8080/swagger-ui.html
```

**Kubernetes (Minikube):**
```
http://revenda-veiculos.local/swagger-ui.html
```
> ⚠️ **Nota**: Para Kubernetes, configure o arquivo `hosts` primeiro (veja seção de deploy)

## 🚀 Exemplos de Uso

### 1. Listar veículos disponíveis

```bash
curl http://localhost:8080/api/veiculos?status=DISPONIVEL
```

**Resposta:**
```json
[
  {
    "id": 1,
    "modelo": "Fusca",
    "marca": "VW",
    "ano": 1972,
    "preco": 25000.00,
    "status": "DISPONIVEL"
  },
  {
    "id": 2,
    "modelo": "Civic",
    "marca": "Honda",
    "ano": 2020,
    "preco": 90000.00,
    "status": "DISPONIVEL"
  }
]
```

### 2. Buscar veículo por ID

```bash
curl http://localhost:8080/api/veiculos/1
```

**Resposta:**
```json
{
  "id": 1,
  "modelo": "Fusca",
  "marca": "VW",
  "ano": 1972,
  "preco": 25000.00,
  "status": "DISPONIVEL"
}
```

### 3. Cadastrar um novo veículo

```bash
curl -X POST http://localhost:8080/api/veiculos \
  -H "Content-Type: application/json" \
  -d '{
    "modelo": "Gol",
    "marca": "VW",
    "ano": 2020,
    "preco": 50000.00
  }'
```

**Resposta:**
```json
{
  "id": 3,
  "modelo": "Gol",
  "marca": "VW",
  "ano": 2020,
  "preco": 50000.00,
  "status": "DISPONIVEL"
}
```

### 4. Editar um veículo existente

```bash
curl -X PUT http://localhost:8080/api/veiculos/3 \
  -H "Content-Type: application/json" \
  -d '{
    "modelo": "Gol GTI",
    "marca": "VW",
    "ano": 2021,
    "preco": 55000.00
  }'
```

**Resposta:**
```json
{
  "id": 3,
  "modelo": "Gol GTI",
  "marca": "VW",
  "ano": 2021,
  "preco": 55000.00,
  "status": "DISPONIVEL"
}
```

### 5. Vender um veículo

```bash
curl -X POST http://localhost:8080/api/veiculos/1/venda \
  -H "Content-Type: application/json" \
  -d '{
    "cpfCliente": "473.640.598-98",
    "nomeCliente": "João Silva",
    "valorVenda": 120000.00
  }'
```

**Resposta:**
```json
{
  "id": 1,
  "veiculoId": 1,
  "cpfCliente": "473.640.598-98",
  "dataVenda": "2025-01-18T14:30:00",
  "codigoPagamento": "PAG-ABC12345",
  "statusPagamento": "PENDENTE"
}
```

**Observação:** O veículo agora fica com status `RESERVADO` aguardando confirmação de pagamento.

### 6. Listar vendas ⭐ NOVO

```bash
# Listar todas as vendas
curl http://localhost:8080/api/vendas

# Apenas vendas pendentes
curl "http://localhost:8080/api/vendas?status=PENDENTE"

# Apenas vendas confirmadas
curl "http://localhost:8080/api/vendas?status=CONFIRMADO"

# Apenas vendas canceladas
curl "http://localhost:8080/api/vendas?status=CANCELADO"
```

**Resposta:**
```json
[
  {
    "id": 1,
    "veiculoId": 1,
    "cpfCliente": "473.640.598-98",
    "dataVenda": "2025-01-18T14:30:00",
    "codigoPagamento": "PAG-ABC12345",
    "statusPagamento": "PENDENTE"
  }
]
```

### 7. Confirmar pagamento via webhook

```bash
curl -X POST http://localhost:8080/api/webhook/pagamento \
  -H "Content-Type: application/json" \
  -d '{
    "codigoPagamento": "PAG-ABC12345",
    "status": "CONFIRMADO"
  }'
```

**Resposta:** `200 OK` (sem corpo)

**Observação:** O veículo agora fica com status `VENDIDO`.

### 8. Cancelar pagamento via webhook

```bash
curl -X POST http://localhost:8080/api/webhook/pagamento \
  -H "Content-Type: application/json" \
  -d '{
    "codigoPagamento": "PAG-ABC12345",
    "status": "CANCELADO"
  }'
```

**Resposta:** `200 OK` (sem corpo)

**Observação:** O veículo agora fica disponível para venda novamente.
