#!/bin/bash

# Script para setup e deploy completo no Minikube
# Revenda de Veículos - Minikube Setup

echo "🚀 Setup Completo da Aplicação Revenda de Veículos no Minikube"
echo "============================================================="

# Função para verificar se o comando foi executado com sucesso
check_command() {
    if [ $? -eq 0 ]; then
        echo "✅ $1"
    else
        echo "❌ Erro: $1"
        exit 1
    fi
}

# Função para mostrar ajuda
show_help() {
    echo "Uso: $0 [OPÇÃO]"
    echo ""
    echo "Opções:"
    echo "  setup     - Configurar Minikube (start, addons, etc.)"
    echo "  build     - Build da imagem Docker no Minikube"
    echo "  deploy    - Deploy da aplicação no Minikube"
    echo "  all       - Executar setup + build + deploy"
    echo "  status    - Verificar status da aplicação"
    echo "  access    - Instruções para acessar a aplicação"
    echo "  cleanup   - Limpar recursos do Minikube"
    echo "  help      - Mostrar esta ajuda"
    echo ""
}

# Função para verificar pré-requisitos
check_prerequisites() {
    echo "🔍 Verificando pré-requisitos..."

    # Verificar se minikube está instalado
    if ! command -v minikube &> /dev/null; then
        echo "❌ Minikube não está instalado!"
        echo "💡 Instale o Minikube: https://minikube.sigs.k8s.io/docs/start/"
        exit 1
    fi

    # Verificar se kubectl está instalado
    if ! command -v kubectl &> /dev/null; then
        echo "❌ kubectl não está instalado!"
        echo "💡 Instale o kubectl: https://kubernetes.io/docs/tasks/tools/"
        exit 1
    fi

    # Verificar se docker está instalado
    if ! command -v docker &> /dev/null; then
        echo "❌ Docker não está instalado!"
        echo "💡 Instale o Docker: https://docs.docker.com/get-docker/"
        exit 1
    fi

    echo "✅ Todos os pré-requisitos estão instalados"
}

# Função para configurar Minikube
setup_minikube() {
    echo "⚙️ Configurando Minikube..."

    # Verificar se Minikube está rodando
    if ! minikube status | grep -q "Running"; then
        echo "🚀 Iniciando Minikube..."
        minikube start --driver=docker --memory=4096 --cpus=2
        check_command "Minikube iniciado"
    else
        echo "✅ Minikube já está rodando"
    fi

    # Habilitar addons necessários
    echo "🔧 Habilitando addons do Minikube..."
    minikube addons enable ingress
    check_command "Ingress habilitado"

    minikube addons enable metrics-server
    check_command "Metrics Server habilitado"

    minikube addons enable dashboard
    check_command "Dashboard habilitado"

    # Configurar contexto kubectl para Minikube
    kubectl config use-context minikube
    check_command "Contexto kubectl configurado para Minikube"

    echo "✅ Minikube configurado com sucesso!"
}

# Função para build da imagem Docker
build_image() {
    echo "🐳 Fazendo build da imagem Docker no Minikube..."

    # Configurar Docker env para usar o daemon do Minikube
    echo "🔧 Configurando Docker env para Minikube..."
    eval $(minikube docker-env)
    check_command "Docker env configurado"

    # Build da imagem
    echo "📦 Fazendo build da imagem revenda-veiculos..."
    docker build -t revenda-veiculos:latest .
    check_command "Imagem Docker criada"

    # Verificar se a imagem foi criada
    docker images | grep revenda-veiculos
    check_command "Imagem verificada"

    echo "✅ Build da imagem concluído!"
}

# Função para deploy da aplicação
deploy_app() {
    echo "🚢 Fazendo deploy da aplicação no Minikube..."

    # Aplicar manifestos na ordem correta
    echo "📦 Aplicando Namespace..."
    kubectl apply -f k8s/namespace.yaml
    check_command "Namespace aplicado"

    echo "📋 Aplicando ConfigMap..."
    kubectl apply -f k8s/configmap.yaml
    check_command "ConfigMap aplicado"

    echo "🔐 Aplicando Secret..."
    kubectl apply -f k8s/secret.yaml
    check_command "Secret aplicado"

    echo "💾 Aplicando PVC do MySQL..."
    kubectl apply -f k8s/mysql-pvc.yaml
    check_command "PVC aplicado"

    echo "🗄️ Aplicando Deployment do MySQL..."
    kubectl apply -f k8s/mysql-deployment.yaml
    check_command "MySQL Deployment aplicado"

    echo "🔗 Aplicando Service do MySQL..."
    kubectl apply -f k8s/mysql-service.yaml
    check_command "MySQL Service aplicado"

    echo "⏳ Aguardando MySQL ficar pronto..."
    kubectl wait --for=condition=ready pod -l app=mysql -n revenda-veiculos --timeout=300s
    check_command "MySQL está pronto"

    echo "🚀 Aplicando Deployment da aplicação..."
    kubectl apply -f k8s/app-deployment.yaml
    check_command "App Deployment aplicado"

    echo "🔗 Aplicando Service da aplicação..."
    kubectl apply -f k8s/app-service.yaml
    check_command "App Service aplicado"

    echo "📈 Aplicando HPA..."
    kubectl apply -f k8s/app-hpa.yaml
    check_command "HPA aplicado"

    echo "🌐 Aplicando Ingress..."
    kubectl apply -f k8s/app-ingress.yaml
    check_command "Ingress aplicado"

    echo "⏳ Aguardando aplicação ficar pronta..."
    kubectl wait --for=condition=ready pod -l app=revenda-app -n revenda-veiculos --timeout=300s
    check_command "Aplicação está pronta"

    echo "✅ Deploy concluído com sucesso!"
}

# Função para verificar status
check_status() {
    echo "📊 Status da aplicação no Minikube:"
    echo "=================================="

    echo ""
    echo "🔍 Namespace revenda-veiculos:"
    kubectl get all -n revenda-veiculos

    echo ""
    echo "📈 HPA Status:"
    kubectl get hpa -n revenda-veiculos

    echo ""
    echo "🌐 Ingress Status:"
    kubectl get ingress -n revenda-veiculos

    echo ""
    echo "💾 Volumes:"
    kubectl get pv,pvc -n revenda-veiculos
}

# Função para mostrar como acessar a aplicação
show_access_info() {
    echo "🌐 Como Acessar a Aplicação no Minikube:"
    echo "======================================="

    # Obter IP do Minikube
    MINIKUBE_IP=$(minikube ip)

    echo ""
    echo "📍 IP do Minikube: $MINIKUBE_IP"
    echo ""

    echo "🔗 Opções de Acesso:"
    echo ""

    echo "1️⃣ Via Port Forward (Mais Simples):"
    echo "   kubectl port-forward svc/revenda-app-service 8080:80 -n revenda-veiculos"
    echo "   Depois acesse: http://localhost:8080"
    echo ""

    echo "2️⃣ Via Ingress (Mais Realista):"
    echo "   Adicione no /etc/hosts (Linux/Mac) ou C:\\Windows\\System32\\drivers\\etc\\hosts (Windows):"
    echo "   $MINIKUBE_IP revenda-veiculos.local"
    echo "   $MINIKUBE_IP api.revenda-veiculos.local"
    echo ""
    echo "   Depois acesse:"
    echo "   - App: http://revenda-veiculos.local"
    echo "   - API: http://api.revenda-veiculos.local/api"
    echo "   - Swagger: http://api.revenda-veiculos.local/swagger-ui.html"
    echo ""

    echo "3️⃣ Via Minikube Service:"
    echo "   minikube service revenda-app-service -n revenda-veiculos"
    echo ""

    echo "4️⃣ Dashboard do Minikube:"
    echo "   minikube dashboard"
    echo ""

    echo "📋 Logs da aplicação:"
    echo "   kubectl logs -f deployment/revenda-app-deployment -n revenda-veiculos"
}

# Função para limpeza
cleanup_minikube() {
    echo "🧹 Limpando recursos do Minikube..."

    # Usar o script de cleanup existente
    if [ -f "k8s/cleanup.sh" ]; then
        chmod +x k8s/cleanup.sh
        ./k8s/cleanup.sh
    else
        # Cleanup manual
        kubectl delete namespace revenda-veiculos --ignore-not-found=true
    fi

    echo "✅ Limpeza concluída!"
}

# Main
case "$1" in
    setup)
        check_prerequisites
        setup_minikube
        ;;
    build)
        check_prerequisites
        build_image
        ;;
    deploy)
        check_prerequisites
        deploy_app
        ;;
    all)
        check_prerequisites
        setup_minikube
        build_image
        deploy_app
        echo ""
        show_access_info
        ;;
    status)
        check_status
        ;;
    access)
        show_access_info
        ;;
    cleanup)
        cleanup_minikube
        ;;
    help|--help|-h)
        show_help
        ;;
    "")
        echo "❌ Nenhuma opção fornecida."
        echo ""
        show_help
        exit 1
        ;;
    *)
        echo "❌ Opção inválida: $1"
        echo ""
        show_help
        exit 1
        ;;
esac
