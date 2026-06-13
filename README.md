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