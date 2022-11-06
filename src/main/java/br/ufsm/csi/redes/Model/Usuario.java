package br.ufsm.csi.redes.Model;

import br.ufsm.csi.redes.Interface.ChatClientSwing;

import java.net.InetAddress;

public class Usuario {

    private String nome;
    private StatusUsuario status;
    private InetAddress endereco;

    public enum StatusUsuario {
        DISPONIVEL, NAO_PERTURBE, VOLTO_LOGO
    }

    public Usuario() {
    }

    public Usuario(String nome, StatusUsuario status, InetAddress endereco) {
        this.nome = nome;
        this.status = status;
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public StatusUsuario getStatus() {
        return status;
    }

    public void setStatus(StatusUsuario status) {
        this.status = status;
    }

    public InetAddress getEndereco() {
        return endereco;
    }

    public void setEndereco(InetAddress endereco) {
        this.endereco = endereco;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;

        return nome.equals(usuario.getNome());

    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }

    public String toString() {
        return this.getNome() + " (" + getStatus().toString() + ")";
    }


}
