package br.ufsm.csi.redes.Model;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Mensagem implements Serializable {

    private TipoMensagem tipoMensagem;
    private Usuario usuario;
    public enum TipoMensagem { SONDA }

}
