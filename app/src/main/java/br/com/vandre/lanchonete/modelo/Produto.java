package br.com.vandre.lanchonete.modelo;

import java.io.Serializable;

public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long codigo;
    private String produto;
    private double preco;
    private String ingredientes;
    private String ingredientesJson;
    private String imagem;
    private boolean ativarScroll;

    public Produto() {
        super();
    }

    public Produto(Long codigo, String produto, String ingredientes, String imagem, String ingredientesJson) {
        this.codigo = codigo;
        this.produto = produto;
        this.ingredientes = ingredientes;
        this.imagem = imagem;
        this.ingredientesJson = ingredientesJson;

    }

    public Produto(Long codigo, String produto, Double preco, String ingredientes, String imagem, String ingredientesJson) {
        this.codigo = codigo;
        this.produto = produto;
        this.preco = preco;
        this.ingredientes = ingredientes;
        this.ingredientesJson = ingredientesJson;
        this.imagem = imagem;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getIngredientes() {
        return ingredientes == null ? "" : ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getImagem() {
        return imagem == null ? "" : imagem.replace("https://", "http://");
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public boolean isAtivarScroll() {
        return ativarScroll;
    }

    public void setAtivarScroll(boolean isAtivarScroll) {
        this.ativarScroll = isAtivarScroll;
    }


    public String getIngredientesJson() {
        return ingredientesJson == null ? "" : ingredientesJson;
    }

    public void setIngredientesJson(String ingredientesJson) {
        this.ingredientesJson = ingredientesJson;
    }

}