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

    private Mensagem mensagem;

    private Thread worker;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public ClienteThread(InetAddress endereco, int porta, Mensagem mensagem){
        this.endereco = endereco;
        this.porta = porta;
        this.mensagem = mensagem;
    }

    public void start() {
        worker = new Thread(this);
        worker.start();
    }

    public void stop() {
        running.set(false);
    }

    @SneakyThrows
    @Override
    public void run() {
        running.set(true);
        while (running.get()) {
            try {
//            System.out.println("Nova conexão estabelecida");
                Socket conexao;
                ObjectOutputStream saida;
//            ObjectInputStream entrada;

                //Estabelecendo conexão com o servidor
                conexao = new Socket(endereco, porta);

//            while (mensagem == null){}


                saida = new ObjectOutputStream(conexao.getOutputStream());
//            entrada = new ObjectInputStream(conexao.getInputStream());

                //Enviando a mensagem do cliente para o servidor
                saida.write(1);
                saida.flush();
                Thread.sleep(1000);

//            entrada.close();
//            saida.close();
//            conexao.close();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }

}