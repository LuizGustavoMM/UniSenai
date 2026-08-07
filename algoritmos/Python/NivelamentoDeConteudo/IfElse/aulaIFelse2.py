min = int(input("Digite quantos minutos de telefone foi utilizado: "))
if min <= 199:
    print("Será cobrado R$:0,20 por minuto\nTotal: {}\nPreço a ser cobrado: R$:{}".format(min, min*0.20))
elif min >= 200 and min <= 400:
    print("Será cobrado R$:0,18 por minuto\nTotal: {}\nPreço a ser cobrado: R$:{}".format(min, min*0.18))
else:
    print("Será cobrado R$:0,15 por minuto\nTotal: {}\nPreço a ser cobrado: R$:{}".format(min, min*0.15))