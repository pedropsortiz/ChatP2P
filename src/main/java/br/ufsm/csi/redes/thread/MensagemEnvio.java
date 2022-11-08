package br.ufsm.csi.redes.thread;

import br.ufsm.csi.redes.model.*;
import br.ufsm.csi.redes.swing.ChatClientSwing;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.*;

import static br.ufsm.csi.redes.model.Usuario.StatusUsuario.DISPONIVEL;

public class MensagemEnvio implements Runnable{

    private final static int porta = 8080;
    private ChatClientSwing janela;
    private final static InetAddress endereco;
    private final static int tempoDormir= 5000; //5 segundos

    static {
        try {
            endereco = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
    public MensagemEnvio(ChatClientSwing janela) throws IOException {
        this.janela = janela;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true){
            Usuario usuario = new Usuario(janela.retornarNomeUsuario(), janela.retornarStatusUsuario(), endereco, System.currentTimeMillis());
            Mensagem mensagem = Mensagem.builder().tipoMensagem( Mensagem.TipoMensagem.SONDA).usuario(usuario).build();
            String StringMensagem =  new ObjectMapper().writeValueAsString(mensagem);
            byte[] bArray = StringMensagem.getBytes("UTF-8");

            DatagramSocket conexao = new DatagramSocket();
            conexao.setBroadcast(true);
            DatagramPacket datagramPacket = new DatagramPacket(
                    bArray,
                    0,
                    bArray.length,
                    endereco,
                    porta
            );

            conexao.send(datagramPacket);
            conexao.close();
            Thread.sleep(tempoDormir);
        }
    }
}
