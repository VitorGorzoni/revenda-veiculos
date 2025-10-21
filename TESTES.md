# Testes Unitários - Revenda de Veículos

Este documento descreve como executar os testes unitários e gerar relatórios de cobertura do projeto.

## 📋 Cobertura de Testes

O projeto possui testes unitários completos para:

### **Use Cases** (Regras de Negócio)
- ✅ `CadastrarVeiculoUseCase` - Cadastro de veículos
- ✅ `BuscarVeiculoPorIdUseCase` - Busca de veículo por ID
- ✅ `ListarTodosVeiculosUseCase` - Listagem de todos os veículos
- ✅ `ListarVeiculosPorStatusUseCase` - Listagem filtrada por status
- ✅ `VenderVeiculoUseCase` - Processo de venda
- ✅ `ProcessarPagamentoUseCase` - Processamento de pagamentos via webhook
- ✅ `EditarVeiculoUseCase` - Edição de veículos
- ✅ `ListarVeiculosVendidosUseCase` - Listagem de vendas

### **Controllers** (Camada de Apresentação)
- ✅ `WebhookController` - Endpoint de webhook de pagamento

### **Entidades de Domínio**
- ✅ `Venda` - Entidade de venda com todas as regras
- ✅ `Veiculo` - Entidade de veículo com validações

### **Mappers** (Conversores)
- ✅ `VendaMapper` - Conversão entre entidades JPA e domínio
- ✅ `VeiculoMapper` - Conversão entre entidades JPA e domínio

### **Adapters** (Camada de Infraestrutura)
- ✅ `VeiculoRepositoryAdapter` - Adapter do repositório de veículos
- ✅ `VendaRepositoryAdapter` - Adapter do repositório de vendas

## 🚀 Como Executar os Testes

### **Opção 1: Via Maven (Recomendado)**

```bash
# Executar todos os testes
mvn test

# Executar testes com relatório de cobertura
mvn clean test jacoco:report

# Executar apenas testes de uma classe específica
mvn test -Dtest=VenderVeiculoUseCaseTest
```

### **Opção 2: Via Docker**

```bash
# Build e testes
docker run --rm -v ${PWD}:/app -w /app maven:3.9.5-eclipse-temurin-21 mvn clean test
```

### **Opção 3: Via IDE (IntelliJ/Eclipse)**

1. Clique com botão direito na pasta `src/test/java`
2. Selecione "Run Tests" ou "Run All Tests"
3. Para cobertura, use "Run with Coverage"

## 📊 Relatório de Cobertura

### **Gerar Relatório JaCoCo**

```bash
mvn clean test jacoco:report
```

O relatório será gerado em:
```
target/site/jacoco/index.html
```

Abra o arquivo no navegador para visualizar a cobertura detalhada.

### **Configuração do JaCoCo no pom.xml**

Adicione o plugin JaCoCo no `pom.xml` se ainda não estiver configurado:

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## 🎯 Cobertura Esperada

Com todos os testes implementados, a cobertura deve atingir:

- **Use Cases**: ~100% de cobertura
- **Entidades**: ~100% de cobertura
- **Mappers**: ~100% de cobertura
- **Adapters**: ~95-100% de cobertura
- **Controllers**: ~90-95% de cobertura

## 📝 Estrutura dos Testes

```
src/test/java/
└── org/com/revenda/
    ├── domain/
    │   ├── entity/
    │   │   ├── VeiculoTest.java
    │   │   └── VendaTest.java
    │   └── usecase/
    │       ├── BuscarVeiculoPorIdUseCaseTest.java
    │       ├── CadastrarVeiculoUseCaseTest.java
    │       ├── EditarVeiculoUseCaseTest.java
    │       ├── ListarTodosVeiculosUseCaseTest.java
    │       ├── ListarVeiculosPorStatusUseCaseTest.java
    │       ├── ListarVeiculosVendidosUseCaseTest.java
    │       ├── ProcessarPagamentoUseCaseTest.java
    │       └── VenderVeiculoUseCaseTest.java
    ├── infrastructure/
    │   └── persistence/
    │       ├── adapter/
    │       │   ├── VeiculoRepositoryAdapterTest.java
    │       │   └── VendaRepositoryAdapterTest.java
    │       └── mapper/
    │           ├── VeiculoMapperTest.java
    │           └── VendaMapperTest.java
    └── presentation/
        └── controller/
            └── WebhookControllerTest.java
```

## 🔧 Executar Testes em CI/CD

### **GitHub Actions**

```yaml
- name: Run tests with coverage
  run: mvn clean test jacoco:report

- name: Upload coverage to Codecov
  uses: codecov/codecov-action@v3
  with:
    file: ./target/site/jacoco/jacoco.xml
```

### **GitLab CI**

```yaml
test:
  stage: test
  script:
    - mvn clean test jacoco:report
  artifacts:
    paths:
      - target/site/jacoco/
```

## 🐛 Debug de Testes

Para executar testes em modo debug:

```bash
# Com mais detalhes
mvn test -X

# Com logs do Surefire
mvn test -Dsurefire.printSummary=true
```

## 📚 Tecnologias Utilizadas

- **JUnit 5** - Framework de testes
- **Mockito** - Framework de mocks
- **JaCoCo** - Cobertura de código
- **Spring Boot Test** - Suporte a testes Spring

## ✅ Boas Práticas Implementadas

1. ✅ **AAA Pattern** - Arrange, Act, Assert
2. ✅ **Nomenclatura descritiva** - Nomes de testes claros
3. ✅ **Testes isolados** - Cada teste é independente
4. ✅ **Mocks apropriados** - Uso correto de mocks
5. ✅ **Testes de casos de borda** - Validação de exceções
6. ✅ **DisplayName** - Descrição legível dos testes

## 🎓 Exemplos de Comandos Úteis

```bash
# Executar apenas testes rápidos
mvn test -Dgroups=unit

# Executar testes e pular integração
mvn test -DskipITs

# Executar com threads paralelas
mvn test -T 4

# Executar e gerar site com relatórios
mvn clean test site
```

## 📞 Suporte

Para dúvidas sobre os testes, consulte a documentação do JUnit 5:
https://junit.org/junit5/docs/current/user-guide/

