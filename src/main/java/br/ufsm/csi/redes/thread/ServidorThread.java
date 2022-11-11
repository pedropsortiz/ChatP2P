package br.ufsm.csi.redes.thread;

import br.ufsm.csi.redes.model.Mensagem;
import br.ufsm.csi.redes.model.Pacote;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.*;
import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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
            String mensagemFinal = entrada.readObject().toString();
            System.out.println(mensagemFinal);
            conexaoCliente.close();
        }
    }
}