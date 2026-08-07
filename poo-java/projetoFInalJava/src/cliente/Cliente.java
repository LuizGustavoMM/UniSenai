package cliente;

import database.Conexao;
import utils.CNPJUtil;
import utils.CPFUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class  Cliente {
    private int id;

    private String nome;
    private String telefone;
    private final String tipo;
    private final String cpf_cnpj;

    public Cliente(int id, String nome, String telefone, String tipo, String cpf_cnpj) {
        if(id < 1){
            throw new IllegalArgumentException("O id não pode ser negativo");
        }
        if((nome == null) || nome.trim().isEmpty()){
            throw new IllegalArgumentException("O nome não pode ser null nem vazio");
        }
        if(telefone == null || nome.trim().isEmpty()){
            throw new IllegalArgumentException("O telefone não pode ser null new vazio.");
        }
        if(!telefone.matches("\\d+")) {
            throw new IllegalArgumentException("O telefone deve conter apenas números");
        }
        if(tipo == null || (!tipo.equals("F")) && !tipo.equals("J")){
            throw new IllegalArgumentException("O tipo de cliente deve ser F(Pessoa Física) ou J(Pessoa Jurídica)");
        }
        if (tipo.equals("F") && CPFUtil.isCPFValido(cpf_cnpj)
                || tipo.equals("J") && CNPJUtil.isCNPJValido(cpf_cnpj)) {
            this.cpf_cnpj = cpf_cnpj;
        } else {
            throw new IllegalArgumentException("O tipo (CPF | CNPJ) não bate com o número informado");
        }
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.tipo = tipo;
    }

    public Cliente( String nome, String telefone, String tipo, String cpf_cnpj) {
        if((nome == null) || nome.trim().isEmpty()){
            throw new IllegalArgumentException("O nome não pode ser null nem vazio");
        }
        if(telefone == null || nome.trim().isEmpty()){
            throw new IllegalArgumentException("O telefone não pode ser null new vazio.");
        }
        if(!telefone.matches("\\d+")) {
            throw new IllegalArgumentException("O telefone deve conter apenas números");
        }
        if(tipo == null || (!tipo.equals("F")) && (!tipo.equals("J"))){
            throw new IllegalArgumentException("O tipo de cliente deve ser F(Pessoa Física) e J(Pessoa Jurídica)");
        }
        if (tipo.equals("F") && CPFUtil.isCPFValido(cpf_cnpj)
                || tipo.equals("J") && CNPJUtil.isCNPJValido(cpf_cnpj)) {

            this.cpf_cnpj = cpf_cnpj;
        } else {
            throw new IllegalArgumentException("O tipo (CPF | CNPJ) não bate com o número informado");
        }
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if(nome == null || nome.trim().isEmpty()){
            throw new IllegalArgumentException("O nome não pode ser null nem vazio.");
        }
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        if(telefone == null || nome.trim().isEmpty()){
            throw new IllegalArgumentException("O telefone não pode ser null new vazio.");
        }
        if(!telefone.matches("\\d+")) {
            throw new IllegalArgumentException("O telefone deve conter apenas números");
        }
        this.telefone = telefone;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCpf_cnpj() {
        return cpf_cnpj;
    }

    public int getId () {return id;}

    public void setId(int id) { this.id = id ;}
}
