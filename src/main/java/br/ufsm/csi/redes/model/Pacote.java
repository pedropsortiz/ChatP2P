package br.ufsm.csi.redes.model;
import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Pacote implements Serializable {

    private TipoPacote tipoMensagem;
    private Usuario usuario;
    public enum TipoPacote { SONDA }

}
