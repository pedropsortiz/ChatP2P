package br.ufsm.csi.redes.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MensagemRadar implements Runnable{

    private final int porta = 5000;
    ServerSocket conexaoServidor = new ServerSocket(porta);
    Socket conexaoCliente;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_START = "\u001B[32m";

    public MensagemRadar() throws IOException {
        new Thread(new MensagemRadar()).start();
    }


    @Override
    public void run() {
        System.out.println(ANSI_START + "Escutando a porta " + porta + ANSI_RESET);
        while (true){

        }
    }
}
