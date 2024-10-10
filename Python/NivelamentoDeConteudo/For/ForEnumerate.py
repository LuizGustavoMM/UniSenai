unidade=["Newton", "Joule", "Kelvin", "Pascal"]
print(unidade)
x=len(unidade)
for i, elemento in enumerate (unidade):
    print("No índice = ",(x-1)," temos: ", unidade[x-1])
    x=x-1