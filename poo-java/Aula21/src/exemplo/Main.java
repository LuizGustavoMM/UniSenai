package exemplo;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        /*
         * Para execução:
         * - execute o banco de dados:
         * - docker pull mysql:9
         * - docker run -e MYSQL_DATABASE=meubanco -e MYSQL_ROOT_PASSWORD=rootsegredo -e MYSQL_USER=senai -e MYSQL_PASSWORD=segredo -p 3306:3306 -d mysql:9
         * - descomente as linhas passo-a-passo.
         */

        /*
         * PASSO 1: Exemplo de conexão
         */

//        Connection conn = Conexao.getConnection();
//        if (conn != null) {
//            System.err.println("Conexao estabelecida com sucesso!");
//            Conexao.close(conn);
//        } else {
//            System.err.println("Erro na tentativa de conexao!");
//            System.exit(1);
//        }

        /*
         * PASSO 2: Exemplo de conexão com uso de properties
         */

        Connection conn = ConexaoComPropriedades.getConnection();
        if (conn != null) {
            System.err.println("Conexao estabelecida com sucesso!");
            ConexaoComPropriedades.close(conn);
        } else {
            System.err.println("Erro na tentativa de conexao!");
            System.exit(1);
        }

        /*
         * PASSO 3: Exemplo de criação de tabela
         */

        CriacaoTabela ct = new CriacaoTabela();
        ct.criar();

        /*
         * PASSO 4: Exemplo de operações de manipulação
         */

        ManipulacaoBD m = new ManipulacaoBD();
//        m.inserirInseguro("Pessoa insegura", "0123456789");
        m.inserirSeguro("Pessoa segura", "9876543210");
        m.atualizar(1, "Agora atualizado com seguranca", "999999999999");
        m.excluir(2);
        m.listar();
        m.encerrar();
    }

}
