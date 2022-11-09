package br.ufsm.csi.redes.server;

import lombok.SneakyThrows;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor implements Runnable{

    private final int porta = 8081;
    ServerSocket conexao = new ServerSocket(porta);
    Socket cliente;

    public Servidor() throws IOException {
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true){
            cliente = conexao.accept();
        }
    }
}