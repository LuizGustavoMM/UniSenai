sal = float(input("Digite seu salário em reais saiba quantos ""%"" recebeu de aumento!\n\nR$:"))

if sal <= 280:
    aumento = sal * 0.20 + sal
    print("Seu salário antigo é de R$:{}\nSeu novo salario será de R$:{}\nReajuste de 20%" .format(sal, aumento))
elif sal >= 280.1 and sal <= 700:
    aumento = sal * 0.15 + sal
    print("Seu salário antigo é de R$:{}\nSeu novo salario será de R$:{}\nReajuste de 15%" .format(sal, aumento))
elif sal >= 700.01 and sal <= 1500:
    aumento = sal * 0.10 + sal
    print("Seu salário antigo é de R$:{}\nSeu novo salario será de R$:{}\nReajuste de 10%" .format(sal, aumento))
elif sal > 1500:
    aumento = sal * 0.05 + sal
    print("Seu salário antigo é de R$:{}\nSeu novo salario será de R$:{}\nReajuste de 5%" .format(sal, aumento))
else:
    print("Digite um valor válido!")