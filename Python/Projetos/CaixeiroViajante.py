# Distâncias entre as cidades (definidas como variáveis para facilitar o cálculo)
AB = 100 # Distância de A para B
AC = 15 # Distância de A para C
AD = 20 # Distância de A para D
BC = 35 # Distância de B para C
BD = 25 # Distância de B para D
CD = 30 # Distância de C para D
# Inicialização das variáveis que armazenarão a menor distância encontrada e a melhor rota
menor_distancia = None # Nenhuma distância definida ainda
melhor_rota = "" # Nenhuma rota definida ainda
# Cálculo das distâncias totais para cada rota possível saindo de A e retornando a A
rota1 = AB + BC + CD + AD # A -> B -> C -> D -> A
rota2 = AB + BD + CD + AC # A -> B -> D -> C -> A
rota3 = AC + BC + BD + AD # A -> C -> B -> D -> A
rota4 = AC + CD + BD + AB # A -> C -> D -> B -> A
rota5 = AD + BD + BC + AC # A -> D -> B -> C -> A
rota6 = AD + CD + BC + AB # A -> D -> C -> B -> A
# Comparação para encontrar a menor distância entre as rotas
if menor_distancia is None or rota1 < menor_distancia:
    menor_distancia = rota1 # Atualiza a menor distância para a rota1
melhor_rota = "A -> B -> C -> D -> A" # Define a melhor rota como sendo rota1
if rota2 < menor_distancia:
    menor_distancia = rota2 # Atualiza a menor distância para a rota2
melhor_rota = "A -> B -> D -> C -> A" # Define a melhor rota como sendo rota2
if rota3 < menor_distancia:
    menor_distancia = rota3 # Atualiza a menor distância para a rota3
melhor_rota = "A -> C -> B -> D -> A" # Define a melhor rota como sendo rota3
if rota4 < menor_distancia:
    menor_distancia = rota4 # Atualiza a menor distância para a rota4
melhor_rota = "A -> C -> D -> B -> A" # Define a melhor rota como sendo rota4
if rota5 < menor_distancia:
    menor_distancia = rota5 # Atualiza a menor distância para a rota5
melhor_rota = "A -> D -> B -> C -> A" # Define a melhor rota como sendo rota5
if rota6 < menor_distancia:
    menor_distancia = rota6 # Atualiza a menor distância para a rota6
melhor_rota = "A -> D -> C -> B -> A" # Define a melhor rota como sendo rota6
# Saída do resultado final
print("A menor distância é:", menor_distancia, "km") # Exibe a menor distância encontrada
print("A melhor rota é:", melhor_rota) # Exibe a melhor rota correspondente