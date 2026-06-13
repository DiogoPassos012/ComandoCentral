#!/bin/bash
echo "==================================================="
echo "  A compilar o Comando Central..."
echo "==================================================="
mkdir -p bin

javac -encoding UTF-8 -cp "lib/*:src" -d bin src/App.java

if [ $? -ne 0 ]; then
    echo ""
    echo "[ERRO] Ocorreu um erro ao compilar a aplicação!"
    exit 1
fi

echo ""
echo "==================================================="
echo "  A executar a aplicação..."
echo "==================================================="
java -cp "lib/*:bin" App
