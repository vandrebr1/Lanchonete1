package br.com.vandre.lanchonete.modelo;

import java.io.Serializable;

public class PedidoItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String produto;
    private Long produtoID;
    private double quantidade;
    private String ingredientes;
    private String ingredientesJson;
    private double preco;
    private double precopromocao;

    public PedidoItem() {
        super();
    }

    public PedidoItem(Long produtoID, String produto, double quantidade, String ingredientes, double preco, String ingredientesJson) {
        this.produtoID = produtoID;
        this.produto = produto;
        this.quantidade = quantidade;
        this.ingredientes = ingredientes;
        this.preco = preco;
        this.ingredientesJson = ingredientesJson;

    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }


    public Long getProdutoID() {
        return produtoID;
    }

    public void setProdutoID(Long produtoID) {
        this.produtoID = produtoID;
    }


    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getPrecoPromocao() {
        return precopromocao;
    }

    public void setPrecoPromocao(double precopromocao) {
        this.precopromocao = precopromocao;
    }

    public String getIngredientesJson() {
        return ingredientesJson;
    }

    public void setIngredientesJson(String ingredientesJson) {
        this.ingredientesJson = ingredientesJson;
    }


}
