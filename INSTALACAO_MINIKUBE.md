# 🚀 Guia de Instalação do Minikube para Windows

## 📋 Pré-requisitos

Antes de executar o script de deploy no Minikube, você precisa ter as seguintes ferramentas instaladas:

### 1. **Docker Desktop**
- ✅ Você já tem instalado (verificado anteriormente)

### 2. **kubectl** (CLI do Kubernetes)

**Instalação via Chocolatey:**
```powershell
choco install kubernetes-cli
```

**Instalação Manual:**
1. Baixe o kubectl: https://kubernetes.io/docs/tasks/tools/install-kubectl-windows/
2. Adicione ao PATH do sistema

**Verificar instalação:**
```bash
kubectl version --client
```

### 3. **Minikube**

**Instalação via Chocolatey (Recomendado):**
```powershell
choco install minikube
```

**Instalação via Windows Installer:**
1. Baixe o instalador: https://minikube.sigs.k8s.io/docs/start/
2. Execute o arquivo `.exe` baixado
3. Reinicie o terminal após a instalação

**Instalação Manual:**
```powershell
# Baixar o executável
New-Item -Path 'c:\minikube' -Type Directory -Force
Invoke-WebRequest -OutFile 'c:\minikube\minikube.exe' -Uri 'https://github.com/kubernetes/minikube/releases/latest/download/minikube-windows-amd64.exe' -UseBasicParsing

# Adicionar ao PATH
$oldPath = [Environment]::GetEnvironmentVariable('Path', [EnvironmentVariableTarget]::Machine)
if ($oldPath.Split(';') -inotcontains 'C:\minikube'){
  [Environment]::SetEnvironmentVariable('Path', $('{0};C:\minikube' -f $oldPath), [EnvironmentVariableTarget]::Machine)
}
```

**Verificar instalação:**
```bash
minikube version
```

## 🚀 Iniciar o Minikube

Após instalar o Minikube e kubectl, execute:

```bash
# Iniciar o Minikube com Docker como driver
minikube start --driver=docker --memory=4096 --cpus=2

# Verificar status
minikube status

# Habilitar addons necessários
minikube addons enable ingress
minikube addons enable metrics-server
```

## 📦 Deploy da Aplicação

Após configurar o Minikube, execute:

```bash
# Da raiz do projeto
scripts\minikube-deploy.bat all
```

Ou execute passo a passo:

```bash
# 1. Setup do Minikube
scripts\minikube-deploy.bat setup

# 2. Build da imagem Docker
scripts\minikube-deploy.bat build

# 3. Deploy no Kubernetes
scripts\minikube-deploy.bat deploy

# 4. Verificar status
scripts\minikube-deploy.bat status

# 5. Ver instruções de acesso
scripts\minikube-deploy.bat access
```

## 🌐 Acessar a Aplicação

### Opção 1: Via Ingress (Recomendado)

1. Obter o IP do Minikube:
```bash
minikube ip
```

2. Adicionar no arquivo `C:\Windows\System32\drivers\etc\hosts` (como Administrador):
```
<IP_DO_MINIKUBE> revenda-veiculos.local
```

3. Acessar:
- **Aplicação**: http://revenda-veiculos.local
- **Swagger**: http://revenda-veiculos.local/swagger-ui.html

### Opção 2: Via Tunnel do Minikube

```bash
# Em um terminal separado (deixe rodando)
minikube tunnel
```

Depois acesse: http://revenda-veiculos.local

### Opção 3: Via Port Forward (Mais simples para testes)

```bash
# Obter o nome do pod
kubectl get pods -n revenda-veiculos

# Fazer port-forward
kubectl port-forward -n revenda-veiculos pod/<nome-do-pod> 8080:8080
```

Depois acesse: http://localhost:8080

### Opção 4: Via NodePort

```bash
# Obter a URL do serviço
minikube service revenda-app-service -n revenda-veiculos --url
```

Acesse a URL retornada.

## 🔍 Comandos Úteis

```bash
# Ver logs da aplicação
kubectl logs -f -n revenda-veiculos -l app=revenda-app

# Ver logs do MySQL
kubectl logs -f -n revenda-veiculos -l app=mysql

# Listar todos os recursos
kubectl get all -n revenda-veiculos

# Descrever um pod
kubectl describe pod <nome-do-pod> -n revenda-veiculos

# Entrar em um pod
kubectl exec -it <nome-do-pod> -n revenda-veiculos -- /bin/bash

# Ver eventos
kubectl get events -n revenda-veiculos --sort-by='.lastTimestamp'
```

## 🧹 Limpeza

```bash
# Remover a aplicação
scripts\minikube-deploy.bat cleanup

# Parar o Minikube
minikube stop

# Deletar o cluster Minikube
minikube delete
```

## ⚠️ Troubleshooting

### Problema: Minikube não inicia

**Solução:**
```bash
# Verificar se o Docker está rodando
docker ps

# Deletar e recriar o cluster
minikube delete
minikube start --driver=docker --memory=4096 --cpus=2
```

### Problema: Pods não iniciam

**Solução:**
```bash
# Ver logs do pod
kubectl logs <nome-do-pod> -n revenda-veiculos

# Ver eventos
kubectl get events -n revenda-veiculos
```

### Problema: Não consigo acessar via Ingress

**Solução:**
```bash
# Verificar se o ingress controller está rodando
kubectl get pods -n ingress-nginx

# Usar tunnel do Minikube
minikube tunnel
```

## 📚 Referências

- [Documentação do Minikube](https://minikube.sigs.k8s.io/docs/)
- [Documentação do Kubernetes](https://kubernetes.io/docs/home/)
- [Kubectl Cheat Sheet](https://kubernetes.io/docs/reference/kubectl/cheatsheet/)

## 🎯 Checklist de Instalação

- [ ] Docker Desktop instalado e rodando
- [ ] kubectl instalado (`kubectl version --client`)
- [ ] Minikube instalado (`minikube version`)
- [ ] Minikube iniciado (`minikube start --driver=docker`)
- [ ] Addons habilitados (`minikube addons enable ingress`)
- [ ] Deploy executado (`scripts\minikube-deploy.bat all`)
- [ ] Aplicação acessível

---

## 🎉 Teste Rápido (Alternativa ao Minikube)

Se você quer testar a aplicação rapidamente sem instalar o Minikube, use o Docker Compose:

```bash
# Da raiz do projeto
scripts\docker-manager.bat start

# Acessar
# http://localhost:8080
# http://localhost:8080/swagger-ui.html
```

Isso é muito mais simples para desenvolvimento e testes locais!

