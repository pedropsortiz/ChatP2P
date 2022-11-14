package br.ufsm.csi.redes.c_s;

import br.ufsm.csi.redes.model.Mensagem;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClienteThread implements Runnable{

    private InetAddress endereco;
    private int porta;
    private Mensagem mensagem;

    private Thread worker;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final AtomicBoolean mensagemEnvio = new AtomicBoolean(false);
    Socket conexao;


    public ClienteThread(InetAddress endereco, int porta){
        this.endereco = endereco;
        this.porta = porta;
    }

    @SneakyThrows
    public void start() {
        worker = new Thread(this);
        worker.start();
        conexao = new Socket(endereco, porta);
    }

    public void stop() throws IOException {
        running.set(false);
        conexao.close();
        System.out.println("Conexão do cliente encerrada!");
    }

    //TODO: Criar método de recebimento e envio de pacotes

    public void setMensagem(Mensagem mensagem){
        this.mensagem = mensagem;
        mensagemEnvio.set(false);
    }

    @SneakyThrows
    public void run() {
        running.set(true);
        while (running.get()) {
            if (mensagem.mensagem() != null){
                ObjectOutputStream saida = new ObjectOutputStream(conexao.getOutputStream());
                saida.writeObject(mensagem.mensagem());
                saida.flush();
                saida.close();
            }
        }

    }

}