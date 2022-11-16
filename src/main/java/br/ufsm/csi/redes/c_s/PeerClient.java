package br.ufsm.csi.redes.c_s;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class PeerClient {

    private AtomicBoolean parar;
    private static final int porta = 8081;
    Socket conexao;
    InetAddress endereco;

    public void start() throws IOException {
        conexao = new Socket(endereco, porta);
        parar = new AtomicBoolean(true);
        new Thread(new EnviaMensagem()).start();
    }

    public void stop(){
        parar.set(false);
    }

    public class EnviaMensagem implements Runnable{
        @Override
        public void run() {
            while (!parar.get()) {
                //TODO
            }
        }
    }

}
