package br.com.vandre.lanchonete.modelo;

import java.io.Serializable;

public class Ingrediente implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long codigo;
    private String produto;
    private double preco;
    private String imagem;

    public Ingrediente() {
        super();
    }

    public Ingrediente(Long codigo, String produto, double preco, String imagem) {
        this.codigo = codigo;
        this.produto = produto;
        this.preco = preco;
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

    public String getImagem() {
        return imagem == null ? "" : imagem.replace("https://","http://");
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

}