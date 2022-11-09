package br.ufsm.csi.redes.server;
import br.ufsm.csi.redes.swing.ChatClientSwing;
import br.ufsm.csi.redes.test.MensagemEnvioTeste;
import br.ufsm.csi.redes.thread.ListaChecar;
import br.ufsm.csi.redes.thread.MensagemEnvio;
import br.ufsm.csi.redes.thread.MensagemRadar;

import java.io.IOException;
import java.net.InetAddress;


public class App {

    private final int porta = 8080;
    private final InetAddress endereco = InetAddress.getLocalHost();
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_START = "\u001B[32m";

    public static void main(String[] args) throws IOException {
        new App();
    }

    public App() throws IOException {
        System.out.println(ANSI_START + "Iniciando broadcasting para o endereço " + endereco + ANSI_RESET);
        System.out.println(ANSI_START + "Escutando porta " + porta + ANSI_RESET + "\n");
        ChatClientSwing janela = new ChatClientSwing();
        new Thread(new MensagemEnvio(janela)).start();
        new Thread(new MensagemEnvioTeste(janela)).start();
        new Thread(new MensagemRadar(janela)).start();
        new Thread(new ListaChecar(janela)).start();
        new Thread(new Servidor()).start();
    }
}
