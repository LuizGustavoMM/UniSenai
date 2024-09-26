dia = int(input("Digite 1 a 7 para identificar qual dia da semana se iguala."))

match dia:
    case 1:
        print("Domingo")
    case 2:
        print("Segunda-feira")
    case 3:
        print("Terça-feira")
    case 4:
        print("Quarta-feira")
    case 5:
        print("Quinta-feira")
    case 6:
        print("Sexta-feira")
    case 7:
        print("Sábado-feira")
    case _:
        print("Número fora do range!")