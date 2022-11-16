package br.ufsm.csi.redes.c_s;

import br.ufsm.csi.redes.model.Mensagem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
public class ClienteThread implements Runnable{

    private InetAddress endereco;
    private int porta;
    private Mensagem mensagem = null;

    private Thread worker;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private Socket conexao;

    public ClienteThread(InetAddress endereco, int porta) throws IOException {
        this.endereco = endereco;
        this.porta = porta;
        this.conexao = new Socket(endereco, porta);
    }

    public ClienteThread(Socket conexao){
        this.conexao = conexao;
    }

    @SneakyThrows
    public void start() {
        running.set(true);
        worker = new Thread(this);
        worker.start();
    }

    public void stop() throws IOException {
        running.set(false);
        conexao.close();
        System.out.println("Conexão do cliente encerrada!");
    }

    //TODO: Criar método de recebimento e envio de pacotes

    public void setMensagem(Mensagem mensagem) throws IOException {
        this.mensagem = mensagem;
        ObjectOutputStream saida = new ObjectOutputStream(this.conexao.getOutputStream());
//        System.out.println(mensagem.mensagem());
        saida.writeObject(mensagem.mensagem());
        saida.flush();
    }

    @SneakyThrows
    public void run() {
        while (running.get()) {

        }

    }

}