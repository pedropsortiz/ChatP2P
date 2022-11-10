package br.ufsm.csi.redes.thread;

import lombok.SneakyThrows;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorThread implements Runnable{

    static final int porta = 8081;
    ServerSocket conexaoServidor = new ServerSocket(porta);
    Socket conexaoCliente;

    public ServidorThread() throws IOException {
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true){
            conexaoCliente = conexaoServidor.accept();

            ObjectOutputStream saida = new ObjectOutputStream(conexaoCliente.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(conexaoCliente.getInputStream());

            System.out.println(entrada.read());

            conexaoCliente.close();
        }
    }
}