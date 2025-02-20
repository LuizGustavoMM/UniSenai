# Definição das distâncias entre as cidades (distâncias reais do Google Maps)
FJ = 179  # Florianópolis - Joinville
FB = 153  # Florianópolis - Blumenau
FC = 554  # Florianópolis - Chapecó
FL = 232  # Florianópolis - Lages
FR = 186  # Florianópolis - Criciúma
JB = 103  # Joinville - Blumenau
JC = 468  # Joinville - Chapecó
JL = 304  # Joinville - Lages
JR = 359  # Joinville - Criciúma
BC = 400  # Blumenau - Chapecó
BL = 223  # Blumenau - Lages
BR = 203  # Blumenau - Criciúma
CL = 349  # Chapecó - Lages
CR = 510  # Chapecó - Criciúma
LR = 202  # Lages - Criciúma

# Inicializar a menor distância como None e melhor rota como vazia
menor_distancia = None
melhor_rota = ""

# Todas as rotas possíveis partindo de Joinville
rota1 = JB + FB + FL + CR + CL + JC  # Joinville -> Blumenau -> Florianópolis -> Criciúma -> Lages -> Chapecó -> Joinville
rota2 = JB + FB + FR + CR + CL + JL  # Joinville -> Blumenau -> Florianópolis -> Criciúma -> Chapecó -> Lages -> Joinville
rota3 = JC + CL + CR + FR + FB + FJ  # Joinville -> Chapecó -> Lages -> Criciúma -> Florianópolis -> Blumenau -> Joinville
rota4 = JC + CL + BL + FB + FR + JR  # Joinville -> Chapecó -> Lages -> Blumenau -> Florianópolis -> Criciúma -> Joinville
rota5 = JR + FR + FB + BL + CL + JC  # Joinville -> Criciúma -> Florianópolis -> Blumenau -> Lages -> Chapecó -> Joinville
rota6 = JR + BR + BL + CL + JC + FJ  # Joinville -> Criciúma -> Blumenau -> Lages -> Chapecó -> Florianópolis -> Joinville

# Comparação das rotas
if menor_distancia is None or rota1 < menor_distancia:
    menor_distancia = rota1
    melhor_rota = "Joinville -> Blumenau -> Florianópolis -> Criciúma -> Lages -> Chapecó -> Joinville"

if rota2 < menor_distancia:
    menor_distancia = rota2
    melhor_rota = "Joinville -> Blumenau -> Florianópolis -> Criciúma -> Chapecó -> Lages -> Joinville"

if rota3 < menor_distancia:
    menor_distancia = rota3
    melhor_rota = "Joinville -> Chapecó -> Lages -> Criciúma -> Florianópolis -> Blumenau -> Joinville"

if rota4 < menor_distancia:
    menor_distancia = rota4
    melhor_rota = "Joinville -> Chapecó -> Lages -> Blumenau -> Florianópolis -> Criciúma -> Joinville"

if rota5 < menor_distancia:
    menor_distancia = rota5
    melhor_rota = "Joinville -> Criciúma -> Florianópolis -> Blumenau -> Lages -> Chapecó -> Joinville"

if rota6 < menor_distancia:
    menor_distancia = rota6
    melhor_rota = "Joinville -> Criciúma -> Blumenau -> Lages -> Chapecó -> Florianópolis -> Joinville"

# Exibição da menor distância e melhor rota
print("A menor distância é:", menor_distancia, "km")
print("A melhor rota é:", melhor_rota)
