@echo off
REM Script para setup e deploy completo no Minikube - Windows
REM Revenda de Veículos - Minikube Setup

REM Navegar para a raiz do projeto (pasta pai da pasta scripts)
cd /d "%~dp0\.."

echo 🚀 Setup Completo da Aplicação Revenda de Veículos no Minikube (Windows)
echo =======================================================================

if "%1"=="help" goto :help
if "%1"=="setup" goto :setup
if "%1"=="build" goto :build
if "%1"=="deploy" goto :deploy
if "%1"=="all" goto :all
if "%1"=="status" goto :status
if "%1"=="access" goto :access
if "%1"=="cleanup" goto :cleanup
if "%1"=="" goto :all

goto :help

:maven_build
echo.
echo ☕ Compilando aplicação Java com Maven...
echo ==========================================
call mvnw.cmd clean package -DskipTests
if errorlevel 1 (
    echo ❌ Erro no build do Maven
    exit /b 1
)
echo ✅ Build Maven concluído!
echo.
exit /b 0

:setup
echo ⚙️ Configurando Minikube...

REM Verificar se Minikube está instalado
where minikube >nul 2>&1
if errorlevel 1 (
    echo ❌ Erro: Minikube não está instalado!
    echo.
    echo 📝 Para instalar o Minikube:
    echo    1. Execute como Administrador: scripts\instalar-minikube.bat
    echo    2. OU instale manualmente via Chocolatey: choco install minikube kubernetes-cli -y
    echo.
    exit /b 1
)

REM Verificar se Minikube está rodando
echo 🔍 Verificando status do Minikube...
minikube status >nul 2>&1
if errorlevel 1 (
    echo 🚀 Minikube não está rodando, iniciando...
    goto :start_minikube
)

echo ✅ Minikube reporta que está rodando

REM Garantir que o contexto kubectl está correto
echo 🔧 Configurando contexto kubectl...
kubectl config use-context minikube >nul 2>&1

REM Verificar conectividade com o cluster
echo 🔍 Verificando conectividade com o cluster...
kubectl cluster-info >nul 2>&1
if errorlevel 1 (
    echo ⚠️ Cluster não está respondendo, mesmo dizendo que está rodando!
    echo 🔄 Forçando restart do Minikube...
    goto :start_minikube
)

echo ✅ Cluster está respondendo normalmente!
goto :configure_addons

:start_minikube
echo 🧹 Parando Minikube anterior (se existir^)...
minikube stop >nul 2>&1
minikube delete >nul 2>&1

REM Limpar cache corrompido de imagens
echo 🗑️ Removendo cache de imagens (pode estar corrompido^)...
if exist "%USERPROFILE%\.minikube\cache\kic" (
    rmdir /s /q "%USERPROFILE%\.minikube\cache\kic" 2>nul
)
if exist "%USERPROFILE%\.minikube\cache\images" (
    rmdir /s /q "%USERPROFILE%\.minikube\cache\images" 2>nul
)

echo 🚀 Iniciando Minikube (primeira vez pode demorar 5-10 minutos^)...
echo ⏳ Aguarde o download e carregamento das imagens base...
echo.

REM Iniciar sem flags problemáticas, deixando fazer download limpo
minikube start --driver=docker --memory=4096 --cpus=2

if errorlevel 1 (
    echo.
    echo ❌ Erro ao iniciar Minikube
    echo.
    echo 💡 Tentativa de solução alternativa:
    echo.
    echo    1. Verifique se o Docker Desktop está rodando
    echo.
    echo    2. Tentar novamente com menos recursos:
    echo       minikube start --driver=docker --memory=2048 --cpus=1
    echo.
    echo    3. Verificar logs detalhados:
    echo       minikube logs
    echo.
    exit /b 1
)

echo.
echo ✅ Minikube iniciado com sucesso!

REM Garantir que o contexto kubectl está correto
echo 🔧 Configurando contexto kubectl...
kubectl config use-context minikube
if errorlevel 1 (
    echo ❌ Erro ao configurar contexto kubectl
    echo 💡 Tente: minikube update-context
    exit /b 1
)

REM Verificar conectividade com o cluster (aguardar até 30 segundos)
echo 🔍 Verificando conectividade com o cluster...
kubectl cluster-info >nul 2>&1
if errorlevel 1 (
    echo ⏳ Aguardando cluster ficar pronto...
    timeout /t 15 /nobreak >nul
    kubectl cluster-info >nul 2>&1
    if errorlevel 1 (
        echo ❌ Cluster não está respondendo após inicialização
        echo 💡 Tente: minikube delete e depois minikube start
        exit /b 1
    )
)

:configure_addons
echo 🔧 Habilitando addons do Minikube...
minikube addons enable ingress
minikube addons enable metrics-server

echo ✅ Minikube configurado com sucesso!
echo.
echo 📊 Status do cluster:
kubectl cluster-info
echo.
goto :eof

:build
echo 🐳 Fazendo build da aplicação e imagem Docker no Minikube...

REM Compilar a aplicação Java primeiro
call :maven_build
if errorlevel 1 exit /b 1

REM Verificar se Dockerfile existe
if not exist "Dockerfile" (
    echo ❌ Erro: Dockerfile não encontrado!
    echo 📍 Diretório atual: %CD%
    exit /b 1
)

echo 🔧 Configurando Docker env para Minikube...
@echo on
for /f "tokens=*" %%i in ('minikube docker-env --shell cmd') do %%i
@echo off

echo 🐳 Fazendo build da imagem Docker...
echo 📍 Diretório: %CD%
docker build -t revenda-veiculos:latest .
if errorlevel 1 (
    echo ❌ Erro no build da imagem Docker
    exit /b 1
)
echo ✅ Build da imagem Docker concluído!
goto :eof

:deploy
echo 🚀 Fazendo deploy da aplicação no Kubernetes...

REM Verificar se a pasta k8s existe
if not exist "k8s" (
    echo ❌ Erro: Pasta k8s/ não encontrada!
    echo 📍 Diretório atual: %CD%
    exit /b 1
)

echo 📦 Aplicando configurações base...
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml
if errorlevel 1 (
    echo ❌ Erro ao criar configurações base
    exit /b 1
)

echo 💾 Aplicando PVC do MySQL...
kubectl apply -f k8s/mysql-pvc.yaml
if errorlevel 1 (
    echo ❌ Erro ao criar PVC do MySQL
    exit /b 1
)

echo 🗄️ Fazendo deploy do MySQL...
kubectl apply -f k8s/mysql-deployment.yaml
kubectl apply -f k8s/mysql-service.yaml
if errorlevel 1 (
    echo ❌ Erro no deploy do MySQL
    exit /b 1
)

echo ⏳ Aguardando MySQL estar pronto...
kubectl wait --for=condition=ready pod -l app=mysql -n revenda-veiculos --timeout=300s

echo 🚀 Fazendo deploy da aplicação...
kubectl apply -f k8s/app-deployment.yaml
kubectl apply -f k8s/app-service.yaml
kubectl apply -f k8s/app-ingress.yaml
kubectl apply -f k8s/app-hpa.yaml
if errorlevel 1 (
    echo ❌ Erro no deploy da aplicação
    exit /b 1
)

echo ✅ Deploy concluído com sucesso!
goto :eof

:all
echo 🚀 Executando setup completo...
echo 📍 Diretório de trabalho: %CD%
echo.
call :setup
if errorlevel 1 exit /b 1
call :build
if errorlevel 1 exit /b 1
call :deploy
if errorlevel 1 exit /b 1
echo.
echo 🎉 Setup completo finalizado!
echo.
call :access
goto :eof

:status
echo 📊 Status da aplicação...
echo.
echo Pods:
kubectl get pods -n revenda-veiculos
echo.
echo Services:
kubectl get services -n revenda-veiculos
echo.
echo Ingress:
kubectl get ingress -n revenda-veiculos
echo.
echo HPA:
kubectl get hpa -n revenda-veiculos
goto :eof

:access
echo 🌐 Instruções para acessar a aplicação:
echo.
echo 📍 Opção 1: Via NodePort (Mais Simples)
echo ==========================================
echo.
minikube service revenda-app-service -n revenda-veiculos --url
echo.
echo Acesse a URL acima para testar a aplicação
echo Swagger UI: [URL_ACIMA]/swagger-ui.html
echo.
echo 📍 Opção 2: Via Ingress
echo ==========================================
echo.
echo 1. Obter IP do Minikube:
minikube ip
echo.
echo 2. Adicionar entrada no arquivo hosts (como Administrador):
echo    C:\Windows\System32\drivers\etc\hosts
echo    [IP_DO_MINIKUBE] revenda-veiculos.local
echo.
echo 3. Acessar:
echo    http://revenda-veiculos.local
echo    http://revenda-veiculos.local/swagger-ui.html
echo.
echo 📍 Opção 3: Via Tunnel do Minikube
echo ==========================================
echo    minikube tunnel
echo    (Execute em um terminal separado e mantenha rodando)
echo.
goto :eof

:cleanup
echo 🧹 Limpando recursos...
kubectl delete namespace revenda-veiculos
echo ✅ Recursos removidos!
goto :eof

:help
echo Uso: %0 [OPÇÃO]
echo.
echo Opções:
echo   setup     - Configurar Minikube (start, addons, etc.)
echo   build     - Build da imagem Docker no Minikube
echo   deploy    - Deploy da aplicação no Minikube
echo   all       - Executar setup + build + deploy (padrão)
echo   status    - Verificar status da aplicação
echo   access    - Instruções para acessar a aplicação
echo   cleanup   - Limpar recursos do Minikube
echo   help      - Mostrar esta ajuda
echo.
goto :eof
