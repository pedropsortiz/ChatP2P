package br.ufsm.csi.redes.Server;
import java.io.IOException;

public class Servidor {

    public static void main(String[] args) throws IOException {
        new Servidor();
    }

    public Servidor() throws IOException {
        new Thread(new MensagemRadar()).start();
    }
}
