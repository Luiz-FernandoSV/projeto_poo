package edu.curso.DAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.curso.entities.ItemComanda;
import edu.curso.entities.Produto;
import edu.curso.enums.ECategoria;
import edu.curso.enums.EStatus;

public class ItemComandaDAOImplementation
        implements ItemComandaDAOInterface {

    private Connection con;

    // Construtor utilizado quando a DAO é instanciada separadamente
    public ItemComandaDAOImplementation() {
        conectar();
    }

    // Construtor para compartilhar a conexão com outra DAO
    public ItemComandaDAOImplementation(Connection con) {
        this.con = con;
    }

    private void conectar() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            con = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/bd_controle_comanda",
                    "root",
                    "alunofatec"
            );

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Erro ao conectar com o banco de dados.");
            e.printStackTrace();
        }
    }

    @Override
    public boolean salvarItemComanda(ItemComanda item) {

        String sql = """
        INSERT INTO itemcomanda
        (id_comanda, id_produto, quantidade, preco_unitario)
        VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setLong(1, item.getIdComanda());

            stmt.setLong(2, item.getProduto().getIdProduto());

            stmt.setInt(3, item.getQuantidade());

            stmt.setDouble(4, item.getPrecoUnitario());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void excluirItemComanda(long idItemComanda) {

        String sql = """
            DELETE FROM itemcomanda
            WHERE id_item_comanda = ?
        """;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setLong(1, idItemComanda);

            stmt.executeUpdate();

            stmt.close();

        } catch (SQLException e) {
            System.out.println("Erro ao excluir item da comanda.");
            e.printStackTrace();
        }
    }

    @Override
    public List<ItemComanda> buscarItensDaComanda(long idComanda) {

        List<ItemComanda> lista = new ArrayList<>();

        String sql = """
        SELECT
            ic.id_item_comanda,
            ic.id_comanda,
            ic.quantidade,
            ic.preco_unitario,
            p.id_produto,
            p.categoria,
            p.nome
        FROM itemcomanda ic
        INNER JOIN produto p
            ON ic.id_produto = p.id_produto
        WHERE ic.id_comanda = ?
        """;

        try (PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setLong(1, idComanda);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Produto produto = new Produto();

                    produto.setIdProduto(
                            rs.getLong("id_produto")
                    );

                    produto.setNome(
                            rs.getString("nome")
                    );

                    produto.setCategoria(
                            ECategoria.values()[rs.getInt("categoria")]
                    );

                    ItemComanda item = new ItemComanda();

                    item.setIdItemComanda(
                            rs.getLong("id_item_comanda")
                    );

                    item.setIdComanda(
                            rs.getLong("id_comanda")
                    );

                    item.setProduto(produto);

                    item.setQuantidade(
                            rs.getInt("quantidade")
                    );

                    item.setPrecoUnitario(
                            rs.getDouble("preco_unitario")
                    );

                    lista.add(item);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public void excluirItensDaComanda(long idComanda) {

        String sql = """
            DELETE FROM itemcomanda
            WHERE id_comanda = ?
        """;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setLong(1, idComanda);

            stmt.executeUpdate();

            stmt.close();

        } catch (SQLException e) {
            System.out.println("Erro ao excluir itens da comanda.");
            e.printStackTrace();
        }
    }

    @Override
    public double calcularValorTotal(long idComanda) {

        String sql = """
            SELECT COALESCE(
                SUM(quantidade * preco_unitario), 0
            ) AS valor_total
            FROM itemcomanda
            WHERE id_comanda = ?
        """;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setLong(1, idComanda);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal("valor_total").doubleValue();
            }

            stmt.close();

        } catch (SQLException e) {
            System.out.println("Erro ao calcular valor total da comanda.");
            e.printStackTrace();
        }

        return 0.0;
    }
}
