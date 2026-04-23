# 📦 Sistema de Vendas com Interface Gráfica (Java + Swing)

Este projeto é um sistema de controle de vendas com interface gráfica (GUI) feito em Java utilizando **Swing**. O sistema permite o cadastro e login de clientes, realização de vendas com cálculo de desconto por tipo de cliente (Pessoa Física ou Jurídica), geração de relatórios para gerentes e exportação de dados em arquivos `.txt`.

## 🧰 Tecnologias Utilizadas

- Java 17+
- Swing (Interface Gráfica)
- JDBC (para conexão com banco de dados relacional)
- MVC (Model-View-Controller)

## 🔍 Funcionalidades

### 🛒 Cliente
- Cadastro e login de cliente
- Compra de produtos com visualização do carrinho
- Cálculo automático de desconto:
  - Pessoa Física: 5%
  - Pessoa Jurídica: 10%

### 🧾 Vendas
- Criação de vendas com múltiplos itens
- Persistência de vendas com total calculado
- Exibição das vendas realizadas no dia

### 📊 Relatórios para Gerentes
- Vendas realizadas no dia
- Clientes mais rentáveis
- Produtos mais rentáveis
- Exportação de relatórios para `.txt`


## ✅ Requisitos

- JDK 17 ou superior
- IDE de sua preferência (IntelliJ, Eclipse, VS Code)
- Banco de dados relacional (ex: MySQL ou H2)
- Driver JDBC configurado

## 🚀 Como Executar

1. Clone este repositório.
2. Execute com : mercadinho-entrega % java -cp "dist/JavaMercado.jar:dist/mysql-connector-j-9.3.0.jar" ui.TelaInicial
 

## Authores
Desenvolvido por Lucca Della Pasqua e Luiz Massing.

