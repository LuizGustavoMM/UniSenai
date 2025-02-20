num1 = int(input("Digite a velocidade em Km/h.\nVelocidade: "))
multa= (num1 - 80) * 5
if num1 > 80:
    print("Velocidade superior ao permitido! Pague 5 reais de multa para cada Km/h a cima da velocidade permitida\nValor da multa: {} Reais.".format(multa))