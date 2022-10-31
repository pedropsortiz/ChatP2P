package br.ufsm.csi.redes.Model;

import br.ufsm.csi.redes.Interface.ChatClientSwing;

import java.net.InetAddress;

public class Usuario {

    private String nome;
    private ChatClientSwing.StatusUsuario status;
    private InetAddress endereco;

    public enum StatusUsuario {
        DISPONIVEL, NAO_PERTURBE, VOLTO_LOGO
    }

    public Usuario(String nome, ChatClientSwing.StatusUsuario status, InetAddress endereco) {
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

    public ChatClientSwing.StatusUsuario getStatus() {
        return status;
    }

    public void setStatus(ChatClientSwing.StatusUsuario status) {
        this.status = status;
    }

    public InetAddress getEndereco() {
        return endereco;
    }

    public void setEndereco(InetAddress endereco) {
        this.endereco = endereco;
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }

    public String toString() {
        return this.getNome() + " (" + getStatus().toString() + ")";
    }


}
