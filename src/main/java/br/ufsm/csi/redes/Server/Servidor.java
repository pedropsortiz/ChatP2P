package br.ufsm.csi.redes.Server;
import java.io.IOException;
import java.net.InetAddress;

public class Servidor {

    private final int porta = 8080;
    private final InetAddress endereco = InetAddress.getByName("localhost");
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_START = "\u001B[32m";

    public static void main(String[] args) throws IOException {
        new Servidor();
    }

    public Servidor() throws IOException {
        System.out.println(ANSI_START + "Iniciando broadcasting para o endereço " + endereco + ANSI_RESET);
        System.out.println(ANSI_START + "Escutando porta " + porta + ANSI_RESET + "\n");
        new Thread(new MensagemRadar()).start();
        new Thread(new MensagemEnvio()).start();

    }
}