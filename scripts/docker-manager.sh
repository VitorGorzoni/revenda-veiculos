#!/bin/bash
# Script para gerenciar containers Docker da aplicação Revenda de Veículos
# Uso: ./docker-manager.sh [comando]

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Navegar para a raiz do projeto (pasta pai da pasta scripts)
cd "$(dirname "$0")/.." || exit

# Função para mostrar ajuda
show_help() {
    echo ""
    echo "🐳 Gerenciador Docker - Revenda de Veículos"
    echo "=========================================="
    echo ""
    echo "Comandos disponíveis:"
    echo "  build     - Fazer build das imagens Docker"
    echo "  start     - Iniciar containers (produção)"
    echo "  stop      - Parar containers"
    echo "  restart   - Reiniciar containers"
    echo "  logs      - Visualizar logs dos containers"
    echo "  status    - Status dos containers"
    echo "  clean     - Limpar containers e volumes"
    echo "  dev       - Iniciar ambiente de desenvolvimento"
    echo "  prod      - Iniciar ambiente de produção"
    echo "  help      - Mostrar esta ajuda"
    echo ""
}

# Função para build
do_build() {
    echo "🔨 Fazendo build das imagens Docker..."
    echo "🛑 Parando containers antigos..."
    docker-compose down

    echo "🗑️ Removendo imagens antigas..."
    docker-compose rm -f
    docker rmi revenda-veiculos-app 2>/dev/null || true

    echo "🏗️ Construindo novas imagens..."
    if docker-compose build --no-cache; then
        echo -e "${GREEN}✅ Build concluído!${NC}"
        echo "💡 Use './scripts/docker-manager.sh start' para iniciar os containers"
    else
        echo -e "${RED}❌ Erro no build${NC}"
        exit 1
    fi
}

# Função para start
do_start() {
    echo "🚀 Iniciando containers (produção)..."
    echo "🔄 Reconstruindo imagem para garantir versão atualizada..."
    docker-compose down
    docker-compose build

    if docker-compose up -d; then
        echo -e "${GREEN}✅ Containers iniciados!${NC}"
        echo "📊 Status dos containers:"
        docker-compose ps
        echo ""
        echo "🌐 Aplicação disponível em: http://localhost:8080"
        echo "📖 Documentação Swagger: http://localhost:8080/swagger-ui.html"
    else
        echo -e "${RED}❌ Erro ao iniciar containers${NC}"
        exit 1
    fi
}

# Função para stop
do_stop() {
    echo "🛑 Parando containers..."
    docker-compose down
    echo -e "${GREEN}✅ Containers parados!${NC}"
}

# Função para restart
do_restart() {
    echo "🔄 Reiniciando containers..."
    echo "🔄 Reconstruindo imagem antes de reiniciar..."
    docker-compose down
    docker-compose build

    if docker-compose up -d; then
        echo -e "${GREEN}✅ Containers reiniciados!${NC}"
        echo "📊 Status:"
        docker-compose ps
    else
        echo -e "${RED}❌ Erro ao reiniciar containers${NC}"
        exit 1
    fi
}

# Função para logs
do_logs() {
    echo "📋 Logs dos containers:"
    if [ -z "$2" ]; then
        docker-compose logs -f
    else
        docker-compose logs -f "$2"
    fi
}

# Função para status
do_status() {
    echo "📊 Status dos containers:"
    docker-compose ps
    echo ""
    echo "💾 Uso de recursos:"
    docker stats --no-stream
}

# Função para clean
do_clean() {
    echo "🧹 Limpando containers e volumes..."
    read -p "Tem certeza? Isso removerá TODOS os dados! (s/N): " confirm

    if [[ "$confirm" =~ ^[sS]$ ]]; then
        docker-compose down -v --remove-orphans
        docker system prune -f
        echo -e "${GREEN}✅ Limpeza concluída!${NC}"
    else
        echo -e "${YELLOW}❌ Operação cancelada.${NC}"
    fi
}

# Função para dev
do_dev() {
    echo "🚀 Iniciando ambiente de DESENVOLVIMENTO..."
    echo "🔄 Reconstruindo imagem de desenvolvimento..."
    docker-compose -f docker-compose.dev.yml down
    docker-compose -f docker-compose.dev.yml build

    if docker-compose -f docker-compose.dev.yml up -d; then
        echo -e "${GREEN}✅ Ambiente de desenvolvimento iniciado!${NC}"
        echo "🌐 Aplicação disponível em: http://localhost:8080"
        echo "🐛 Debug disponível na porta: 5005"
        echo "📖 Documentação Swagger: http://localhost:8080/swagger-ui.html"
        echo "📋 Logs:"
        docker-compose -f docker-compose.dev.yml logs -f
    else
        echo -e "${RED}❌ Erro ao iniciar ambiente de desenvolvimento${NC}"
        exit 1
    fi
}

# Função para prod
do_prod() {
    echo "🚀 Iniciando ambiente de PRODUÇÃO..."
    echo "🔄 Reconstruindo imagem de produção..."
    docker-compose down
    docker-compose build

    if docker-compose up -d; then
        echo -e "${GREEN}✅ Ambiente de produção iniciado!${NC}"
        echo "🌐 Aplicação disponível em: http://localhost:8080"
        echo "📖 Documentação Swagger: http://localhost:8080/swagger-ui.html"
        echo "📊 Status:"
        docker-compose ps
    else
        echo -e "${RED}❌ Erro ao iniciar ambiente de produção${NC}"
        exit 1
    fi
}

# Main - processar comando
case "${1:-help}" in
    help)
        show_help
        ;;
    build)
        do_build
        ;;
    start)
        do_start
        ;;
    stop)
        do_stop
        ;;
    restart)
        do_restart
        ;;
    logs)
        do_logs "$@"
        ;;
    status)
        do_status
        ;;
    clean)
        do_clean
        ;;
    dev)
        do_dev
        ;;
    prod)
        do_prod
        ;;
    *)
        echo -e "${RED}❌ Comando inválido: $1${NC}"
        show_help
        exit 1
        ;;
esac

