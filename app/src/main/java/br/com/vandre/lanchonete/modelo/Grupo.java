package br.com.vandre.lanchonete.modelo;


public class Grupo {

    private static final long serialVersionUID = 1L;

    private String grupo;

    public Grupo() {
        super();
    }

    public Grupo(String grupo) {
        this.grupo = grupo;
    }

    public String getGrupo() {
        return grupo == null ? "" : grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

}
