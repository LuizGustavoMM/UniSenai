nota = int(input("Digite uma nota entre 0 e 10: "))

match nota:
    case 0 | 1 | 2 | 3 | 4:
        print ("Classificação: insuficiente")
    case 5 | 6:
        print ("Classificação: Suficiente")
    case 7 | 8:
        print ("Classificação: Bom")
    case 9 | 10:
        print ("Classificação: Excelente")
    case _:
        print ("Nota inválida")