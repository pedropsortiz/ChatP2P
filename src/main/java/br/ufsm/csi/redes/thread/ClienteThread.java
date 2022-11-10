package br.ufsm.csi.redes.thread;

import br.ufsm.csi.redes.model.Mensagem;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

@AllArgsConstructor
public class ClienteThread implements Runnable{

    private InetAddress endereco;
    private int porta;

    private Mensagem mensagem;

    @SneakyThrows
    @Override
    public void run() {
        while (true){
//            System.out.println("Nova conexão estabelecida");
            Socket conexao;
            ObjectOutputStream saida;
//            ObjectInputStream entrada;

            //Estabelecendo conexão com o servidor
            conexao = new Socket(endereco, porta);

//            while (mensagem == null){}


            saida = new ObjectOutputStream(conexao.getOutputStream());
//            entrada = new ObjectInputStream(conexao.getInputStream());

            //Enviando a mensagem do cliente para o servidor
            saida.write(1);
            saida.flush();
            Thread.sleep(1000);

//            entrada.close();
//            saida.close();
//            conexao.close();

        }
    }

}