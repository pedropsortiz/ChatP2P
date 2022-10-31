package br.ufsm.csi.redes.Server;

import br.ufsm.csi.redes.Interface.ChatClientSwing;
import br.ufsm.csi.redes.Model.Mensagem;
import br.ufsm.csi.redes.Model.Usuario;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.*;
import java.net.*;
import java.util.Scanner;

import static br.ufsm.csi.redes.Model.Usuario.StatusUsuario.DISPONIVEL;

public class MensagemEnvio implements Runnable{

    private final static int porta = 5000;
    Usuario usuario = new Usuario("Pedro", ChatClientSwing.StatusUsuario.DISPONIVEL, InetAddress.getLocalHost());
    BufferedReader mensagemServidor;
    Mensagem mensagem = Mensagem.builder().tipoMensagem( Mensagem.TipoMensagem.SONDA).usuario(usuario).build();
    String StringMensagem =  new ObjectMapper().writeValueAsString(mensagem);
    byte[] bArray = StringMensagem.getBytes("UTF-8");
    public MensagemEnvio() throws IOException {
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true){
            Socket conexao = new Socket(InetAddress.getLocalHost(), porta);

            ObjectOutputStream saida = new ObjectOutputStream(conexao.getOutputStream());
            DatagramPacket datagramPacket = new DatagramPacket(
                    bArray,
                    0,
                    bArray.length,
                    InetAddress.getByName("localhost"),
                    5000
            );

            saida.write(datagramPacket.getData());
            saida.flush();

            saida.close();
            conexao.close();
            Thread.sleep(5000);
        }
    }
}
