# FUNÇÕES

# Função para verificar o estado baseado nas letras da placa
def inserir_estado(placa):
    prefixo = placa[:3]  # Pegamos as três primeiras letras da placa

    # Comparações para determinar o estado
    if "AAA" <= prefixo <= "BEZ":
        return "Paraná"
    elif "BFA" <= prefixo <= "GKI":
        return "São Paulo"
    elif "GKJ" <= prefixo <= "HOK":
        return "Minas Gerais"
    elif "HOL" <= prefixo <= "HQE":
        return "Maranhão"
    elif "HQF" <= prefixo <= "HTW":
        return "Mato Grosso do Sul"
    elif "HTX" <= prefixo <= "HZA":
        return "Ceará"
    elif "HZB" <= prefixo <= "IAP":
        return "Sergipe"
    elif "IAQ" <= prefixo <= "JDO":
        return "Rio Grande do Sul"
    elif "JDP" <= prefixo <= "JKR":
        return "Distrito Federal"
    elif "JKS" <= prefixo <= "JSZ":
        return "Bahia"
    elif "JTA" <= prefixo <= "JWE":
        return "Pará"
    elif "JWF" <= prefixo <= "JXY":
        return "Amazonas"
    elif "JXZ" <= prefixo <= "KAU":
        return "Mato Grosso"
    elif "KAV" <= prefixo <= "KFC":
        return "Goiás"
    elif "KFD" <= prefixo <= "KME":
        return "Pernambuco"
    elif "KMF" <= prefixo <= "LVE":
        return "Rio de Janeiro"
    elif "LVF" <= prefixo <= "LWQ":
        return "Piauí"
    elif "LWR" <= prefixo <= "MMM":
        return "Santa Catarina"
    elif "MMN" <= prefixo <= "MOW":
        return "Paraíba"
    elif "MOX" <= prefixo <= "MTZ":
        return "Espírito Santo"
    elif "MUA" <= prefixo <= "MVK":
        return "Alagoas"
    elif "MVL" <= prefixo <= "MXG":
        return "Tocantins"
    elif "MXH" <= prefixo <= "MZM":
        return "Rio Grande do Norte"
    elif "MZN" <= prefixo <= "NAG":
        return "Acre"
    elif "NAH" <= prefixo <= "NBA":
        return "Roraima"
    elif "NBB" <= prefixo <= "NEH":
        return "Rondônia"
    elif "NEI" <= prefixo <= "NFB":
        return "Amapá"
    elif "NFC" <= prefixo <= "NGZ":
        return "Mato Grosso"
    elif "NHA" <= prefixo <= "NHT":
        return "Goiás"
    else:
        return "Estado desconhecido"



# Validar Placa:
def validar_placa(placa):
    # Verificação do formato padrão AAA-1111
    if len(placa) == 8 and placa[3] == "-":
        for char in placa[:3]:
            if char < "A" or char > "Z":
                return False
            
        for num in placa[4:]:
            if num < "0" or num > "9":
                return False
        return True
    else:
        return False


# Converter Placa:
def converter_placa_mercosul(placa):
    letras = "ABCDEFGHIJ"
    nova_placa = placa[:5] + letras[int(placa[4])] + placa[6:]
    return nova_placa


# Cadastrar uma nova placa:
def cadastrar_placa(placas):
  # Solicita ao usuário a placa:
    placa = input("Digite a placa no formato AAA-1111: ").upper()
  # Verifica se a placa é valida:
    if validar_placa(placa):
    # Sublista com as informações da placa:
        placas.append([placa,
                   converter_placa_mercosul(placa), # Placa convertida para o padrão Mercosul
                   inserir_estado(placa) # Estado associado ao prefixo
                   ])
        print(placas) # Placa no formato antigo
        print("Placa cadastrada com sucesso!")
    else:
        print("Placa inválida!")


# Constultar o estado de uma placa:
def verificar_estado(placa):
    # Solicita ao usuário uma placa
    placa = input("Digite a placa no formato AAA-1111: ").upper()

    # Verifica se a placa é válida
    if validar_placa(placa):
        # Chama a função 'inserir_estado' para identificar o estado
        estado = inserir_estado(placa)
        print(f"A placa {placa} é do estado: {estado}")
    else:
        print("Placa inválida!")

# Listar todas as placas cadastradas:
def listar_placas(placas):
    if len(placas):
        for placa in placas:
            print(placa)
    else:
        print('\nNenhuma placa cadastrada!')

# Atualizar placa:
def atualizar_placa(placas):
    cont = -1
    for placa in placas:
        cont += 1
        print('Índice:',cont , placa)
    indice = int(input('\nDigite o índice da placa que deseja alterar: '))
    att_placa = input("Digite a placa no formato AAA-1111: ").upper()
    if validar_placa(att_placa):
        placas[indice] = [att_placa, converter_placa_mercosul(att_placa), inserir_estado(att_placa)]
        print(placas) # Placa no formato antigo
        print("Placa cadastrada com sucesso!")
    else:
        print("Placa inválida!")


# Excluir uma placa:
def excluir_placa(placas):
    cont = -1
    for placa in placas:
        cont += 1
        print('Índice:',cont , placa)
    excluir_indice = int(input('\nDigite o índice da placa que deseja excluir: '))
    del placas[excluir_indice]
    for placa in placas:
        print('Lista de placas atualizada')
        print('Índice:',cont , placa)

# Programa principal
placas = [] # Lista para armazenar as placas cadastradas

while True:
    print("\nMENU:")
    print("1. Cadastrar placa")
    print("2. Consultar estado")
    print("3. Lista de placas")
    print("4. Atualizar placa")
    print("5. Excluir placa")
    print("6. Sair")

    opcao = input("\nEscolha uma opção: ")

    if opcao == "1":
        cadastrar_placa(placas)
    elif opcao == "2":
        verificar_estado(placas)
    elif opcao == "3":
        listar_placas(placas)
    elif opcao == "4":
        atualizar_placa(placas)
    elif opcao == "5":
        excluir_placa(placas)
    elif opcao == "6":
        print("Saindo...")
        break
    else:
        print("Opção inválida!")