package edu.curso.DAO;

import java.util.List;
import edu.curso.entities.Comanda;

public interface ComandaDAOInterface {

    boolean salvarComanda(Comanda comanda);

    boolean excluirComanda(long idComanda);

    boolean editarComanda(Comanda comanda);

    List<Comanda> buscarComandas();

    void atualizarValorTotal(long idComanda);

    boolean fecharComanda(long idComanda);
}
