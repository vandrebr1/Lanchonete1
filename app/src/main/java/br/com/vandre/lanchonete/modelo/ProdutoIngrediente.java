package br.com.vandre.lanchonete.modelo;

import java.io.Serializable;

public class ProdutoIngrediente implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long codigo;
    private Long produtoID;
    private Long ingredienteID;

    public ProdutoIngrediente() {
        super();
    }

    public ProdutoIngrediente(Long codigo, Long produtoID, Long ingredienteID) {
        this.codigo = codigo;
        this.produtoID = produtoID;
        this.ingredienteID = ingredienteID;

    }

    public ProdutoIngrediente(Long produtoID, Long ingredienteID) {
        this.produtoID = produtoID;
        this.ingredienteID = ingredienteID;

    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Long getProdutoID() {
        return produtoID;
    }

    public void setProdutoID(Long produtoID) {
        this.produtoID = produtoID;
    }

    public Long getIngredienteID() {
        return ingredienteID;
    }

    public void setIngredienteID(Long ingredienteID) {
        this.ingredienteID = ingredienteID;
    }
}