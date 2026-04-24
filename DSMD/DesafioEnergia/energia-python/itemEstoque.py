class ItemEstoque:
    def __init__(self, codigo, nome, preco, quantidade):
        self.codigo = codigo
        self.nome = nome
        self.preco = preco
        self.quantidade = quantidade
    
    def __str__(self):
        return f"Código: {self.codigo} | Nome: {self.nome} | Valor: R${self.preco} | Quantidade: {self.quantidade}"
