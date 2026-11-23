package edu.curso.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.curso.entities.Produto;
import edu.curso.enums.ECategoria;
import edu.curso.enums.EStatus;

public class ProdutoDAOImplementation implements ProdutoDAOInterface {

    private Connection con;

    public ProdutoDAOImplementation() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            System.out.println("Classe carregada com sucesso");
            con = DriverManager.getConnection(
                    "jdbc:mariadb://localhost:3306/bd_controle_comanda",
                    "root",
                    "alunofatec");
            System.out.println("Conectado no banco de dados");
        } catch (ClassNotFoundException e) {
            System.out.println("Classe MariaDB nao encontrada");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("Erro ao conectar no banco de dados");
            e.printStackTrace();
        }
    }

    @Override
    public Produto salvarProduto(Produto produto) {
        try {
            String sql = """
            INSERT INTO produto(nome, descricao, preco, categoria, disponivel)
            VALUES (?, ?, ?, ?, ?)
        """;

            PreparedStatement stmt = con.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getCategoria().ordinal());
            stmt.setInt(5, produto.getDisponivel().ordinal());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                produto.setIdProduto(rs.getLong(1));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao inserir o produto no banco de dados");
            e.printStackTrace();
        }

        return produto;
    }

    @Override
    public boolean excluirProduto(long id) {
        try {
            String sql = "DELETE FROM produto WHERE id_produto = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, id);
            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas > 0) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar o produto no banco de dados");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Produto editarProduto(long id, Produto produto) {
        try {
            String sql = """
            UPDATE produto
            SET nome = ?, descricao = ?, preco = ?,
                categoria = ?, disponivel = ?
            WHERE id_produto = ?
        """;

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getCategoria().ordinal());
            stmt.setInt(5, produto.getDisponivel().ordinal());
            stmt.setLong(6, id);

            int linhasAfetadas = stmt.executeUpdate();

            if (linhasAfetadas > 0) {
                produto.setIdProduto(id);
                return produto;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar o produto no banco de dados");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Produto> buscarProdutos() {
        List<Produto> lista = new ArrayList<>();

        try {
            String sql = "SELECT * FROM produto";

            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Produto p = new Produto();

                p.setIdProduto(rs.getLong("id_produto"));
                p.setNome(rs.getString("nome"));
                p.setDescricao(rs.getString("descricao"));
                p.setPreco(rs.getDouble("preco"));

                p.setCategoria(
                        ECategoria.values()[rs.getInt("categoria")]
                );

                p.setDisponivel(
                        EStatus.values()[rs.getInt("disponivel")]
                );

                lista.add(p);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao pesquisar produtos no banco de dados");
            e.printStackTrace();
        }

        return lista;
    }
}
