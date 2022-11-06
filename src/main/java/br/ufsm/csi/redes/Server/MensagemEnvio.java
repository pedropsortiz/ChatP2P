package br.ufsm.csi.redes.Server;

import br.ufsm.csi.redes.Interface.ChatClientSwing;
import br.ufsm.csi.redes.Model.Mensagem;
import br.ufsm.csi.redes.Model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.*;

import static br.ufsm.csi.redes.Model.Usuario.StatusUsuario.DISPONIVEL;

public class MensagemEnvio implements Runnable{

    private final static int porta = 8080;
    private final static InetAddress endereco;
    private final static int tempoDormir= 5000; //5 segundos

    static {
        try {
            endereco = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    Usuario usuario = new Usuario("Pedro", DISPONIVEL, endereco);
    Mensagem mensagem = Mensagem.builder().tipoMensagem( Mensagem.TipoMensagem.SONDA).usuario(usuario).build();
    String StringMensagem =  new ObjectMapper().writeValueAsString(mensagem);
    byte[] bArray = StringMensagem.getBytes("UTF-8");
    public MensagemEnvio() throws IOException {
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true){
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
