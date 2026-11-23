package edu.curso.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import edu.curso.entities.Comanda;
import edu.curso.enums.EStatusComanda;

public class ComandaDAOImplementation
        implements ComandaDAOInterface {

    private Connection con;
    private ItemComandaDAOImplementation itemComandaDAO;

    public ComandaDAOImplementation() {

        try {
            Class.forName("org.mariadb.jdbc.Driver");

            con = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/bd_controle_comanda",
                    "root",
                    "alunofatec"
            );

            // Compartilha a conexão com a DAO de itens
            itemComandaDAO = new ItemComandaDAOImplementation(con);

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Erro ao conectar com o banco de dados.");
            e.printStackTrace();
        }
    }

    @Override
    public boolean salvarComanda(Comanda comanda) {
        String sql = """
            INSERT INTO comanda
            (numero, data_abertura, data_fechamento, valor_total, status)
            VALUES (?, ?, ?, ?, ?)
        """;

        try {
            PreparedStatement stmt = con.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            stmt.setLong(1, comanda.getNumero());

            stmt.setTimestamp(
                    2,
                    Timestamp.valueOf(comanda.getDataAbertura())
            );

            if (comanda.getDataFechamento() != null) {
                stmt.setTimestamp(
                        3,
                        Timestamp.valueOf(comanda.getDataFechamento())
                );
            } else {
                stmt.setNull(3, Types.TIMESTAMP);
            }

            stmt.setDouble(4, comanda.getValorTotal());

            stmt.setString(
                    5,
                    comanda.getStatus().name()
            );

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                comanda.setIdComanda(rs.getLong(1));
            }

            stmt.close();

            return comanda.getIdComanda() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao salvar comanda.");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean excluirComanda(long idComanda) {
        String sql = """
            DELETE FROM comanda
            WHERE id_comanda = ?
        """;

        try {
            // Primeiro exclui os itens relacionados
            itemComandaDAO.excluirItensDaComanda(idComanda);

            // Depois exclui a comanda
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setLong(1, idComanda);

            int linhasAfetadas = stmt.executeUpdate();

            stmt.close();

            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao excluir comanda.");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean editarComanda(Comanda comanda) {

        String sql = """
            UPDATE comanda
            SET numero = ?,
                data_abertura = ?,
                data_fechamento = ?,
                valor_total = ?,
                status = ?
            WHERE id_comanda = ?
        """;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setLong(1, comanda.getNumero());

            stmt.setTimestamp(
                    2,
                    Timestamp.valueOf(comanda.getDataAbertura())
            );

            if (comanda.getDataFechamento() != null) {
                stmt.setTimestamp(
                        3,
                        Timestamp.valueOf(comanda.getDataFechamento())
                );
            } else {
                stmt.setNull(3, Types.TIMESTAMP);
            }

            stmt.setDouble(4, comanda.getValorTotal());

            stmt.setString(
                    5,
                    comanda.getStatus().name()
            );

            stmt.setLong(6, comanda.getIdComanda());

            int linhasAfetadas = stmt.executeUpdate();

            stmt.close();

            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao editar comanda.");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Comanda> buscarComandas() {

        List<Comanda> comandas = new ArrayList<>();

        String sql = """
            SELECT *
            FROM comanda
        """;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Comanda comanda = new Comanda();

                comanda.setIdComanda(
                        rs.getLong("id_comanda")
                );

                comanda.setNumero(
                        rs.getLong("numero")
                );

                Timestamp abertura
                        = rs.getTimestamp("data_abertura");

                if (abertura != null) {
                    comanda.setDataAbertura(
                            abertura.toLocalDateTime()
                    );
                }

                Timestamp fechamento
                        = rs.getTimestamp("data_fechamento");

                if (fechamento != null) {
                    comanda.setDataFechamento(
                            fechamento.toLocalDateTime()
                    );
                }

                comanda.setValorTotal(
                        rs.getDouble("valor_total")
                );

                comanda.setStatus(
                        EStatusComanda.valueOf(
                                rs.getString("status")
                        )
                );

                comandas.add(comanda);
            }

            stmt.close();

        } catch (SQLException e) {
            System.out.println("Erro ao buscar comandas.");
            e.printStackTrace();
        }

        return comandas;
    }

    @Override
    public void atualizarValorTotal(long idComanda) {

        double total
                = itemComandaDAO.calcularValorTotal(idComanda);

        String sql = """
            UPDATE comanda
            SET valor_total = ?
            WHERE id_comanda = ?
        """;

        try {
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setDouble(1, total);
            stmt.setLong(2, idComanda);

            stmt.executeUpdate();

            stmt.close();

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar valor total da comanda.");
            e.printStackTrace();
        }
    }

    @Override
    public boolean fecharComanda(long idComanda) {

        String sql = """
        UPDATE comanda
        SET data_fechamento = ?, status = ?
        WHERE id_comanda = ?
        """;

        try (PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setTimestamp(
                    1,
                    Timestamp.valueOf(LocalDateTime.now())
            );

            stmt.setString(
                    2,
                    EStatusComanda.FECHADA.toString()
            );

            stmt.setLong(3, idComanda);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
