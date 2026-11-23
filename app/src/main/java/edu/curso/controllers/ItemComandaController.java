package edu.curso.controllers;

import edu.curso.DAO.ComandaDAOImplementation;
import edu.curso.DAO.ItemComandaDAOImplementation;
import edu.curso.entities.ItemComanda;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ItemComandaController {

    private ItemComandaDAOImplementation itemComandaDAO;
    private ComandaDAOImplementation comandaDAO;

    public ItemComandaController() {

        itemComandaDAO = new ItemComandaDAOImplementation();
        comandaDAO = new ComandaDAOImplementation();
    }

    public void adicionarItem(ItemComanda item) {

        itemComandaDAO.salvarItemComanda(item);

        comandaDAO.atualizarValorTotal(
                item.getIdComanda()
        );
    }

    public void excluirItem(long idItemComanda, long idComanda) {

        itemComandaDAO.excluirItemComanda(idItemComanda);

        comandaDAO.atualizarValorTotal(idComanda);
    }
}
