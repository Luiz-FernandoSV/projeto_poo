package edu.curso.boundaries;

import edu.curso.controllers.ProdutoController;
import edu.curso.entities.Produto;
import edu.curso.enums.ECategoria;
import edu.curso.enums.EStatus;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class ProdutoBoundary {

    private Scene scene;
    private ProdutoController controller;
    private TableView<Produto> table = new TableView<>();
    private ObservableList<Produto> produtos = FXCollections.observableArrayList();

    public ProdutoBoundary(Stage stage) {

        controller = new ProdutoController();

        // idProduto
        Label lblId = new Label("ID do produto:");
        TextField txtId = new TextField();
        txtId.setEditable(false);
        // nome
        Label lblNome = new Label("Nome do produto:");
        TextField txtNome = new TextField();
        // descricao
        Label lblDescricao = new Label("Descrição do produto:");
        TextField txtDescricao = new TextField();
        // preco
        Label lblPreco = new Label("Preco do produto:");
        TextField txtPreco = new TextField();
        // categoria
        Label lblCategoria = new Label("Categoria do produto:");
        ComboBox<ECategoria> cbCategoria = new ComboBox<>();
        cbCategoria.getItems().setAll(ECategoria.values());

        // disponibilidade
        Label lblDisponibilidade = new Label("Disponibilidade do produto:");
        ComboBox<EStatus> cbDisponibilidade = new ComboBox<>();
        cbDisponibilidade.getItems().setAll(EStatus.values());

        Button btnVoltar = new Button("Voltar");
        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");

        TableColumn<Produto, Long> colId = new TableColumn<>("ID");

        colId.setCellValueFactory(
                item -> new javafx.beans.property.ReadOnlyObjectWrapper<>(
                        item.getValue().getIdProduto()
                )
        );

        TableColumn<Produto, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(
                item -> new javafx.beans.property.ReadOnlyStringWrapper(
                        item.getValue().getNome()
                )
        );

        TableColumn<Produto, String> colDescricao = new TableColumn<>("Descrição");
        colDescricao.setCellValueFactory(
                item -> new javafx.beans.property.ReadOnlyStringWrapper(
                        item.getValue().getDescricao()
                )
        );

        TableColumn<Produto, Double> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(
                item -> new javafx.beans.property.ReadOnlyObjectWrapper<>(
                        item.getValue().getPreco()
                )
        );
        colPreco.setCellFactory(col -> new TableCell<Produto, Double>() {

            @Override
            protected void updateItem(Double preco, boolean empty) {
                super.updateItem(preco, empty);

                if (empty || preco == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", preco));
                }
            }
        });

        TableColumn<Produto, ECategoria> colCategoria = new TableColumn<>("Categoria");
        colCategoria.setCellValueFactory(
                item -> new javafx.beans.property.ReadOnlyObjectWrapper<>(
                        item.getValue().getCategoria()
                )
        );
        TableColumn<Produto, EStatus> colDisponibilidade
                = new TableColumn<>("Disponibilidade");

        colDisponibilidade.setCellValueFactory(
                item -> new ReadOnlyObjectWrapper<>(
                        item.getValue().getDisponivel()
                )
        );

        TableColumn<Produto, Void> colAcoes = new TableColumn<>("Ações");

        colAcoes.setCellFactory(col -> new TableCell<Produto, Void>() {

            private final Button btnAlterar = new Button("Alterar");
            private final Button btnExcluir = new Button("Excluir");
            private final HBox painelBotoes = new HBox(5);

            {
                painelBotoes.getChildren().addAll(
                        btnAlterar,
                        btnExcluir
                );

                // Alterar produto
                btnAlterar.setOnAction(e -> {
                    Produto produto = getTableRow().getItem();

                    if (produto != null) {
                        controller.carregarProduto(produto);
                    }
                });

                // Excluir produto
                btnExcluir.setOnAction(e -> {
                    Produto produto = getTableRow().getItem();

                    if (produto != null) {
                        // Exclusão direta do produto
                        controller.carregarProduto(produto);

                        if (controller.excluir()) {
                            new Alert(AlertType.INFORMATION,
                                    "Produto excluído com sucesso",
                                    ButtonType.OK).showAndWait();
                        } else {
                            new Alert(AlertType.ERROR,
                                    "Erro ao excluir o produto",
                                    ButtonType.OK).showAndWait();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(painelBotoes);
                }
            }
        });

        table.getColumns().addAll(
                colId,
                colNome,
                colDescricao,
                colPreco,
                colCategoria,
                colDisponibilidade,
                colAcoes
        );

        produtos.setAll(controller.buscarProdutos());
        table.setItems(controller.getLista());

        GridPane pane = new GridPane();

        pane.setHgap(10);
        pane.setVgap(10);
        pane.setPadding(new Insets(15));

        // campos
        pane.add(lblId, 0, 0);
        pane.add(txtId, 1, 0);

        pane.add(lblNome, 0, 1);
        pane.add(txtNome, 1, 1);

        pane.add(lblDescricao, 0, 2);
        pane.add(txtDescricao, 1, 2);

        pane.add(lblPreco, 0, 3);
        pane.add(txtPreco, 1, 3);

        pane.add(lblCategoria, 0, 4);
        pane.add(cbCategoria, 1, 4);

        pane.add(lblDisponibilidade, 0, 5);
        pane.add(cbDisponibilidade, 1, 5);

        // Botões
        pane.add(btnSalvar, 0, 6);

        // Layout principal
        VBox layout = new VBox(10);

        layout.setPadding(new Insets(10));

        layout.getChildren().addAll(
                btnVoltar,
                pane,
                table
        );

        // Scene
        scene = new Scene(layout, 700, 600);

        btnSalvar.setOnAction(e -> {

            // Valida os campos antes de salvar
            if (!validarCampos(
                    txtNome,
                    txtDescricao,
                    txtPreco,
                    cbCategoria,
                    cbDisponibilidade
            )) {
                return;
            }

            // Verifica se é cadastro ou alteração
            boolean editando = controller.idProperty().get() != 0;

            if (controller.salvar()) {

                if (editando) {
                    new Alert(
                            AlertType.INFORMATION,
                            "Produto alterado com sucesso!",
                            ButtonType.OK
                    ).showAndWait();

                } else {
                    new Alert(
                            AlertType.INFORMATION,
                            "Produto cadastrado com sucesso!",
                            ButtonType.OK
                    ).showAndWait();
                }

            } else {

                if (editando) {
                    mostrarErro("Alteração", "Não foi possível alterar o produto.");
                } else {
                    mostrarErro("Cadastro", "Não foi possível cadastrar o produto.");
                }
            }
        });
        Bindings.bindBidirectional(txtId.textProperty(), controller.idProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(txtNome.textProperty(), controller.nomeProperty());
        Bindings.bindBidirectional(txtDescricao.textProperty(), controller.descricaoProperty());
        Bindings.bindBidirectional(txtPreco.textProperty(), controller.precoProperty(), new NumberStringConverter("#,##0.00"));
        cbDisponibilidade.valueProperty().bindBidirectional(
                controller.disponivelProperty()
        );

        cbCategoria.valueProperty().bindBidirectional(
                controller.categoriaProperty()
        );
        btnVoltar.setOnAction(e
                -> {
            MenuBoundary menu = new MenuBoundary(stage);
            stage.setScene(menu.getScene());
        }
        );
    }

    public Scene getScene() {
        return scene;
    }

    private boolean validarCampos(
            TextField txtNome,
            TextField txtDescricao,
            TextField txtPreco,
            ComboBox<ECategoria> cbCategoria,
            ComboBox<EStatus> cbDisponibilidade
    ) {

        // Nome
        if (txtNome.getText().trim().isEmpty()) {
            mostrarErro("Nome", "O nome do produto é obrigatório.");
            txtNome.requestFocus();
            return false;
        }

        if (txtNome.getText().trim().length() > 100) {
            mostrarErro("Nome", "O nome deve ter no máximo 100 caracteres.");
            txtNome.requestFocus();
            return false;
        }

        // Descrição
        if (txtDescricao.getText().length() > 255) {
            mostrarErro("Descrição", "A descrição deve ter no máximo 255 caracteres.");
            txtDescricao.requestFocus();
            return false;
        }

        // Preço
        if (txtPreco.getText().trim().isEmpty()) {
            mostrarErro("Preço", "O preço é obrigatório.");
            txtPreco.requestFocus();
            return false;
        }

        double preco;

        try {
            preco = Double.parseDouble(
                    txtPreco.getText().replace(".", "").replace(",", ".")
            );
        } catch (NumberFormatException ex) {
            mostrarErro("Preço", "Informe um valor numérico válido.");
            txtPreco.requestFocus();
            return false;
        }

        if (!Double.isFinite(preco) || preco <= 0) {
            mostrarErro("Preço", "O preço deve ser maior que zero.");
            txtPreco.requestFocus();
            return false;
        }

        // Categoria
        if (cbCategoria.getValue() == null
                || cbCategoria.getValue() == ECategoria.Nenhum) {

            mostrarErro("Categoria", "Selecione uma categoria válida.");
            cbCategoria.requestFocus();
            return false;
        }

        // Disponibilidade
        if (cbDisponibilidade.getValue() == null) {
            mostrarErro("Disponibilidade", "Selecione a disponibilidade do produto.");
            cbDisponibilidade.requestFocus();
            return false;
        }

        return true;
    }

    private void mostrarErro(String campo, String mensagem) {
        new Alert(
                AlertType.ERROR,
                mensagem,
                ButtonType.OK
        ).showAndWait();
    }
}
