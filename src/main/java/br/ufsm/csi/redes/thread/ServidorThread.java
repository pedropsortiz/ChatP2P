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

            // Conversão do byte recebido para Pacote
//            String stringMensagem = new String(entrada.read());
//            Pacote objPacote = new ObjectMapper().readValue(stringMensagem, Pacote.class);

//            System.out.println(msg.getUsuario().getNome() + " > " + msg.getMensagemTexto());
            System.out.println(entrada.readObject());
            conexaoCliente.close();
        }
    }
}