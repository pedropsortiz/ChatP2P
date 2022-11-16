package br.ufsm.csi.redes.thread;

        import br.ufsm.csi.redes.model.*;
        import br.ufsm.csi.redes.view.ChatClientSwing;
        import com.fasterxml.jackson.databind.ObjectMapper;
        import lombok.SneakyThrows;

        import java.io.IOException;
        import java.net.*;

public class MensagemEnvio implements Runnable{

    private final static int porta = 8080;
    private ChatClientSwing janela;
    private final static InetAddress endereco;
    private final static int tempoDormir= 5000; //5 segundos

    static {
        try {
//            endereco = InetAddress.getByName("192.168.81.151");
              endereco = InetAddress.getByName("192.168.81.161");
//            endereco = InetAddress.getByName("255.255.255.255");
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
            Usuario usuario = new Usuario(janela.retornarNomeUsuario(), janela.retornarStatusUsuario(), InetAddress.getLocalHost(), System.currentTimeMillis());
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

