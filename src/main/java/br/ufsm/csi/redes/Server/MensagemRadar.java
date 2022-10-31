package br.ufsm.csi.redes.Server;

import br.ufsm.csi.redes.Interface.ChatClientSwing;
import br.ufsm.csi.redes.Model.Mensagem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class MensagemRadar implements Runnable{

    private final static int porta = 8080;
    private final static InetAddress endereco;

    static {
        try {
            endereco = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    DatagramSocket conexao = new DatagramSocket(porta, endereco);

    public MensagemRadar() throws IOException {
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true){
            byte[] buffer = new byte[100];
            DatagramPacket pacoteDetectado = new DatagramPacket(buffer, buffer.length);
            conexao.setBroadcast(true);
            conexao.receive(pacoteDetectado);

//          ObjectMapper objectMapper = new ObjectMapper();
            String stringMensagem = new String(pacoteDetectado.getData(), StandardCharsets.UTF_8);
            System.out.println("Mensagem Detectada! | " + stringMensagem);

        }
    }
}


