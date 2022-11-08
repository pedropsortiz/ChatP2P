package br.ufsm.csi.redes.thread;

import br.ufsm.csi.redes.swing.ChatClientSwing;
import br.ufsm.csi.redes.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MensagemRadar implements Runnable{

    private ChatClientSwing janela;
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

    public MensagemRadar(ChatClientSwing janela) throws IOException {
        this.janela = janela;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true){
            conexao.setBroadcast(true);
            byte[] buffer = new byte[conexao.getReceiveBufferSize()];
            DatagramPacket pacoteDetectado = new DatagramPacket(buffer, buffer.length);
            conexao.receive(pacoteDetectado);

            // Conversão do byte recebido para Mensagem
            String stringMensagem = new String(pacoteDetectado.getData(), StandardCharsets.UTF_8);
            Mensagem objMensagem = new ObjectMapper().readValue(stringMensagem, Mensagem.class);

            // Ignorando mensagens que são recebidas de localhost
            // # Adicionar !
            if ((objMensagem.getUsuario().getEndereco().equals(InetAddress.getByName("localhost")))){
                Usuario usuario = objMensagem.getUsuario();
                usuario.setUltimoAcesso(System.currentTimeMillis());
                if (!(janela.retornarListaUsuarios().contains(usuario))){
                    janela.adicionaUsuario(usuario);
                    System.out.println("Mensagem Detectada!\nTipo da mensagem: " + objMensagem.getTipoMensagem() + "\nUsuário: " + usuario + "\nEndereço: "+ usuario.getEndereco() + "\n");
                } else{
                    janela.atualizarUsuario(usuario);
                    System.out.println("Usuário atualizado com sucesso!\nStatus atualizado: " + usuario.getStatus() + "\nTempo atual da última conexão: " + (usuario.getUltimoAcesso() / 1000) + "\n");
                }
            }
        }
    }
}
