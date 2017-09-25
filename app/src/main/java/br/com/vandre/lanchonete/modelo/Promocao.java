package br.com.vandre.lanchonete.modelo;

import java.io.Serializable;

public class Promocao implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long codigo;
    private String nome;
    private String descricao;

    public Promocao() {
        super();
    }


    public Promocao(Long codigo, String nome, String descricao) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}