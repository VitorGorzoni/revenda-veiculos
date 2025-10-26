@echo off
REM Script para gerenciar containers Docker da aplicação Revenda de Veículos
REM Uso: docker-manager.bat [comando]

setlocal EnableDelayedExpansion

REM Navegar para a raiz do projeto
cd /d "%~dp0\.."

if "%1"=="help" goto :help
if "%1"=="build" goto :build
if "%1"=="start" goto :start
if "%1"=="stop" goto :stop
if "%1"=="restart" goto :restart
if "%1"=="logs" goto :logs
if "%1"=="status" goto :status
if "%1"=="clean" goto :clean
if "%1"=="dev" goto :dev
if "%1"=="prod" goto :prod
if "%1"=="" goto :help

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

:help
echo.
echo 🐳 Gerenciador Docker - Revenda de Veículos
echo ==========================================
echo.
echo Comandos disponíveis:
echo   build     - Fazer build das imagens Docker
echo   start     - Iniciar containers (produção)
echo   stop      - Parar containers
echo   restart   - Reiniciar containers
echo   logs      - Visualizar logs dos containers
echo   status    - Status dos containers
echo   clean     - Limpar containers e volumes
echo   dev       - Iniciar ambiente de desenvolvimento
echo   prod      - Iniciar ambiente de produção
echo   help      - Mostrar esta ajuda
echo.
goto :eof

:build
echo 🔨 Fazendo build das imagens Docker...
call :maven_build
if errorlevel 1 exit /b 1
echo 🛑 Parando containers antigos...
docker-compose down
echo 🗑️ Removendo imagens antigas...
docker-compose rm -f
docker rmi revenda-veiculos-app 2>nul
echo 🏗️ Construindo novas imagens...
docker-compose build --no-cache
if errorlevel 1 (
    echo ❌ Erro no build
    exit /b 1
)
echo ✅ Build concluído!
echo 💡 Use 'scripts\docker-manager.bat start' para iniciar os containers
goto :eof

:start
echo 🚀 Iniciando containers (produção)...
echo 🔄 Reconstruindo imagem para garantir versão atualizada...
docker-compose down
docker-compose build
docker-compose up -d
if errorlevel 1 (
    echo ❌ Erro ao iniciar containers
    exit /b 1
)
echo ✅ Containers iniciados!
echo 📊 Status dos containers:
docker-compose ps
echo.
echo 🌐 Aplicação disponível em: http://localhost:8080
echo 📖 Documentação Swagger: http://localhost:8080/swagger-ui.html
goto :eof

:stop
echo 🛑 Parando containers...
docker-compose down
echo ✅ Containers parados!
goto :eof

:restart
echo 🔄 Reiniciando containers...
echo 🔄 Reconstruindo imagem antes de reiniciar...
docker-compose down
docker-compose build
docker-compose up -d
if errorlevel 1 (
    echo ❌ Erro ao reiniciar containers
    exit /b 1
)
echo ✅ Containers reiniciados!
echo 📊 Status:
docker-compose ps
goto :eof

:logs
echo 📋 Logs dos containers:
if "%2"=="" (
    docker-compose logs -f
) else (
    docker-compose logs -f %2
)
goto :eof

:status
echo 📊 Status dos containers:
docker-compose ps
echo.
echo 💾 Uso de recursos:
docker stats --no-stream
goto :eof

:clean
echo 🧹 Limpando containers e volumes...
set /p confirm="Tem certeza? Isso removerá TODOS os dados! (s/N): "
if /i "!confirm!"=="s" (
    docker-compose down -v --remove-orphans
    docker system prune -f
    echo ✅ Limpeza concluída!
) else (
    echo ❌ Operação cancelada.
)
goto :eof

:dev
echo 🚀 Iniciando ambiente de DESENVOLVIMENTO...
echo 🔄 Reconstruindo imagem de desenvolvimento...
docker-compose -f docker-compose.dev.yml down
docker-compose -f docker-compose.dev.yml build
docker-compose -f docker-compose.dev.yml up -d
if errorlevel 1 (
    echo ❌ Erro ao iniciar ambiente de desenvolvimento
    exit /b 1
)
echo ✅ Ambiente de desenvolvimento iniciado!
echo 🌐 Aplicação disponível em: http://localhost:8080
echo 🐛 Debug disponível na porta: 5005
echo 📖 Documentação Swagger: http://localhost:8080/swagger-ui.html
echo 📋 Logs:
docker-compose -f docker-compose.dev.yml logs -f
goto :eof

:prod
echo 🚀 Iniciando ambiente de PRODUÇÃO...
echo 🔄 Reconstruindo imagem de produção...
docker-compose down
docker-compose build
docker-compose up -d
if errorlevel 1 (
    echo ❌ Erro ao iniciar ambiente de produção
    exit /b 1
)
echo ✅ Ambiente de produção iniciado!
echo 🌐 Aplicação disponível em: http://localhost:8080
echo 📖 Documentação Swagger: http://localhost:8080/swagger-ui.html
echo 📊 Status:
docker-compose ps
goto :eof

