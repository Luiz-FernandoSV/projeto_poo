package edu.curso.DAO;

import java.util.List;
import edu.curso.entities.ItemComanda;

public interface ItemComandaDAOInterface {

    boolean salvarItemComanda(ItemComanda item);

    void excluirItemComanda(long idItemComanda);

    List<ItemComanda> buscarItensDaComanda(long idComanda);

    void excluirItensDaComanda(long idComanda);

    double calcularValorTotal(long idComanda);
}
