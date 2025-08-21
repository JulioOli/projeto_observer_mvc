#!/bin/bash

# Script para compilar e executar o projeto MonitorAcoes
# Uso: ./run.sh [compile|run|both]

PROJECT_DIR="$(pwd)"
BIN_DIR="$PROJECT_DIR/bin"

compile() {
    echo "Compilando projeto..."
    mkdir -p "$BIN_DIR"
    javac -d "$BIN_DIR" -sourcepath . MonitorAcoes.java model/*.java view/*.java controller/*.java
    if [ $? -eq 0 ]; then
        echo "Compilação concluída com sucesso."
    else
        echo "Erro na compilação."
        exit 1
    fi
}

run() {
    echo "Executando projeto..."
    java -cp "$BIN_DIR" MonitorAcoes
}

case "$1" in
    compile)
        compile
        ;;
    run)
        run
        ;;
    both|*)
        compile
        run
        ;;
esac
