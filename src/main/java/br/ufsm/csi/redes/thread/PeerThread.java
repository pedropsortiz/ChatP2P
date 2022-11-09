package br.ufsm.csi.redes.thread;

import br.ufsm.csi.redes.model.Mensagem;
import br.ufsm.csi.redes.model.Usuario;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PeerThread implements Runnable{
    private Mensagem mensagem;
    private Usuario destinatario;

    @Override
    public void run() {
        while (true) {
            Usuario usuario = mensagem.getUsuario();
            System.out.println("Usuário " + usuario.getNome() + " disse: " + mensagem.getMensagemTexto() + " para o usuário " + destinatario.getNome());
            break;
        }
    }
}
