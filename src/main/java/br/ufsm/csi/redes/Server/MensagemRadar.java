package br.ufsm.csi.redes.Server;

import br.ufsm.csi.redes.Interface.ChatClientSwing;
import br.ufsm.csi.redes.Model.Mensagem;
import br.ufsm.csi.redes.Model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Array;
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
            conexao.setBroadcast(true);
            byte[] buffer = new byte[conexao.getReceiveBufferSize()];
            DatagramPacket pacoteDetectado = new DatagramPacket(buffer, buffer.length);
            conexao.receive(pacoteDetectado);
            InetAddress enderecoCliente = pacoteDetectado.getAddress();

            //Conversão do byte recebido para Mensagem
            String stringMensagem = new String(pacoteDetectado.getData(), StandardCharsets.UTF_8);
            Mensagem objMensagem = new ObjectMapper().readValue(stringMensagem, Mensagem.class);

            //Ignorando mensagens que são recebidas de localhost
            // # Adicionar !
            if ((objMensagem.getUsuario().getEndereco().equals(InetAddress.getByName("localhost")))){
                Usuario usuario = objMensagem.getUsuario();
                System.out.println("Mensagem Detectada!\nTipo da mensagem: " + objMensagem.getTipoMensagem() + "\nUsuário: " + usuario + "\nEndereço: "+ usuario.getEndereco() + "\n");
            }
        }
    }
}
