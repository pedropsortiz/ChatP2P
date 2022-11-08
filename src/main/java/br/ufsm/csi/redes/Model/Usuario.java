package br.ufsm.csi.redes.Model;
import lombok.*;
import java.net.InetAddress;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    private String nome;
    private StatusUsuario status;
    private InetAddress endereco;

    public enum StatusUsuario {
        DISPONIVEL, NAO_PERTURBE, VOLTO_LOGO
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;

        return nome.equals(usuario.getNome());

    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }

    public String toString() {
        return this.getNome() + " (" + getStatus().toString() + ")";
    }


}
