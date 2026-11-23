
package edu.curso.controllers;

import java.time.LocalDateTime;

import edu.curso.DAO.ComandaDAOImplementation;
import edu.curso.DAO.ComandaDAOInterface;
import edu.curso.DAO.ItemComandaDAOImplementation;
import edu.curso.DAO.ItemComandaDAOInterface;
import edu.curso.entities.Comanda;
import edu.curso.entities.ItemComanda;
import edu.curso.entities.Produto;
import edu.curso.enums.EStatus;
import edu.curso.enums.EStatusComanda;
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

public class ComandaController {

    // Propriedades utilizadas para vincular os dados à interface
    private LongProperty idProperty
            = new SimpleLongProperty(0);

    private StringProperty numeroTextoProperty
            = new SimpleStringProperty("");

    private ObjectProperty<LocalDateTime> dataAberturaProperty
            = new SimpleObjectProperty<>(LocalDateTime.now());

    private ObjectProperty<LocalDateTime> dataFechamentoProperty
            = new SimpleObjectProperty<>(null);

    private DoubleProperty valorTotalProperty
            = new SimpleDoubleProperty(0);

    private ObjectProperty<EStatusComanda> statusProperty
            = new SimpleObjectProperty<>(EStatusComanda.ABERTA);

    // Lista de comandas
    private ObservableList<Comanda> lista
            = FXCollections.observableArrayList();

    // Objetos de acesso aos dados
    private ComandaDAOInterface dao
            = new ComandaDAOImplementation();

    private ItemComandaDAOInterface itemComandaDAO
            = new ItemComandaDAOImplementation();

    private ProdutoController produtoController
            = new ProdutoController();

    public ComandaController() {
        pesquisarTodos();
    }

    // Retorna a lista de comandas
    public ObservableList<Comanda> getLista() {
        return lista;
    }

    // Busca todas as comandas cadastradas
    public void pesquisarTodos() {
        lista.clear();
        lista.addAll(dao.buscarComandas());
    }

    // Salva ou edita uma comanda
    public boolean salvar() {

        Comanda c = new Comanda();

        c.setIdComanda(idProperty.get());
        c.setNumero(Long.parseLong(numeroTextoProperty.get()));
        c.setDataAbertura(dataAberturaProperty.get());
        c.setDataFechamento(dataFechamentoProperty.get());
        c.setValorTotal(valorTotalProperty.get());
        c.setStatus(statusProperty.get());

        boolean sucesso;

        if (idProperty.get() == 0) {
            sucesso = dao.salvarComanda(c);
        } else {
            sucesso = dao.editarComanda(c);
        }

        if (sucesso) {
            limparCampos();
            pesquisarTodos();
        }

        return sucesso;
    }

    // Busca as comandas cadastradas
    public ObservableList<Comanda> buscarComandas() {
        return FXCollections.observableArrayList(
                dao.buscarComandas()
        );
    }

    // Exclui uma comanda pelo ID
    public boolean excluir(long idComanda) {

        if (dao.excluirComanda(idComanda)) {
            limparCampos();
            pesquisarTodos();
            return true;
        }

        return false;
    }

    // Limpa as propriedades da comanda
    public void limparCampos() {

        idProperty.set(0);
        numeroTextoProperty.set("");

        dataAberturaProperty.set(LocalDateTime.now());
        dataFechamentoProperty.set(null);

        valorTotalProperty.set(0);
        statusProperty.set(EStatusComanda.ABERTA);
    }

    // Carrega os dados de uma comanda nas propriedades
    public void carregarComanda(Comanda c) {

        if (c != null) {

            idProperty.set(c.getIdComanda());
            numeroTextoProperty.set(String.valueOf(c.getNumero()));

            dataAberturaProperty.set(c.getDataAbertura());
            dataFechamentoProperty.set(c.getDataFechamento());

            valorTotalProperty.set(c.getValorTotal());
            statusProperty.set(c.getStatus());
        }
    }

    // Busca os itens pertencentes a uma comanda
    public ObservableList<ItemComanda> buscarItensDaComanda(long idComanda) {

        return FXCollections.observableArrayList(
                itemComandaDAO.buscarItensDaComanda(idComanda)
        );
    }

    // Busca os produtos disponíveis
    public ObservableList<Produto> buscarProdutos() {

        return FXCollections.observableArrayList(
                produtoController.buscarProdutos().stream()
                        .filter(produto -> produto.getDisponivel() == EStatus.Disponivel)
                        .toList()
        );
    }

    // Adiciona um produto a uma comanda
    public boolean adicionarProduto(
            long idComanda,
            Produto produto,
            int quantidade) {

        if (produto == null || quantidade <= 0) {
            return false;
        }

        // Verifica se a comanda existe e está aberta
        Comanda comanda = dao.buscarComandas()
                .stream()
                .filter(c -> c.getIdComanda() == idComanda)
                .findFirst()
                .orElse(null);

        if (comanda == null
                || comanda.getStatus() != EStatusComanda.ABERTA) {
            return false;
        }

        ItemComanda item = new ItemComanda();

        item.setIdComanda(idComanda);
        item.setProduto(produto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(produto.getPreco());

        boolean sucesso = itemComandaDAO.salvarItemComanda(item);

        if (sucesso) {
            dao.atualizarValorTotal(idComanda);
            pesquisarTodos();
        }

        return sucesso;
    }

    // Fecha uma comanda
    public boolean fecharComanda(long idComanda) {

        Comanda comanda = dao.buscarComandas()
                .stream()
                .filter(c -> c.getIdComanda() == idComanda)
                .findFirst()
                .orElse(null);

        if (comanda == null
                || comanda.getStatus() != EStatusComanda.ABERTA) {
            return false;
        }

        boolean sucesso = dao.fecharComanda(idComanda);

        if (sucesso) {
            pesquisarTodos();
        }

        return sucesso;
    }

    // Exclui um item da comanda
    public void excluirItemComanda(long idItemComanda, long idComanda) {

        itemComandaDAO.excluirItemComanda(idItemComanda);

        dao.atualizarValorTotal(idComanda);

        pesquisarTodos();
    }

    // Retorna a propriedade do ID
    public LongProperty idProperty() {
        return idProperty;
    }

    // Retorna a propriedade do número em formato de texto
    public StringProperty numeroTextoProperty() {
        return numeroTextoProperty;
    }

    // Retorna a propriedade da data de abertura
    public ObjectProperty<LocalDateTime> dataAberturaProperty() {
        return dataAberturaProperty;
    }

    // Retorna a propriedade da data de fechamento
    public ObjectProperty<LocalDateTime> dataFechamentoProperty() {
        return dataFechamentoProperty;
    }

    // Retorna a propriedade do valor total
    public DoubleProperty valorTotalProperty() {
        return valorTotalProperty;
    }

    // Retorna a propriedade do status
    public ObjectProperty<EStatusComanda> statusProperty() {
        return statusProperty;
    }
}
