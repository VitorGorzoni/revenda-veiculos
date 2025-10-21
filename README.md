# Sistema de Revenda de Veículos

## 📋 Descrição do Projeto

Este é um sistema completo de revenda de veículos automotores desenvolvido seguindo os princípios de **Clean Architecture** e **SOLID**. A aplicação fornece uma API REST robusta para gerenciar o cadastro, edição, venda e listagem de veículos, além de processar webhooks de pagamento.

## 🏗️ Arquitetura

O projeto foi desenvolvido seguindo os princípios da Clean Architecture, organizando o código em camadas bem definidas:

### Estrutura de Camadas

```
src/main/java/org/com/revenda/
├── domain/                     # Camada de Domínio (regras de negócio)
│   ├── entity/                 # Entidades do domínio
│   ├── repository/             # Interfaces de repositório
│   ├── usecase/               # Casos de uso (regras de negócio)
│   └── exception/             # Exceções do domínio
├── infrastructure/            # Camada de Infraestrutura
│   └── persistence/           # Persistência de dados
│       ├── entity/            # Entidades JPA
│       ├── repository/        # Repositórios Spring Data
│       ├── adapter/           # Adaptadores de repositório
│       └── mapper/            # Mappers para conversão
└── presentation/              # Camada de Apresentação
    ├── controller/            # Controllers REST
    ├── dto/                   # DTOs de request/response
    ├── mapper/                # Mappers de DTO
    └── exception/             # Tratamento de exceções
```

### Princípios SOLID Aplicados

- **SRP** (Single Responsibility): Cada classe tem uma única responsabilidade
- **OCP** (Open/Closed): Extensível para modificações sem alterar código existente
- **LSP** (Liskov Substitution): Substituição de implementações via interfaces
- **ISP** (Interface Segregation): Interfaces específicas para cada necessidade
- **DIP** (Dependency Inversion): Dependências invertidas através de interfaces

## ✨ Funcionalidades

### Principais Features

1. ✅ **Cadastrar Veículo** - Adicionar novos veículos ao sistema
2. ✏️ **Editar Veículo** - Modificar dados de veículos disponíveis
3. 💰 **Vender Veículo** - Processar venda com geração de código de pagamento
4. 📋 **Listar Veículos** - Filtrar por status (disponível/vendido) ordenados por preço
5. 🔍 **Buscar Veículo por ID** - Obter detalhes de um veículo específico
6. 🔔 **Webhook de Pagamento** - Confirmar ou cancelar pagamentos

### 🌐 Endpoints da API

#### Veículos

- `GET /api/veiculos` - Listar todos os veículos ou filtrar por status
  - Query param: `?status=DISPONIVEL` ou `?status=VENDIDO`
- `GET /api/veiculos/{id}` - Buscar veículo por ID
- `POST /api/veiculos` - Cadastrar novo veículo
- `PUT /api/veiculos/{id}` - Editar veículo existente
- `POST /api/veiculos/{id}/venda` - Vender veículo

#### Webhook

- `POST /api/webhook/pagamento` - Processar status de pagamento

### 📖 Documentação da API

A documentação completa da API está disponível através do Swagger/OpenAPI:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/api-docs`

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 21** - Linguagem principal
- **Spring Boot 3.3.4** - Framework
  - Spring Web
  - Spring Data JPA
  - Spring Validation
  - Spring Actuator
- **MySQL 8.0** - Banco de dados para produção
- **Maven** - Gerenciamento de dependências
- **Lombok** - Redução de boilerplate

### Documentação
- **SpringDoc OpenAPI** - Documentação automática da API

### Testes
- **JUnit 5** - Framework de testes unitários
- **Mockito** - Mocks para testes
- **Spring Boot Test** - Testes de integração
- **JaCoCo** - Cobertura de código (100% de cobertura)

### Containerização e Orquestração
- **Docker** e **Docker Compose**
- **Kubernetes** com manifests completos
- **Minikube** para testes locais

## 🚀 Como Executar Localmente

### Pré-requisitos

- **Java 21** ou superior
- **Maven 3.6+**
- **Docker Desktop** (para execução com containers)

---

## 🐳 Opção 1: Executar com Docker Compose (Recomendado)

A forma **mais simples e rápida** de executar a aplicação localmente é usando os scripts de gerenciamento Docker:

### Início Rápido

#### **Windows**
```bash
# 1. Clone o repositório
git clone <url-do-repositorio>
cd revenda-veiculos

# 2. Iniciar a aplicação (build + MySQL + App)
scripts\docker-manager.bat start
```

#### **Linux/Mac**
```bash
# 1. Clone o repositório
git clone <url-do-repositorio>
cd revenda-veiculos

# 2. Dar permissão de execução (primeira vez)
chmod +x scripts/docker-manager.sh

# 3. Iniciar a aplicação (build + MySQL + App)
./scripts/docker-manager.sh start
```

✅ **Pronto!** A aplicação estará disponível em:
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html

### 📋 Comandos Disponíveis

#### **Windows (docker-manager.bat)**
```bash
# Build das imagens Docker
scripts\docker-manager.bat build

# Iniciar containers (reconstrói automaticamente)
scripts\docker-manager.bat start

# Parar containers
scripts\docker-manager.bat stop

# Reiniciar containers (rebuild + restart)
scripts\docker-manager.bat restart

# Ver logs em tempo real
scripts\docker-manager.bat logs

# Ver logs de um serviço específico
scripts\docker-manager.bat logs app
scripts\docker-manager.bat logs mysql

# Ver status dos containers
scripts\docker-manager.bat status

# Limpar tudo (containers, volumes, imagens)
scripts\docker-manager.bat clean

# Ambiente de desenvolvimento (com hot reload e debug)
scripts\docker-manager.bat dev

# Ambiente de produção
scripts\docker-manager.bat prod

# Ver ajuda completa
scripts\docker-manager.bat help
```

#### **Linux/Mac (docker-manager.sh)**
```bash
# Build das imagens Docker
./scripts/docker-manager.sh build

# Iniciar containers (reconstrói automaticamente)
./scripts/docker-manager.sh start

# Parar containers
./scripts/docker-manager.sh stop

# Reiniciar containers (rebuild + restart)
./scripts/docker-manager.sh restart

# Ver logs em tempo real
./scripts/docker-manager.sh logs

# Ver logs de um serviço específico
./scripts/docker-manager.sh logs app
./scripts/docker-manager.sh logs mysql

# Ver status dos containers
./scripts/docker-manager.sh status

# Limpar tudo (containers, volumes, imagens)
./scripts/docker-manager.sh clean

# Ambiente de desenvolvimento (com hot reload e debug)
./scripts/docker-manager.sh dev

# Ambiente de produção
./scripts/docker-manager.sh prod

# Ver ajuda completa
./scripts/docker-manager.sh help
```

### 🎯 O que o script faz?

- ✅ **Build automático** - Reconstrói a imagem Docker antes de iniciar
- ✅ **MySQL configurado** - Banco de dados pronto para uso
- ✅ **Healthchecks** - Aguarda o MySQL estar pronto antes de iniciar a app
- ✅ **Volumes persistentes** - Dados do MySQL são preservados
- ✅ **Cache otimizado** - Dependências Maven são cacheadas (builds mais rápidos)
- ✅ **Restart inteligente** - Reinicia apenas se falhar

---

## ☸️ Opção 2: Executar no Minikube (Kubernetes Local)

Para testar a aplicação em um ambiente **Kubernetes local**, use o script `minikube-deploy.bat`:

### Pré-requisitos Adicionais

1. **Instalar Minikube e kubectl**

   **Opção A: Script Automático (Recomendado)**
   ```bash
   # Execute como Administrador
   scripts\instalar-minikube.bat
   ```

   **Opção B: Manual via Chocolatey**
   ```powershell
   # Execute PowerShell como Administrador
   choco install minikube kubernetes-cli -y
   ```

2. **Certifique-se que o Docker Desktop está rodando**

### 🚀 Deploy Completo no Minikube

```bash
# Deploy completo (setup + build + deploy)
scripts\minikube-deploy.bat all
```

O script executará automaticamente:
1. ⚙️ **Setup** - Iniciar o Minikube e habilitar addons
2. 🐳 **Build** - Construir a imagem Docker dentro do Minikube
3. 🚀 **Deploy** - Aplicar todos os manifests Kubernetes
4. 🌐 **Access** - Mostrar a URL de acesso

### 📋 Comandos do minikube-deploy.bat

```bash
# Configurar Minikube (iniciar + addons)
scripts\minikube-deploy.bat setup

# Build da imagem Docker no Minikube
scripts\minikube-deploy.bat build

# Deploy da aplicação no Kubernetes
scripts\minikube-deploy.bat deploy

# Deploy completo (setup + build + deploy)
scripts\minikube-deploy.bat all

# Ver status dos pods e services
scripts\minikube-deploy.bat status

# Ver instruções e URLs de acesso
scripts\minikube-deploy.bat access

# Limpar todos os recursos
scripts\minikube-deploy.bat cleanup

# Ver ajuda completa
scripts\minikube-deploy.bat help
```

### 🌐 Como Acessar a Aplicação no Minikube

Após o deploy, você tem **3 opções**:

#### **Opção 1: Via NodePort (Mais Simples)** ⭐

```bash
# O script já mostra a URL automaticamente após o deploy
scripts\minikube-deploy.bat access

# A URL será algo como: http://192.168.49.2:30220
```

Acesse:
- **API**: `http://<IP>:<PORTA>`
- **Swagger**: `http://<IP>:<PORTA>/swagger-ui.html`

#### **Opção 2: Via Ingress**

```bash
# 1. Obter IP do Minikube
minikube ip

# 2. Adicionar no arquivo hosts (Execute como Administrador)
# Arquivo: C:\Windows\System32\drivers\etc\hosts
# Adicione: <IP_DO_MINIKUBE> revenda-veiculos.local
# Exemplo: 192.168.49.2 revenda-veiculos.local

# 3. Acessar
# http://revenda-veiculos.local
# http://revenda-veiculos.local/swagger-ui.html
```

#### **Opção 3: Via Tunnel**

```bash
# Execute em um terminal separado (mantenha rodando)
minikube tunnel

# Acesse: http://revenda-veiculos.local
```

### 📦 Recursos Deployados no Kubernetes

O script `minikube-deploy.bat all` cria automaticamente:

- ✅ **Namespace**: `revenda-veiculos`
- ✅ **ConfigMap**: Configurações da aplicação
- ✅ **Secret**: Credenciais do banco de dados (base64)
- ✅ **MySQL**: 
  - Deployment (1 réplica)
  - Service (ClusterIP)
  - PersistentVolumeClaim (armazenamento persistente)
- ✅ **Aplicação**: 
  - Deployment (2 réplicas)
  - Service (LoadBalancer)
  - Ingress (roteamento HTTP)
  - HPA (auto-scaling: 2-10 réplicas)

### 🔧 Comandos Úteis do Kubernetes

```bash
# Ver todos os recursos
kubectl get all -n revenda-veiculos

# Ver logs da aplicação em tempo real
kubectl logs -f -n revenda-veiculos -l app=revenda-app

# Ver logs do MySQL
kubectl logs -f -n revenda-veiculos -l app=mysql

# Ver detalhes de um pod
kubectl describe pod <nome-do-pod> -n revenda-veiculos

# Acessar shell de um pod
kubectl exec -it <nome-do-pod> -n revenda-veiculos -- /bin/sh

# Escalar manualmente a aplicação
kubectl scale deployment revenda-app-deployment -n revenda-veiculos --replicas=5

# Ver eventos (útil para troubleshooting)
kubectl get events -n revenda-veiculos --sort-by='.lastTimestamp'

# Ver uso de recursos
kubectl top pods -n revenda-veiculos
```

---

## 📝 Opção 3: Executar com Maven (Sem Docker)

```bash
# 1. Configurar banco de dados MySQL local
mysql -u root -p
CREATE DATABASE revenda_veiculos;
CREATE USER 'app'@'localhost' IDENTIFIED BY 'app123';
GRANT ALL PRIVILEGES ON revenda_veiculos.* TO 'app'@'localhost';

# 2. Executar a aplicação
mvn spring-boot:run

# Ou compilar e executar o JAR
mvn clean package -DskipTests
java -jar target/revenda-veiculos-1.0.0.jar
```

---

## 🧪 Testes

### Executar Testes Unitários

O projeto possui **100% de cobertura** de testes unitários (48+ cenários de teste):

```bash
# Executar todos os testes
mvn test

# Executar com relatório de cobertura JaCoCo
mvn clean test jacoco:report

# Ver relatório de cobertura no navegador
# Abrir: target/site/jacoco/index.html
```

### Estrutura de Testes

- ✅ **Use Cases** (8 classes) - Testes de regras de negócio
- ✅ **Controllers** (1 classe) - Testes de endpoints
- ✅ **Entities** (2 classes) - Testes de entidades de domínio
- ✅ **Mappers** (2 classes) - Testes de conversão de dados
- ✅ **Adapters** (2 classes) - Testes de integração com repositórios

📘 **Documentação detalhada**: Consulte [TESTES.md](TESTES.md) para mais informações sobre os testes.

---

## 📚 Documentação Adicional

- 📘 [TESTES.md](TESTES.md) - Guia completo de testes unitários e cobertura
- 📘 [INSTALACAO_MINIKUBE.md](INSTALACAO_MINIKUBE.md) - Guia detalhado de instalação do Minikube
- 📘 [GUIA_TESTES_ENDPOINTS.md](GUIA_TESTES_ENDPOINTS.md) - Como testar os endpoints da API
- 📘 [k8s/README.md](k8s/README.md) - Documentação dos manifests Kubernetes

---

## 🔍 Exemplo de Uso da API

### 1. Cadastrar um veículo

```bash
curl -X POST http://localhost:8080/api/veiculos \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Toyota",
    "modelo": "Corolla",
    "ano": 2023,
    "cor": "Prata",
    "preco": 120000.00
  }'
```

**Resposta:**
```json
{
  "id": 1,
  "marca": "Toyota",
  "modelo": "Corolla",
  "ano": 2023,
  "cor": "Prata",
  "preco": 120000.00,
  "status": "DISPONIVEL",
  "dataCadastro": "2025-01-18T10:00:00"
}
```

### 2. Listar veículos

```bash
# Todos os veículos
curl http://localhost:8080/api/veiculos

# Apenas disponíveis
curl "http://localhost:8080/api/veiculos?status=DISPONIVEL"

# Apenas vendidos
curl "http://localhost:8080/api/veiculos?status=VENDIDO"
```

**Resposta:**
```json
[
  {
    "id": 1,
    "marca": "Toyota",
    "modelo": "Corolla",
    "ano": 2023,
    "cor": "Prata",
    "preco": 120000.00,
    "status": "DISPONIVEL",
    "dataCadastro": "2025-01-18T10:00:00"
  }
]
```

### 3. Buscar veículo por ID

```bash
curl http://localhost:8080/api/veiculos/1
```

**Resposta:**
```json
{
  "id": 1,
  "marca": "Toyota",
  "modelo": "Corolla",
  "ano": 2023,
  "cor": "Prata",
  "preco": 120000.00,
  "status": "DISPONIVEL",
  "dataCadastro": "2025-01-18T10:00:00"
}
```

### 4. Editar um veículo

```bash
curl -X PUT http://localhost:8080/api/veiculos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Toyota",
    "modelo": "Corolla XEI",
    "ano": 2024,
    "cor": "Preto",
    "preco": 135000.00
  }'
```

**Resposta:**
```json
{
  "id": 1,
  "marca": "Toyota",
  "modelo": "Corolla XEI",
  "ano": 2024,
  "cor": "Preto",
  "preco": 135000.00,
  "status": "DISPONIVEL",
  "dataCadastro": "2025-01-18T10:00:00"
}
```

### 5. Vender um veículo

```bash
curl -X POST http://localhost:8080/api/veiculos/1/venda \
  -H "Content-Type: application/json" \
  -d '{
    "cpfComprador": "123.456.789-00",
    "dataVenda": "2025-01-18T14:30:00"
  }'
```

**Resposta:**
```json
{
  "id": 1,
  "veiculoId": 1,
  "cpfComprador": "123.456.789-00",
  "dataVenda": "2025-01-18T14:30:00",
  "codigoPagamento": "PAG-ABC12345",
  "statusPagamento": "PENDENTE"
}
```

### 6. Confirmar pagamento via webhook

```bash
curl -X POST http://localhost:8080/api/webhook/pagamento \
  -H "Content-Type: application/json" \
  -d '{
    "codigoPagamento": "PAG-ABC12345",
    "status": "CONFIRMADO"
  }'
```

**Resposta:** `200 OK` (sem corpo)

### 7. Cancelar pagamento via webhook

```bash
curl -X POST http://localhost:8080/api/webhook/pagamento \
  -H "Content-Type: application/json" \
  -d '{
    "codigoPagamento": "PAG-ABC12345",
    "status": "CANCELADO"
  }'
```

**Resposta:** `200 OK` (sem corpo)

---

## 📦 Estrutura do Projeto

```
revenda-veiculos/
├── docker/                      # Configurações Docker
│   └── mysql/
│       └── init.sql            # Script de inicialização do MySQL
├── k8s/                        # Manifests Kubernetes
│   ├── namespace.yaml
│   ├── configmap.yaml
│   ├── secret.yaml
│   ├── mysql-*.yaml
│   ├── app-*.yaml
│   └── README.md
├── scripts/                    # Scripts de automação
│   ├── docker-manager.bat      # Gerenciador Docker
│   ├── minikube-deploy.bat     # Deploy no Minikube
│   └── instalar-minikube.bat   # Instalação do Minikube
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/com/revenda/
│   │   │       ├── domain/     # Camada de domínio
│   │   │       ├── infrastructure/  # Camada de infraestrutura
│   │   │       └── presentation/    # Camada de apresentação
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-docker.yml
│   │       └── application-kubernetes.yml
│   └── test/                   # Testes unitários (100% cobertura)
├── docker-compose.yml          # Docker Compose produção
├── docker-compose.dev.yml      # Docker Compose desenvolvimento
├── Dockerfile                  # Imagem de produção
├── Dockerfile.dev              # Imagem de desenvolvimento
├── pom.xml                     # Dependências Maven
└── README.md                   # Este arquivo
```

---

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto está sob a licença MIT.

---

## 👥 Autores

- **Seu Nome** - Desenvolvimento inicial

---

## 🎯 Roadmap

- [ ] Implementar autenticação JWT
- [ ] Adicionar cache com Redis
- [ ] Integração com serviços de pagamento externos
- [ ] Dashboard administrativo
- [ ] Notificações por email
- [ ] API de relatórios

---

## 📞 Suporte

Para dúvidas ou problemas:
- 📧 Email: seuemail@example.com
- 🐛 Issues: [GitHub Issues](https://github.com/usuario/revenda-veiculos/issues)
- 📖 Documentação: [Wiki do Projeto](https://github.com/usuario/revenda-veiculos/wiki)

---

**Desenvolvido com ❤️ usando Clean Architecture e SOLID**
