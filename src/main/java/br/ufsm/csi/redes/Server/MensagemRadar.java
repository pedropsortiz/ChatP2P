package br.ufsm.csi.redes.Server;

import br.ufsm.csi.redes.Interface.ChatClientSwing;
import br.ufsm.csi.redes.Model.Mensagem;
import br.ufsm.csi.redes.Model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.io.IOException;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MensagemRadar implements Runnable{

    private final static int porta = 5000;

    ServerSocket conexao = new ServerSocket(porta);
    Socket conexaoCliente;

    public MensagemRadar() throws IOException {
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true){
            conexaoCliente = conexao.accept();
            ObjectInputStream entrada = new ObjectInputStream(conexaoCliente.getInputStream());
            System.out.print("\nPacote recebido do cliente: " +  entrada);
        }
    }
}
