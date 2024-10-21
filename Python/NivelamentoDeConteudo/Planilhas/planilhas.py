import pandas as pd
planilha = pd.read_excel('C:\Users\Pichau\UniSenai\Python\NivelamentoDeConteudo\planilhas\controle_da_empresa.xlsx')
planilha['Vendas']['Total de Vendas'].sum()