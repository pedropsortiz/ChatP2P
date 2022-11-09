package br.ufsm.csi.redes.thread;

import br.ufsm.csi.redes.swing.ChatClientSwing;
import br.ufsm.csi.redes.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class MensagemRadar implements Runnable{

    private ChatClientSwing janela;
    private final static int porta = 8080;
    private final static InetAddress endereco;

    static {
        try {
            endereco = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public MensagemRadar(ChatClientSwing janela) throws IOException {
        this.janela = janela;
    }

    DatagramSocket conexao = new DatagramSocket(porta, endereco);

    @SneakyThrows
    @Override
    public void run() {
        while (true){
            conexao.setBroadcast(true);
            byte[] buffer = new byte[conexao.getReceiveBufferSize()];
            DatagramPacket pacoteDetectado = new DatagramPacket(buffer, buffer.length);
            conexao.receive(pacoteDetectado);

            // Conversão do byte recebido para Pacote
            String stringMensagem = new String(pacoteDetectado.getData(), StandardCharsets.UTF_8);
            Pacote objPacote = new ObjectMapper().readValue(stringMensagem, Pacote.class);

            // Ignorando mensagens que são recebidas de localhost
            //TODO: Descomentar barramento de pacotes do localhost
            //if (!(objPacote.getUsuario().getEndereco().equals(InetAddress.getLocalHost()))){
                Usuario usuario = objPacote.getUsuario();
                usuario.setUltimoAcesso(System.currentTimeMillis());
                if (!(janela.retornarListaUsuarios().contains(usuario))){
                    janela.adicionaUsuario(usuario);
                    //Estabelecer conexão com o TCP nesse ponto
                    System.out.println("Pacote Detectada!\nTipo da mensagem: " + objPacote.getTipoMensagem() + "\nUsuário: " + usuario + "\nEndereço: "+ usuario.getEndereco() + "\n");
                } else{
                    janela.atualizarUsuario(usuario);
                    System.out.println("Usuário atualizado com sucesso!\nStatus atualizado: " + usuario.getStatus() + "\nTempo atual da última conexão: " + (usuario.getUltimoAcesso() / 1000) + "\n");
                }
            //}
        }
    }
}
