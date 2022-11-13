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
        System.out.println("Nova conexão realiza para o endereço " + endereco + " e a porta " + porta);
        (new EsperaMensagem()).start();
    }

    public void stop() throws Exception{
        parar = true;
        conexao.close();
        servidor.close();
        System.out.println("Conexão do servidor encerrada!");
    }

    private boolean ouve() throws Exception {
        try {
            if (!parar){
                conexao = servidor.accept();

                entrada = new ObjectInputStream(conexao.getInputStream());
                String mensagemFinal = entrada.readObject().toString();

                ChatClientSwing.PainelChatPVT.addMensagem(mensagemFinal);
                return true;
            }
        }catch (Exception e){
            System.out.println(e);
        }

        return false;
    }

    public class EsperaMensagem extends Thread {
        @Override
        public void run() {
            try {
                while(!parar){
                    System.out.println("ainda to tentando");
                    ouve();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}