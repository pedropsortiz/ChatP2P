package br.ufsm.csi.redes.thread;

import br.ufsm.csi.redes.model.Usuario;
import br.ufsm.csi.redes.swing.ChatClientSwing;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import javax.swing.*;


@AllArgsConstructor
public class ListaChecar implements Runnable{

    public ChatClientSwing janela;
    private final static int tempoDormir = 15000; //15 segundos

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            DefaultListModel listaUsuarios = janela.retornarListaUsuarios();

            for (int i = 0; i < listaUsuarios.getSize(); i++){
                Usuario usuario = (Usuario) listaUsuarios.getElementAt(i);
                Long tempoAtual = System.currentTimeMillis();
                Long tempoEmSegundos = ((tempoAtual - usuario.getUltimoAcesso()) / 1000)+1;
                if (tempoEmSegundos >= 30){
                    janela.removeUsuario(usuario);
                }
            }

            Thread.sleep(tempoDormir);
        }
    }
}
