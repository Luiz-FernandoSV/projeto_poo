package edu.curso.DAO;

import java.util.List;

import edu.curso.entities.Produto;

public interface ProdutoDAOInterface{
    public Produto salvarProduto(Produto produto);
    public boolean excluirProduto(long id);
    public Produto editarProduto(long id,Produto produto);
    public List<Produto> buscarProdutos();
}