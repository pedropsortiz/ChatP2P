package br.ufsm.csi.redes.c_s;

import br.ufsm.csi.redes.view.ChatClientSwing;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Setter
@Getter
public class Servidor {

    static final int porta = 8081;
    private ServerSocket serverSocket;
    private Socket conexao;
    public Boolean parar = false;
    private ObjectInputStream  entrada;
    private ObjectOutputStream saida;
    private ChatClientSwing janela;

    public Servidor(ChatClientSwing janela) throws IOException {
        this.serverSocket = new ServerSocket(this.porta);
        this.janela = janela;
        new Thread(new EsperaChat()).start();
    }

    public class EsperaChat implements Runnable {
        @Override
        public void run() {
            while(!parar){
                try {
                    Socket conexao = serverSocket.accept();
                    janela.iniciaChat(conexao);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}