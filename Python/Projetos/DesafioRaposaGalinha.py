#um homem precisa levar uma raposa uma galinha e um cesto de milho ate a outra margem do rio.
#o problema é que ele só pode leva uma dessas coisas de cada vez.
#levando o cesto de milho, a raposa comeria a galinha. se levar a raposa, a galinha come o milho

#Variaveis: Galinha, Milho e Raposa


#leva a galinha
#volto
#levo o milho
#volto com a galinha
#levo a raposa
#volto
#levo a galinha de volta

print('''um homem, que está no lado A precisa levar uma raposa uma galinha e um cesto de milho ate ao lado B.
o problema é que ele só pode leva uma dessas coisas de cada vez.
levando o cesto de milho, a raposa comeria a galinha. se levar a raposa, a galinha come o milho''')

G = "Galinha"
M = "Milho"
R = "Raposa"

lado_a = "Galinha Milho Raposa"
lado_b = ""

if G and M and R in lado_a:
    print("\nLado A:", lado_a)
    print("\nLevando galinha para lado B")
    lado_b = G
    print("\nLado B:", lado_b)
    print("\nVoltando")
    lado_a = M,R

if R and M in lado_a:
    print("\nLevando milho para o lado B")
    lado_b = M,G
    print("\nLado B:", lado_b)
    print("\nVoltando com a galinha para o lado A")
    lado_b = M
    lado_a = G,R
    print("\nLado A:", lado_a)

if G and R in lado_a:
    print("\nLevando raposa para o lado B")
    lado_b = M,R
    lado_a = G
    print("\nLado B:", lado_b)
    print("\nVoltando")

if G in lado_a and R and M in lado_b:
    print("\nLevando a galinha para o lado B")
    lado_b = M,R,G
    print("\nLado B:", lado_b)

print("\nParabéns, desafio concluido.")