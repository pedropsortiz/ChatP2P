package br.ufsm.csi.redes.c_s;

import br.ufsm.csi.redes.view.ChatClientSwing;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class PeerServer {
    private AtomicBoolean parar;
    private static final int porta = 8081;
    private ServerSocket serverSocket;

    public void start() throws IOException {
        serverSocket = new ServerSocket(porta);
        parar = new AtomicBoolean(true);
        new Thread(new EsperandoChat()).start();
    }

    public void stop(){
        parar.set(false);
    }

    public class EsperandoChat implements Runnable{

        @Override
        public void run() {
            while (!parar.get()) {
                //TODO
                try {
                    Socket conexao = serverSocket.accept();
//                    ChatClientSwing.PainelChatPVT.
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
