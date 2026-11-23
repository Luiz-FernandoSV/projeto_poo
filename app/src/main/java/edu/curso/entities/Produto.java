
package edu.curso.entities;

import edu.curso.enums.ECategoria;
import edu.curso.enums.EStatus;

public class Produto {
    private long idProduto;
    private String nome;
    private String descricao;
    private double preco;
    private ECategoria categoria;
    private EStatus disponivel;

    public Produto() {}

    public long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(long idProduto) {
        this.idProduto = idProduto;
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

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public ECategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(ECategoria categoria) {
        this.categoria = categoria;
    }

    public EStatus getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(EStatus disponivel) {
        this.disponivel = disponivel;
    }

    @Override
    public String toString(){
        return "ID: "+this.idProduto +
                "| Nome: "+this.nome +
                "| Descricao: " + this.descricao +
                "| Preco: "+this.preco +
                "| Categoria: "+this.categoria.toString() +
                "| Disponibilidade: "+this.disponivel.toString();
    }
}
