package br.ufsm.csi.redes.Server;

import br.ufsm.csi.redes.Model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
    List<Usuario> listaUsuariosConectados = new ArrayList<Usuario>();

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

            //Conversão do byte recebido para Mensagem
            String stringMensagem = new String(pacoteDetectado.getData(), StandardCharsets.UTF_8);
            Mensagem objMensagem = new ObjectMapper().readValue(stringMensagem, Mensagem.class);

            //Ignorando mensagens que são recebidas de localhost
            // # Adicionar !
            if ((objMensagem.getUsuario().getEndereco().equals(InetAddress.getByName("localhost")))){
                Usuario usuario = objMensagem.getUsuario();
                if (!(listaUsuariosConectados.contains(usuario))){
                    listaUsuariosConectados.add(usuario);
                    System.out.println("Mensagem Detectada!\nTipo da mensagem: " + objMensagem.getTipoMensagem() + "\nUsuário: " + usuario + "\nEndereço: "+ usuario.getEndereco() + "\n");
                } else {
                    System.out.println(listaUsuariosConectados);
                }
            }
        }
    }
}
