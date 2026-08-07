# language: pt
Funcionalidade: Cálculo de Distância de Frenagem na Pista
  Como Sistema de Gestão de Voo (FMS)
  Quero calcular a distância necessária para a parada total da aeronave após o toque na pista
  Para garantir a segurança do pouso e evitar um runway excursion
 
  Contexto:
    Dado que a velocidade de referência (Vref) para este pouso é 260 km/h
    E que a desaceleração padrão de frenagem da aeronave é 2.5 m/s²
 
  Cenário: Cálculo de distância segura para pouso em pista molhada com reversores ativos
    Dado que a condição da pista é "molhada"
    E que os reversores de empuxo estão "ativos"
    Quando a aeronave toca a pista a uma velocidade de 250 km/h
    Então o sistema deve calcular a distância de frenagem normalmente
    E a distância de frenagem calculada deve ser aproximadamente 942.8 metros
    E nenhum alerta de segurança deve ser emitido
 
  Cenário: Alerta de segurança e recusa do cálculo quando a velocidade de toque excede o limite Vref + 20
    Dado que a condição da pista é "seca"
    E que os reversores de empuxo estão "inativos"
    Quando a aeronave toca a pista a uma velocidade de 285 km/h
    Então o sistema deve emitir um alerta de segurança no painel do piloto
    E o sistema deve recusar o cálculo padrão de distância de frenagem
    E o sistema deve exigir autorização manual para arremetida "go-around"
 
  Cenário: Cálculo para condição extrema de pista com gelo, com incremento de 70% na distância mínima
    Dado que a condição da pista é "gelo"
    E que os reversores de empuxo estão "inativos"
    Quando a aeronave toca a pista a uma velocidade de 240 km/h
    Então o sistema deve calcular a distância de frenagem normalmente
    E a distância de frenagem calculada deve ser aproximadamente 1511.1 metros
    E essa distância deve ser 70% maior do que a distância calculada nas mesmas condições em pista seca