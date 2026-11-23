package edu.curso.controllers;

import edu.curso.DAO.ProdutoDAOImplementation;
import edu.curso.DAO.ProdutoDAOInterface;
import edu.curso.entities.Produto;
import edu.curso.enums.ECategoria;
import edu.curso.enums.EStatus;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProdutoController {

    private LongProperty idProperty = new SimpleLongProperty(0);
    private StringProperty nomeProperty = new SimpleStringProperty("");
    private StringProperty descricaoProperty = new SimpleStringProperty("");
    private DoubleProperty precoProperty = new SimpleDoubleProperty(0);
    private ObjectProperty<ECategoria> categoriaProperty = new SimpleObjectProperty(ECategoria.Nenhum);
    private ObjectProperty<EStatus> disponivelProperty =
        new SimpleObjectProperty<>(EStatus.Disponivel);

    private ObservableList<Produto> lista = FXCollections.observableArrayList();

    private ProdutoDAOInterface dao = new ProdutoDAOImplementation();

    public ProdutoController(){
        pesquisarTodos();
    }

    public ObservableList<Produto> getLista() { 
        return lista;
    }

    public void pesquisarTodos() { 
        lista.clear();
        lista.addAll( dao.buscarProdutos() );
    }

    

    public boolean salvar() {
        Produto p = new Produto();

        p.setNome(nomeProperty.get());
        p.setDescricao(descricaoProperty.get());
        p.setPreco(precoProperty.get());
        p.setCategoria(categoriaProperty.get());
        p.setDisponivel(disponivelProperty.get());

        if (idProperty.get() == 0) {
            p = dao.salvarProduto(p);
        } else {
            p = dao.editarProduto(idProperty.get(), p);
        }

        if (p != null) {
            limparCampos();
            pesquisarTodos();
            return true;
        }
        pesquisarTodos();
        return false;
    }

    public ObservableList<Produto> buscarProdutos() {
        return FXCollections.observableArrayList(
                dao.buscarProdutos()
        );
    }

    public boolean excluir() {
        if (idProperty.get() != 0) {
            if(dao.excluirProduto(idProperty.get())){
                limparCampos();
                pesquisarTodos();
                return true;
            }else{
                limparCampos();
                return false;
            }
        } else {
            return false;
        }
    }

    public void limparCampos() {
        idProperty.set(0);
        nomeProperty.set("");
        descricaoProperty.set("");
        precoProperty.set(0);
        categoriaProperty.set(ECategoria.Nenhum);
        disponivelProperty.set(EStatus.Disponivel);
    }

    public void carregarProduto(Produto p) {
        if (p != null) {
            idProperty.set(p.getIdProduto());
            nomeProperty.set(p.getNome());
            descricaoProperty.set(p.getDescricao());
            precoProperty.set(p.getPreco());
            categoriaProperty.set(p.getCategoria());
            disponivelProperty.set(p.getDisponivel());
        }
    }

    public LongProperty idProperty() {
        return idProperty;
    }

    public StringProperty nomeProperty() {
        return nomeProperty;
    }

    public StringProperty descricaoProperty() {
        return descricaoProperty;
    }

    public DoubleProperty precoProperty() {
        return precoProperty;
    }

    public ObjectProperty<ECategoria> categoriaProperty() {
        return categoriaProperty;
    }

    public ObjectProperty<EStatus> disponivelProperty() {
        return disponivelProperty;
    }

}
