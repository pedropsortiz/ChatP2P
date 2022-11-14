package br.ufsm.csi.redes.server;
import br.ufsm.csi.redes.gui.ChatClientSwing;
import br.ufsm.csi.redes.c_s.ServidorThread;
import br.ufsm.csi.redes.packageThread.*;

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

        //Iniciando janela da aplicação
        ChatClientSwing janela = new ChatClientSwing();

        System.out.println(ANSI_START + "Iniciando broadcasting para o endereço " + endereco + ANSI_RESET);
        System.out.println(ANSI_START + "Escutando porta " + porta + ANSI_RESET + "\n");
        new ServidorThread();
        new Thread(new MensagemRadar(janela)).start();
        new Thread(new MensagemEnvio(janela)).start();
        new Thread(new UsuarioCheca(janela)).start();
    }
}
