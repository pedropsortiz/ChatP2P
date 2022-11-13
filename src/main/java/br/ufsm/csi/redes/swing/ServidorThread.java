package br.ufsm.csi.redes.swing;

import br.ufsm.csi.redes.swing.ChatClientSwing;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

@Setter
@Getter
public class ServidorThread {

    static final int porta = 8081;
    private ChatClientSwing janela;
    private ServerSocket servidor;
    private Socket conexao;
    public Boolean parar = false;
    private ObjectInputStream  entrada;
    private OutputStream saida;

    public ServidorThread(InetAddress endereco) throws IOException {
        this.servidor = new ServerSocket(this.porta, 50, endereco);
        (new EsperaMensagem()).start();
    }

    public void stop() throws Exception{
        parar = true;
        conexao.close();
        servidor.close();
    }

    private boolean ouve() throws Exception {

        conexao = servidor.accept();

        entrada = new ObjectInputStream(conexao.getInputStream());
        String mensagemFinal = entrada.readObject().toString();

        ChatClientSwing.PainelChatPVT.addMensagem(mensagemFinal);
        return true;
    }

    public class EsperaMensagem extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                while(!parar){
                    ouve();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}