# 📁 Estrutura Kubernetes - Revenda de Veículos

## 🎯 Organização na Pasta k8s

Todos os arquivos estão na pasta `k8s/` organizados por prefixos para fácil identificação:

```
k8s/
├── namespace.yaml          # Namespace da aplicação
├── configmap.yaml          # Configurações da aplicação  
├── secret.yaml             # Credenciais e secrets
│
├── mysql-pvc.yaml          # MySQL: Volume persistente
├── mysql-service.yaml      # MySQL: Serviço interno
├── mysql-deployment.yaml   # MySQL: Deployment
│
├── app-deployment.yaml     # App: Deploy da aplicação
├── app-service.yaml        # App: Serviço interno
├── app-ingress.yaml        # App: Acesso externo
└── app-hpa.yaml           # App: Auto scaling
```

## 🚀 Como Usar

### Deploy Completo (Recomendado)

#### **Windows**
```bash
# Executar deploy completo automatizado
scripts\minikube-deploy.bat all
```

#### **Linux/Mac**
```bash
# Dar permissão de execução (primeira vez)
chmod +x scripts/minikube-deploy.sh

# Executar deploy completo automatizado
./scripts/minikube-deploy.sh all
```

### Deploy Manual Sequencial

```bash
# 1. Configurações base
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml

# 2. MySQL (ordem importante)
kubectl apply -f k8s/mysql-pvc.yaml
kubectl apply -f k8s/mysql-deployment.yaml
kubectl apply -f k8s/mysql-service.yaml

# 3. Aguardar MySQL estar pronto
kubectl wait --for=condition=ready pod -l app=mysql -n revenda-veiculos --timeout=300s

# 4. Aplicação
kubectl apply -f k8s/app-deployment.yaml
kubectl apply -f k8s/app-service.yaml
kubectl apply -f k8s/app-ingress.yaml
kubectl apply -f k8s/app-hpa.yaml
```

### Deploy Seletivo por Componente

```bash
# Apenas MySQL
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/mysql-pvc.yaml
kubectl apply -f k8s/mysql-deployment.yaml
kubectl apply -f k8s/mysql-service.yaml

# Apenas aplicação (assumindo MySQL rodando)
kubectl apply -f k8s/app-deployment.yaml
kubectl apply -f k8s/app-service.yaml
kubectl apply -f k8s/app-ingress.yaml
kubectl apply -f k8s/app-hpa.yaml

# Apenas auto scaling
kubectl apply -f k8s/app-hpa.yaml
```

## 📊 Monitoramento

### Status Geral

#### **Windows**
```bash
scripts\minikube-deploy.bat status
```

#### **Linux/Mac**
```bash
./scripts/minikube-deploy.sh status
```

### Status Manual

```bash
# Ver tudo no namespace
kubectl get all -n revenda-veiculos

# Configurações base
kubectl get namespace,configmap,secret -n revenda-veiculos

# MySQL
kubectl get pods,svc,pvc -l app=mysql -n revenda-veiculos

# Aplicação
kubectl get pods,svc,ingress,hpa -l app=revenda-app -n revenda-veiculos

# Logs da aplicação
kubectl logs -f -n revenda-veiculos -l app=revenda-app

# Logs do MySQL
kubectl logs -f -n revenda-veiculos -l app=mysql

# Eventos (útil para troubleshooting)
kubectl get events -n revenda-veiculos --sort-by='.lastTimestamp'
```

## 🌐 Acessar a Aplicação

### Obter URL de Acesso

#### **Windows**
```bash
scripts\minikube-deploy.bat access
```

#### **Linux/Mac**
```bash
./scripts/minikube-deploy.sh access
```

### Acesso Manual

#### **Opção 1: Via NodePort (Mais Simples)**
```bash
# Obter URL direta
minikube service revenda-app-service -n revenda-veiculos --url

# Acessar a URL retornada
# Exemplo: http://192.168.49.2:30220
```

#### **Opção 2: Via Ingress**
```bash
# 1. Obter IP do Minikube
minikube ip

# 2. Adicionar no arquivo hosts
# Windows: C:\Windows\System32\drivers\etc\hosts
# Linux/Mac: /etc/hosts
# Adicione: <IP_DO_MINIKUBE> revenda-veiculos.local

# 3. Acessar
# http://revenda-veiculos.local
# http://revenda-veiculos.local/swagger-ui.html
```

#### **Opção 3: Via Tunnel**
```bash
# Execute em um terminal separado (deixe rodando)
minikube tunnel

# Acesse: http://revenda-veiculos.local
```

## 🔧 Comandos Úteis

### Escalar a Aplicação
```bash
# Aumentar para 5 réplicas
kubectl scale deployment revenda-app-deployment -n revenda-veiculos --replicas=5

# Voltar para 2 réplicas
kubectl scale deployment revenda-app-deployment -n revenda-veiculos --replicas=2
```

### Atualizar Imagem
```bash
# Fazer rebuild da imagem no Minikube
eval $(minikube docker-env)  # Linux/Mac
# OU
minikube docker-env --shell powershell | Invoke-Expression  # Windows

docker build -t revenda-veiculos:latest .

# Reiniciar pods para usar nova imagem
kubectl rollout restart deployment revenda-app-deployment -n revenda-veiculos
```

### Editar Recursos
```bash
# Editar deployment
kubectl edit deployment revenda-app-deployment -n revenda-veiculos

# Editar service
kubectl edit service revenda-app-service -n revenda-veiculos

# Editar HPA
kubectl edit hpa revenda-app-hpa -n revenda-veiculos
```

### Acessar Shell de um Pod
```bash
# Listar pods
kubectl get pods -n revenda-veiculos

# Acessar shell da aplicação
kubectl exec -it <nome-do-pod> -n revenda-veiculos -- /bin/sh

# Acessar MySQL
kubectl exec -it <nome-do-pod-mysql> -n revenda-veiculos -- mysql -u app -papp123 revenda_veiculos
```

## 🧹 Limpeza

### Limpeza Completa

#### **Windows**
```bash
scripts\minikube-deploy.bat cleanup
```

#### **Linux/Mac**
```bash
./scripts/minikube-deploy.sh cleanup
```

### Limpeza Manual por Componentes

```bash
# Remover aplicação (mantém MySQL)
kubectl delete -f k8s/app-deployment.yaml
kubectl delete -f k8s/app-service.yaml
kubectl delete -f k8s/app-ingress.yaml
kubectl delete -f k8s/app-hpa.yaml

# Remover MySQL (⚠️ cuidado: apaga dados!)
kubectl delete -f k8s/mysql-deployment.yaml
kubectl delete -f k8s/mysql-service.yaml
kubectl delete -f k8s/mysql-pvc.yaml

# Remover configurações base
kubectl delete -f k8s/configmap.yaml
kubectl delete -f k8s/secret.yaml

# Remover namespace (remove tudo)
kubectl delete namespace revenda-veiculos
```

## ✨ Vantagens da Estrutura Atual

- 🎯 **Tudo em um local**: Todos os arquivos na pasta k8s
- 🔍 **Fácil identificação**: Prefixos mysql-* e app-*  
- 📋 **Ordem clara**: Base → MySQL → Aplicação
- 🚀 **Deploy flexível**: Scripts automatizados ou manual
- 🔧 **Manutenção simples**: Arquivos relacionados agrupados por nome
- 🔄 **Auto-scaling**: HPA configurado para 2-10 réplicas
- 💾 **Persistência**: Volume persistente para MySQL

## 📝 Descrição dos Componentes

### Base (3 arquivos)

#### `namespace.yaml`
- Cria o namespace `revenda-veiculos`
- Isola recursos da aplicação

#### `configmap.yaml`
- Configurações não sensíveis da aplicação
- Host, porta e nome do banco de dados

#### `secret.yaml`
- Credenciais do banco de dados em base64
- Username: `app`
- Password: `app123`

### MySQL (3 arquivos)

#### `mysql-pvc.yaml`
- PersistentVolumeClaim de 2GB
- Armazenamento persistente para dados do MySQL
- Classe de armazenamento: `standard`

#### `mysql-deployment.yaml`
- 1 réplica do MySQL 8.0
- Variáveis de ambiente configuradas
- Health checks (liveness e readiness)
- Volume montado em `/var/lib/mysql`
- Script de inicialização em `/docker-entrypoint-initdb.d/`

#### `mysql-service.yaml`
- Service tipo ClusterIP
- Porta 3306
- Acessível apenas dentro do cluster

### Aplicação (4 arquivos)

#### `app-deployment.yaml`
- 2 réplicas iniciais da aplicação
- Imagem: `revenda-veiculos:latest`
- ImagePullPolicy: `IfNotPresent` (usa imagem local do Minikube)
- Variáveis de ambiente do banco via ConfigMap e Secret
- Recursos:
  - Requests: 512Mi RAM, 250m CPU
  - Limits: 1Gi RAM, 500m CPU
- Health checks:
  - Liveness: `/actuator/health` (60s initial delay)
  - Readiness: `/actuator/health` (30s initial delay)

#### `app-service.yaml`
- Service tipo LoadBalancer
- Porta 80 → 8080 (container)
- NodePort alocado automaticamente (~30000-32767)

#### `app-ingress.yaml`
- Host: `revenda-veiculos.local`
- Roteamento para o service na porta 80
- Requer ingress controller habilitado

#### `app-hpa.yaml`
- Horizontal Pod Autoscaler
- Min: 2 réplicas
- Max: 10 réplicas
- Métricas:
  - CPU: 70% de utilização
  - Memória: 80% de utilização
- Requer metrics-server habilitado

## 🔍 Troubleshooting

### Pods não iniciam

```bash
# Ver detalhes do pod
kubectl describe pod <nome-do-pod> -n revenda-veiculos

# Ver eventos
kubectl get events -n revenda-veiculos

# Ver logs
kubectl logs <nome-do-pod> -n revenda-veiculos
```

### Aplicação não conecta ao MySQL

```bash
# Verificar se MySQL está rodando
kubectl get pods -l app=mysql -n revenda-veiculos

# Testar conectividade
kubectl run -it --rm debug --image=mysql:8.0 --restart=Never -n revenda-veiculos -- mysql -h mysql-service -u app -papp123 -e "SELECT 1"

# Verificar variáveis de ambiente na aplicação
kubectl exec <nome-do-pod-app> -n revenda-veiculos -- env | grep DB
```

### Ingress não funciona

```bash
# Verificar se ingress controller está rodando
kubectl get pods -n ingress-nginx

# Habilitar ingress (se necessário)
minikube addons enable ingress

# Verificar ingress
kubectl describe ingress revenda-app-ingress -n revenda-veiculos
```

### HPA não escala

```bash
# Verificar se metrics-server está rodando
kubectl get deployment metrics-server -n kube-system

# Habilitar metrics-server (se necessário)
minikube addons enable metrics-server

# Ver métricas dos pods
kubectl top pods -n revenda-veiculos

# Ver status do HPA
kubectl describe hpa revenda-app-hpa -n revenda-veiculos
```

## 📚 Referências

- [Kubernetes Documentation](https://kubernetes.io/docs/home/)
- [Minikube Documentation](https://minikube.sigs.k8s.io/docs/)
- [kubectl Cheat Sheet](https://kubernetes.io/docs/reference/kubectl/cheatsheet/)

---

**💡 Dica**: Use os scripts automatizados (`minikube-deploy.bat` ou `minikube-deploy.sh`) para simplificar o gerenciamento do cluster!
