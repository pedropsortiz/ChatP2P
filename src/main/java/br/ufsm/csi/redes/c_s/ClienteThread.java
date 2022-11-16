package br.ufsm.csi.redes.c_s;

import br.ufsm.csi.redes.model.Mensagem;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClienteThread implements Runnable{

    private InetAddress endereco;
    private int porta;
    private Mensagem mensagem = null;

    private Thread worker;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final AtomicBoolean mensagemEnvio = new AtomicBoolean(false);
    Socket conexao;
    DatagramSocket ds;


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
        worker = new Thread(this);
        worker.start();
    }

    public void stop() throws IOException {
        running.set(false);
        conexao.close();
        System.out.println("Conexão do cliente encerrada!");
    }

    public void send(Mensagem mensagem){
        String msg = mensagem.getMensagemTexto();
        try {
            DatagramPacket dp =
                    new DatagramPacket(msg.getBytes(),
                            msg.getBytes().length,
                            new InetSocketAddress(mensagem.getDestinatario().getEndereco(), 8081));
            ds.send(dp);
        } catch(Exception e) {
            e.printStackTrace();
        }
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
                ObjectOutputStream saida = new ObjectOutputStream(conexao.getOutputStream());
                saida.writeObject(mensagem.mensagem());
                saida.flush();
                saida.close();
                Thread.sleep(1000);
        }

    }

}