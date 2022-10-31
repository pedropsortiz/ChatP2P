package br.ufsm.csi.redes.Server;
import java.io.IOException;

public class Servidor {

    private final int porta = 5000;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_START = "\u001B[32m";

    public static void main(String[] args) throws IOException {
        new Servidor();
    }

    public Servidor() throws IOException {
        System.out.println(ANSI_START + "Escutando porta " + porta + ANSI_RESET);
        new Thread(new MensagemRadar()).start();
        new Thread(new MensagemEnvio()).start();

    }
}
