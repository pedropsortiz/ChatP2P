package br.ufsm.csi.redes.test;
import br.ufsm.csi.redes.swing.ChatClientSwing;

import java.io.IOException;


public class AppTeste {

    public static void main(String[] args) throws IOException {
        new AppTeste();
    }

    public AppTeste() throws IOException {
        ChatClientSwing janela = new ChatClientSwing();
        new Thread(new MensagemTeste(janela)).start();
    }
}
