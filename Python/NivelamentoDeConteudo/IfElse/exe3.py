sal1 = float(input("Digite o salário do primeiro colaborador em reais saiba quantos ""%"" recebeu de aumento!\n\nR$:"))

if sal1 <= 280:
    aumento1 = sal1 * 0.20 + sal1
    porcentagem1 = "20%"
elif sal1 <= 700:
    aumento1 = sal1 * 0.15 + sal1
    porcentagem1 = "15%"
elif sal1 >= sal1 <= 1500:
    aumento1 = sal1 * 0.10 + sal1
    porcentagem1 = "10%"
elif sal1 > 1500:
    aumento1 = sal1 * 0.05 + sal1
    porcentagem1 = "5%"

sal2 = float(input("Digite o salário do segundo colaborador em reais saiba quantos ""%"" recebeu de aumento!\n\nR$:"))

if sal2 <= 280:
    aumento2 = sal2 * 0.20 + sal2
    porcentagem2 = "20%"
elif sal2 <= 700:
    aumento2 = sal2 * 0.15 + sal2
    porcentagem2 = "15%"
elif sal2 <= 1500:
    aumento2 = sal2 * 0.10 + sal2
    porcentagem2 = "10%"
elif sal2 > 1500:
    aumento2 = sal2 * 0.05 + sal2
    porcentagem2 = "5%"

else:
    print("Digite um valor válido!")
if porcentagem1 > porcentagem2:
    print("A porcentagem do primero colaborador é maior\n\nSalário do primeiro colaborador teve {} de aumento\nR$:{}\nAumento em reais R$:{}\n\nSalário do segundo colaborador teve {} de aumento\nR$:{}\nAumento em reais R$:{}\n\n" .format(porcentagem1, aumento1, aumento1-sal1, porcentagem2, aumento2, aumento2-sal2))
elif porcentagem2 > porcentagem1:
    print("A porcentagem do segundo colaborador é maior\n\nSalário do primeiro colaborador teve {} de aumento\nR$:{}\nAumento em reais R$:{}\n\nSalário do segundo colaborador teve {} de aumento\nR$:{}\nAumento em reais R$:{}\n\n" .format(porcentagem1, aumento1, aumento1-sal1, porcentagem2, aumento2, aumento2-sal2))
elif aumento1 + aumento2 < 10000:
    print("---Limite excedido!---\nO limite total de aumento é de R$:10.000,00\n os dois salários somam um aumento de R$:{}".format(aumento2+aumento1))
