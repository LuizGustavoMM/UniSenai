texto = "python é divertido"
texto_maiusculo = texto.upper()
print(texto_maiusculo)

texto_minusculo = texto.lower()
print(texto_minusculo)

texto_capitalizado = texto.capitalize()
print(texto_capitalizado)

texto_titulo = texto.title()
print(texto_titulo)

texto_sem_espaco = texto.strip()
print(texto_sem_espaco)

texto_sem_espaco_no_final = texto.rstrip()
print(texto_sem_espaco_no_final)

texto_substituido = texto.replace("divertido", "poderoso")
print(texto_substituido)

indice = texto.find("divertido")
print(indice)

comeca_com_python = texto.startswith("python")
print(comeca_com_python)

termina_com_divertido = texto.endswith("divertido")
print(comeca_com_python)

texto_split = texto.split()
print(texto_split)

texto_split = texto.split("-")
print(texto_split)

palavras = ["python", "é", "divertido"]
frase = " ".join(palavras)
print(frase)

contagem = texto.count("python")
print(contagem)

digitos = "12345"
somente_digitos = digitos.isdigit()
print(somente_digitos)

texto_com_digitos = "python123"
alfanumerico = texto.isalnum()
print(alfanumerico)

num = " 42"
texto_preenchido = num.zfill(5)
print(texto_preenchido)