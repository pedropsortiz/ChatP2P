package br.ufsm.csi.redes.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
public class Mensagem implements Serializable {

    private TipoMensagem tipoMensagem;
    private Usuario usuario;

    public Mensagem(TipoMensagem tipoMensagem, Usuario usuario) {
        this.tipoMensagem = tipoMensagem;
        this.usuario = usuario;
    }

    public TipoMensagem getTipoMensagem() {
        return tipoMensagem;
    }

    public void setTipoMensagem(TipoMensagem tipoMensagem) {
        this.tipoMensagem = tipoMensagem;
    }

    public enum TipoMensagem { SONDA }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


}
