# 🧪 Guia Completo de Testes da API Revenda de Veículos v2.0.0

## 🆕 Novidades da v2.0.0
- **Endpoint Unificado**: `GET /api/veiculos?status={DISPONIVEL|VENDIDO}`
- **Filtros Flexíveis**: Busque todos os veículos ou filtre por status
- **Melhor Performance**: Endpoints otimizados com ordenação automática
- **Backward Compatibility**: Endpoints antigos ainda funcionam (depreciados)

## 🚀 Como Acessar a Aplicação

Sua aplicação está rodando no Minikube! Aqui estão as formas de acessá-la:

### Opção 1: Port Forward (Recomendado)
```bash
kubectl port-forward service/revenda-app-service 8080:80 -n revenda-veiculos
```
**Acesse**: http://localhost:8080

### Opção 2: Ingress + Hosts File
1. Adicione no arquivo hosts (`C:\Windows\System32\drivers\etc\hosts`):
```
192.168.49.2 revenda-veiculos.local
```
2. **Acesse**: http://revenda-veiculos.local

### Opção 3: Minikube Tunnel
```bash
minikube tunnel
```

---

## 📚 Documentação Interativa

### 🎯 Swagger UI (Recomendado)
**Acesse**: http://localhost:8080/swagger-ui.html

A documentação Swagger foi completamente atualizada com:
- ✅ Exemplos interativos do novo endpoint unificado
- ✅ Descrições detalhadas dos filtros
- ✅ Marcação clara dos endpoints depreciados
- ✅ Validação automática de parâmetros

---

## 📋 Endpoints da API

### 🔍 **1. Health Check**
```http
GET /actuator/health
```
**Resposta Esperada:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "MySQL",
        "validationQuery": "isValid()"
      }
    }
  }
}
```

### 🚗 **2. Cadastrar Veículo**
```http
POST /api/veiculos
Content-Type: application/json

{
  "marca": "Toyota",
  "modelo": "Corolla",
  "ano": 2023,
  "cor": "Prata",
  "preco": 85000.00
}
```

**Resposta Esperada (201):**
```json
{
  "id": 1,
  "marca": "Toyota",
  "modelo": "Corolla",
  "ano": 2023,
  "cor": "Prata",
  "preco": 85000.00,
  "status": "DISPONIVEL",
  "dataCadastro": "2025-01-18T10:30:00",
  "venda": null
}
```

### 🆕 **3. Listar Veículos (ENDPOINT UNIFICADO)**

#### 📊 Listar TODOS os veículos
```http
GET /api/veiculos
```

#### 🟢 Listar apenas DISPONÍVEIS
```http
GET /api/veiculos?status=DISPONIVEL
```

#### 🔴 Listar apenas VENDIDOS  
```http
GET /api/veiculos?status=VENDIDO
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "marca": "Toyota",
    "modelo": "Corolla",
    "ano": 2023,
    "cor": "Prata",
    "preco": 85000.00,
    "status": "DISPONIVEL",
    "dataCadastro": "2025-01-18T10:30:00",
    "venda": null
  }
]
```

### ⚠️ **Endpoints Depreciados (ainda funcionam)**
```http
GET /api/veiculos/disponiveis    # Use: GET /api/veiculos?status=DISPONIVEL
GET /api/veiculos/venda          # Use: GET /api/veiculos?status=DISPONIVEL  
GET /api/veiculos/vendidos       # Use: GET /api/veiculos?status=VENDIDO
```

### 💰 **4. Efetuar Venda**
```http
POST /api/veiculos/1/venda
Content-Type: application/json

{
  "cpfComprador": "123.456.789-00",
  "dataVenda": "2025-01-18T14:30:00"
}
```

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "marca": "Toyota",
  "modelo": "Corolla",
  "ano": 2023,
  "cor": "Prata",
  "preco": 85000.00,
  "status": "VENDIDO",
  "dataCadastro": "2025-01-18T10:30:00",
  "venda": {
    "id": 1,
    "cpfComprador": "123.456.789-00",
    "dataVenda": "2025-01-18T14:30:00",
    "codigoPagamento": "PAG20250118143001",
    "statusPagamento": "PENDENTE"
  }
}
```

### 🔄 **5. Confirmar Pagamento (Webhook)**
```http
POST /api/webhook/pagamento
Content-Type: application/json

{
  "codigoPagamento": "PAG20250118143001",
  "status": "CONFIRMADO"
}
```

**Resposta Esperada (200):**
```json
{
  "message": "Pagamento confirmado com sucesso",
  "codigoPagamento": "PAG20250118143001"
}
```

### ✏️ **6. Editar Veículo**
```http
PUT /api/veiculos/1
Content-Type: application/json

{
  "marca": "Toyota",
  "modelo": "Corolla Hybrid",
  "ano": 2023,
  "cor": "Branco Pérola",
  "preco": 92000.00
}
```

---

## 🧪 Sequência de Testes Completa

### 📝 **Cenário 1: Fluxo Básico de Venda**

```bash
# 1. Verificar se aplicação está funcionando
curl http://localhost:8080/actuator/health

# 2. Cadastrar um veículo
curl -X POST http://localhost:8080/api/veiculos \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Honda",
    "modelo": "Civic",
    "ano": 2024,
    "cor": "Azul",
    "preco": 95000.00
  }'

# 3. 🆕 Listar todos os veículos (novo endpoint)
curl http://localhost:8080/api/veiculos

# 4. 🆕 Listar apenas disponíveis
curl http://localhost:8080/api/veiculos?status=DISPONIVEL

# 5. Efetuar venda (substituir {id} pelo ID retornado)
curl -X POST http://localhost:8080/api/veiculos/{id}/venda \
  -H "Content-Type: application/json" \
  -d '{
    "cpfComprador": "987.654.321-00",
    "dataVenda": "2025-01-18T15:00:00"
  }'

# 6. 🆕 Listar apenas vendidos
curl http://localhost:8080/api/veiculos?status=VENDIDO

# 7. Confirmar pagamento (usar código retornado na venda)
curl -X POST http://localhost:8080/api/webhook/pagamento \
  -H "Content-Type: application/json" \
  -d '{
    "codigoPagamento": "PAG20250118150001",
    "status": "CONFIRMADO"
  }'
```

### 🔄 **Cenário 2: Comparação Endpoints (Antigo vs Novo)**

```bash
# Método ANTIGO (ainda funciona, mas depreciado)
curl http://localhost:8080/api/veiculos/disponiveis
curl http://localhost:8080/api/veiculos/vendidos

# Método NOVO (recomendado) ✅
curl http://localhost:8080/api/veiculos?status=DISPONIVEL
curl http://localhost:8080/api/veiculos?status=VENDIDO
curl http://localhost:8080/api/veiculos  # Todos
```

---

## 🎯 Scripts Automatizados

### PowerShell (Windows)
```powershell
# Executar teste completo
.\test-endpoints.ps1
```

### Bash (Linux/Mac)
```bash
# Executar teste completo  
chmod +x test-endpoints.sh
./test-endpoints.sh
```

---

## 🔧 Códigos de Status HTTP

| Status | Significado | Quando Ocorre |
|--------|-------------|---------------|
| 200 | ✅ OK | Operação realizada com sucesso |
| 201 | ✅ Created | Veículo cadastrado com sucesso |
| 400 | ❌ Bad Request | Dados inválidos ou veículo já vendido |
| 404 | ❌ Not Found | Veículo ou pagamento não encontrado |
| 500 | ❌ Internal Error | Erro interno do servidor |

---

## 🐛 Troubleshooting

### Problema: "Connection refused"
**Solução**: Verificar se o port-forward está ativo:
```bash
kubectl port-forward service/revenda-app-service 8080:80 -n revenda-veiculos
```

### Problema: "Veículo não encontrado"
**Solução**: Verificar se o ID existe usando:
```bash
curl http://localhost:8080/api/veiculos
```

### Problema: "Database connection failed"
**Solução**: Verificar se o MySQL está rodando:
```bash
kubectl get pods -n revenda-veiculos
kubectl logs mysql-deployment-xxx -n revenda-veiculos
```

---

## 📊 Monitoramento

### Verificar Status dos Pods
```bash
kubectl get pods -n revenda-veiculos
kubectl get services -n revenda-veiculos
```

### Ver Logs da Aplicação
```bash
kubectl logs -f -l app=revenda-app -n revenda-veiculos
```

### Verificar HPA (Auto Scaling)
```bash
kubectl get hpa -n revenda-veiculos
```

---

## 🎉 Próximos Passos

1. ✅ Teste todos os endpoints via Swagger UI
2. ✅ Execute os scripts automatizados  
3. ✅ Migre para o novo endpoint unificado `GET /api/veiculos?status=X`
4. ✅ Monitore performance via `/actuator/metrics`
5. ✅ Configure alertas baseados nos health checks

**🚀 API totalmente funcional e documentada!**
