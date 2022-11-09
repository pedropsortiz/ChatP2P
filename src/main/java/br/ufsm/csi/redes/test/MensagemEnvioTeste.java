package br.ufsm.csi.redes.test;

import br.ufsm.csi.redes.model.Pacote;
import br.ufsm.csi.redes.model.Usuario;
import br.ufsm.csi.redes.swing.ChatClientSwing;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static br.ufsm.csi.redes.model.Usuario.StatusUsuario.*;

public class MensagemEnvioTeste implements Runnable {
    private final static int porta = 8080;
    private ChatClientSwing janela;
    private final static InetAddress endereco;
    private final static int tempoDormir= 5000; //5 segundos

    static {
        try {
            endereco = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
    public MensagemEnvioTeste(ChatClientSwing janela) throws IOException {
        this.janela = janela;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true){
            Usuario usuario;
            Pacote pacote;
            String StringMensagem;
            byte[] bArray;
            DatagramSocket conexao = new DatagramSocket();
            conexao.setBroadcast(true);
            DatagramPacket datagramPacket;

            usuario = new Usuario("Teste1", DISPONIVEL, endereco, System.currentTimeMillis());
            pacote = Pacote.builder().tipoMensagem( Pacote.TipoPacote.SONDA).usuario(usuario).build();
            StringMensagem =  new ObjectMapper().writeValueAsString(pacote);
            bArray = StringMensagem.getBytes("UTF-8");

            conexao = new DatagramSocket();
            conexao.setBroadcast(true);
            datagramPacket = new DatagramPacket(
                    bArray,
                    0,
                    bArray.length,
                    endereco,
                    porta
            );

            conexao.send(datagramPacket);
            conexao.close();

            usuario = new Usuario("Teste2", NAO_PERTURBE, endereco, System.currentTimeMillis());
            pacote = Pacote.builder().tipoMensagem( Pacote.TipoPacote.SONDA).usuario(usuario).build();
            StringMensagem =  new ObjectMapper().writeValueAsString(pacote);
            bArray = StringMensagem.getBytes("UTF-8");

            conexao = new DatagramSocket();
            conexao.setBroadcast(true);
            datagramPacket = new DatagramPacket(
                    bArray,
                    0,
                    bArray.length,
                    endereco,
                    porta
            );

            conexao.send(datagramPacket);
            conexao.close();

            usuario = new Usuario("Teste3", VOLTO_LOGO, endereco, System.currentTimeMillis());
            pacote = Pacote.builder().tipoMensagem( Pacote.TipoPacote.SONDA).usuario(usuario).build();
            StringMensagem =  new ObjectMapper().writeValueAsString(pacote);
            bArray = StringMensagem.getBytes("UTF-8");

            conexao = new DatagramSocket();
            conexao.setBroadcast(true);
            datagramPacket = new DatagramPacket(
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
