@echo off
chcp 65001 > nul
echo ===================================================
echo   A compilar o Comando Central...
echo ===================================================
if not exist bin mkdir bin

javac -encoding UTF-8 -cp "lib/*;src" -d bin src/App.java

if %errorlevel% neq 0 (
    echo.
    echo [ERRO] Ocorreu um erro ao compilar a aplicação!
    pause
    exit /b %errorlevel%
)

echo.
echo ===================================================
echo   A executar a aplicação...
echo ===================================================
java -cp "lib/*;bin" App
