package br.ufsm.csi.redes.thread;

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

    public void start() {
        worker = new Thread(this);
        worker.start();
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

            conexao = new Socket(endereco, porta);

            ObjectOutputStream saida = new ObjectOutputStream(conexao.getOutputStream());
            saida.writeObject(mensagem.mensagem());
            saida.flush();
            saida.close();

            conexao.close();
            break;
        }

    }

}