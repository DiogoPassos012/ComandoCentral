Nome da Aplicação: Comando Central - Controlo e Gestão de Ocorrências

Descrição: A aplicação consiste de um sistema de controlo de ocorrências para um quartel de bombeiros. O sistema permite que o centralista registe ocorrências e despache meios em tempo real, enquanto as equipas operacionais conseguem visualizar a lista de occorencias ativas e os detalhes específicos de cada ocorrência , otimizando o tempo de resposta e a partilha de informação operacional.

Hierarquia de Classes:
Nível 1 - Superclasse (Abstrata): Ocorrencia

Atributos: Contém atributos protegidos comuns a qualquer saída (ex: id, localizacao, estado, horaAlerta, viaturaAtribuida) e atributos de classe para controlo global de contagem.

Nível 2 - Subclasses (Categorias Principais):

EmergenciaMedica (Herda de Ocorrencia): Adiciona atributos específicos para o socorro pré-hospitalar (ex: número de vítimas, queixa principal, idade do paciente).

AcidenteViacao (Herda de Ocorrencia): Adiciona atributos para colisões e despistes (ex: número de veículos envolvidos, presença de encarcerados, tipo de via).

Incendio (Herda de Ocorrencia): Uma classe base para eventos de fogo, contendo atributos comuns (ex: nível de alarme [1 a 5], tempo estimado de combate).

Nível 3 - Subclasses de Especialização:

IncendioUrbano (Herda de Incendio): Adiciona atributos específicos para fogos em estruturas (ex: tipo de edifício [habitação, comercial], número de pisos afetados).

IncendioFlorestal (Herda de Incendio): Adiciona atributos específicos para fogos florestais/rurais (ex: área afetada em hectares, relevo do terreno).

## Como Executar a Aplicação

Para evitar erros de `ClassNotFoundException` (ex: driver do SQLite não encontrado no Classpath), a aplicação deve ser compilada e executada incluindo as bibliotecas da diretoria `lib/`.

### Opção 1: Usar os Scripts de Execução Rápida (Recomendado)
Criámos scripts automáticos que tratam da compilação e definição do classpath:
- **No Windows**: Dê um duplo-clique no ficheiro [run.bat](file:///c:/Users/Wulf/Desktop/ComandoCentral/run.bat) ou execute `.\run.bat` no PowerShell/CMD.
- **No macOS / Linux**: Execute `bash run.sh` (ou dê permissão de execução com `chmod +x run.sh && ./run.sh`).

### Opção 2: Pelo VS Code
1. Certifique-se de que abre a pasta raiz `ComandoCentral` no VS Code (e não apenas o ficheiro isolado `App.java`).
2. O VS Code lerá a configuração em `.vscode/settings.json` e associará os ficheiros `.jar` da diretoria `lib/` automaticamente.
3. Clique em **Run** ou **Debug** em cima do método `main` no ficheiro [App.java](file:///c:/Users/Wulf/Desktop/ComandoCentral/src/App.java) (evite usar extensões não-oficiais como o *Code Runner*, use o *Extension Pack for Java* oficial).

### Opção 3: Manualmente via Linha de Comandos
Se preferir compilar e correr manualmente a partir do terminal na raiz do projeto:

**No Windows**:
```powershell
# Compilar
javac -cp "lib/*;src" -d bin src/App.java
# Executar
java -cp "lib/*;bin" App
```

**No macOS / Linux**:
```bash
# Compilar
javac -cp "lib/*:src" -d bin src/App.java
# Executar
java -cp "lib/*:bin" App
```