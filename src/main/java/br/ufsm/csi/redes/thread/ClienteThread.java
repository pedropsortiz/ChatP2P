package br.ufsm.csi.redes.thread;

import br.ufsm.csi.redes.model.Mensagem;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClienteThread implements Runnable{

    private InetAddress endereco;
    private int porta;
    Socket conexao;
    private Mensagem mensagem;

    private Thread worker;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean mensagemEnvio = new AtomicBoolean(true);

    public ClienteThread(InetAddress endereco, int porta){
        this.endereco = endereco;
        this.porta = porta;
    }

    public void start() {
        worker = new Thread(this);
        worker.start();
    }

    @SneakyThrows
    public void stop() {
        running.set(false);
        System.out.println("A conexão foi morta");
        conexao.close();
    }

    //TODO: Criar método de recebimento e envio de pacotes

    public void sendMessage(Mensagem mensagem){
        this.mensagem = mensagem;
        mensagemEnvio.set(false);
    }

    @SneakyThrows
    @Override
    public void run() {
        running.set(true);
        System.out.println("A conexão foi estabelecido, aguardo uma mensagem");
        while (running.get()) {
            conexao = new Socket(endereco, porta);
            while (mensagemEnvio.get()){}
            if (mensagemEnvio.get()){
                while(true){}
            } else {
                ObjectOutputStream saida;
                saida = new ObjectOutputStream(conexao.getOutputStream());

                //Enviando a mensagem do cliente para o servidor
                saida.writeObject(mensagem.getUsuario().getNome() + " > " + mensagem.getMensagemTexto());
                saida.flush();
                saida.close();
                mensagemEnvio.set(true);
            }

        }
    }

}