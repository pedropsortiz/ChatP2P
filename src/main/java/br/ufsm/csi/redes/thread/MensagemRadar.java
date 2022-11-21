package br.ufsm.csi.redes.thread;

import br.ufsm.csi.redes.view.ChatClientSwing;
import br.ufsm.csi.redes.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class MensagemRadar implements Runnable{

    private ChatClientSwing janela;
    private final static int porta = 8080;

    public MensagemRadar(ChatClientSwing janela) throws IOException {
        this.janela = janela;
    }

    DatagramSocket conexao = new DatagramSocket(porta);

    @SneakyThrows
    @Override
    public void run() {
        while (true){
//            conexao.setBroadcast(true);
            byte[] buffer = new byte[conexao.getReceiveBufferSize()];
            DatagramPacket pacoteDetectado = new DatagramPacket(buffer, buffer.length);
            conexao.receive(pacoteDetectado);

            // Conversão do byte recebido para Pacote
            String stringMensagem = new String(pacoteDetectado.getData(), StandardCharsets.UTF_8);
            Pacote objPacote = new ObjectMapper().readValue(stringMensagem, Pacote.class);

            // Ignorando mensagens que são recebidas de localhost
//            if (!(objPacote.getUsuario().getEndereco().equals(InetAddress.getLocalHost()))){
                Usuario usuario = objPacote.getUsuario();
                usuario.setUltimoAcesso(System.currentTimeMillis());
                if (!(janela.retornarListaUsuarios().contains(usuario))){
                    janela.adicionaUsuario(usuario);
                } else{
                    janela.atualizarUsuario(usuario);
                }
//            }
        }
    }
}
