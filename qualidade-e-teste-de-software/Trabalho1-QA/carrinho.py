class CarrinhoDeCompras:
    def __init__(self):
        self.itens = []

    def adicionar_item(self, nome: str, preco: float):
        if preco <= 0:
            raise ValueError("O preço do item deve ser maior que zero.")
        self.itens.append({"nome": nome, "preco": preco})

    def calcular_total_produtos(self) -> float:
        return sum(item["preco"] for item in self.itens)
