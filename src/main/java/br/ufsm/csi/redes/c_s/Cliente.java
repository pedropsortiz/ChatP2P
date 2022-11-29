package br.ufsm.csi.redes.c_s;

import br.ufsm.csi.redes.model.Mensagem;
import br.ufsm.csi.redes.view.ChatClientSwing;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
public class Cliente implements Runnable{

    private InetAddress endereco;
    private int porta;
    private Mensagem mensagem = null;

    private Thread worker;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private Socket conexao;
    ObjectOutputStream saida;
    ChatClientSwing.PainelChatPVT tab;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(conexao, cliente.conexao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conexao);
    }

    public Cliente(InetAddress endereco, int porta) throws IOException {
        this.endereco = endereco;
        this.porta = porta;
        this.conexao = new Socket(endereco, porta);
        saida = new ObjectOutputStream(this.conexao.getOutputStream());
    }

    public Cliente(Socket conexao) throws IOException {
        this.endereco = conexao.getInetAddress();
        this.porta = conexao.getPort();
        this.conexao = conexao;
        saida = new ObjectOutputStream(this.conexao.getOutputStream());
    }

    @SneakyThrows
    public void start() {
        running.set(true);
        worker = new Thread(this);
        worker.start();
    }

    public void stop() throws IOException {
        conexao.close();
        running.set(false);
    }

    //TODO: Criar método de recebimento e envio de pacotes

    public void setMensagem(Mensagem mensagem) throws IOException {
        saida.writeObject(mensagem.mensagem());
        saida.flush();
    }

    @SneakyThrows
    public void run() {
        ObjectInputStream entrada = new ObjectInputStream(conexao.getInputStream());
        while (true) {
            synchronized (entrada) {
                try {
                    tab.areaChat.append((String) entrada.readObject());
                } catch(Exception e){
                    tab.areaChat.append("\nUsuário desconectado!");
                    tab.campoEntrada.setEnabled(false);
                    break;
                }
            }
        }

    }

}