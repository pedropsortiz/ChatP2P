package br.ufsm.csi.redes.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Mensagem implements Serializable {

    private String mensagemTexto;
    private Usuario remetente;
    private Usuario destinatario;

    public String mensagem(){
        String mensagem = remetente.getNome() + ": " + mensagemTexto + "\n";
        return mensagem;
    }

}
