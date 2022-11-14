package br.ufsm.csi.redes.c_s;

import br.ufsm.csi.redes.gui.ChatClientSwing;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
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
    private ObjectOutputStream saida;

    public ServidorThread() throws IOException {
        try {
            this.servidor = new ServerSocket(this.porta);
            new EsperaMensagem().start();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void stop() throws Exception{
        parar = true;
        conexao.close();
        servidor.close();
        System.out.println("Conexão do servidor encerrada!");
    }

    private void ouve(){
        try {
            conexao = servidor.accept();

            entrada = new ObjectInputStream(conexao.getInputStream());
            String mensagemFinal = entrada.readObject().toString();

            ChatClientSwing.PainelChatPVT.addMensagem(mensagemFinal);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public class EsperaMensagem extends Thread {
        @Override
        public void run() {
            while(!parar){
                try {
                    ouve();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}