package br.ufsm.csi.redes.test;

import br.ufsm.csi.redes.model.Pacote;
import br.ufsm.csi.redes.model.Usuario;
import br.ufsm.csi.redes.swing.ChatClientSwing;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

@AllArgsConstructor
public class MensagemTeste implements Runnable{

    private final static int porta = 8080;
    private ChatClientSwing janela;
    private final static InetAddress endereco;

    static {
        try {
            endereco = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
    private final static int tempoDormir= 5000; //5 segundos

    @SneakyThrows
    @Override
    public void run() {
        while (true){
            Usuario usuario = new Usuario(janela.retornarNomeUsuario(), janela.retornarStatusUsuario(), endereco, System.currentTimeMillis());
            Pacote pacote = Pacote.builder().tipoMensagem( Pacote.TipoPacote.SONDA).usuario(usuario).build();
            String StringMensagem =  new ObjectMapper().writeValueAsString(pacote);
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
