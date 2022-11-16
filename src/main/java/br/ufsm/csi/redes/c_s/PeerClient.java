package br.ufsm.csi.redes.c_s;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class PeerClient {

    private static final int porta = 8081;
    Socket conexao;

    public PeerClient(InetAddress endereco) throws IOException {
        conexao = new Socket(endereco, porta);
    }

}
