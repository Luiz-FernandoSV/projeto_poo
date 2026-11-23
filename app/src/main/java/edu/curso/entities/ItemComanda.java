package edu.curso.entities;

public class ItemComanda {

    private long idItemComanda;
    private long idComanda;
    private Produto produto;
    private int quantidade;
    private double precoUnitario;

    public ItemComanda() {
    }

    public long getIdItemComanda() {
        return idItemComanda;
    }

    public void setIdItemComanda(long idItemComanda) {
        this.idItemComanda = idItemComanda;
    }

    public long getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(long idComanda) {
        this.idComanda = idComanda;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}