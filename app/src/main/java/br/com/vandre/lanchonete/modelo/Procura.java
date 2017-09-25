package br.com.vandre.lanchonete.modelo;

import java.io.Serializable;

public class Procura implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String tipo;
	private String campo;
	private boolean ocultarProdSemEstoque;
	private boolean exibirPrimeiroProdutosPromocao;

	public Procura() {
		super();
	}

	public Procura(String nome, String tipo, String campo,
                   boolean ocultarProdSemEstoque, boolean exibirPrimeiroProdutosPromocao) {
		super();
		this.nome = nome;
		this.tipo = tipo;
		this.campo = campo;
		this.ocultarProdSemEstoque = ocultarProdSemEstoque;
		this.exibirPrimeiroProdutosPromocao = exibirPrimeiroProdutosPromocao;
	}

	public String getNome() {
		return nome == null ? "" : nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo == null ? "" : tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCampo() {
		return campo == null ? "" : campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public boolean getOcultarProdSemEstoque() {
		return ocultarProdSemEstoque;
	}

	public void setOcultarProdSemEstoque(boolean ocultarProdSemEstoque) {
		this.ocultarProdSemEstoque = ocultarProdSemEstoque;
	}
	
	public boolean getExibirPrimeiroProdutosPromocao() {
		return exibirPrimeiroProdutosPromocao;
	}

	public void setExibirPrimeiroProdutosPromocao(boolean exibirPrimeiroProdutosPromocao) {
		this.exibirPrimeiroProdutosPromocao = exibirPrimeiroProdutosPromocao;
	}
	
}
