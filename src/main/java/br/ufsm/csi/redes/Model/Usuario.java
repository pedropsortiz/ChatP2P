package br.ufsm.csi.redes.Model;

public class Usuario {

    private String nome;
    public String username;

    public Usuario(String nome, String username) {
        this.nome = nome;
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
