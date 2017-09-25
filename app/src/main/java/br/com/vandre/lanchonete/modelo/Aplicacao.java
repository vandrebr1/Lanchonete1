package br.com.vandre.lanchonete.modelo;

import java.io.Serializable;

public class Aplicacao implements Serializable {

    private static final long serialVersionUID = 1L;

    private long codigo;
    private String servidor;

    public Aplicacao() {
        super();
    }

    public Aplicacao(long codigo, String servidor) {
        this.codigo = codigo;
        this.servidor = servidor;
    }

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

}




