package br.ufsm.csi.redes.c_s;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PeerServer {
    private AtomicBoolean parar;
    private static final int porta = 8081;
    ServerSocket conexao;

    public void start() throws IOException {
        conexao = new ServerSocket(porta);
        parar = new AtomicBoolean(true);
        new Thread(new EsperandoMensagem()).start();
    }

    public void stop(){
        parar.set(false);
    }

    public class EsperandoMensagem implements Runnable{

        @Override
        public void run() {
            while (!parar.get()) {
                //TODO
            }
        }
    }

}
