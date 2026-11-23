package edu.curso.boundaries;

import java.time.format.DateTimeFormatter;

import edu.curso.controllers.ComandaController;
import edu.curso.entities.Comanda;
import edu.curso.entities.ItemComanda;
import edu.curso.entities.Produto;
import edu.curso.enums.EStatusComanda;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
import javafx.util.StringConverter;

public class ComandaBoundary {

    private Scene scene;
    private ComandaController controller;

    // Tabelas de comandas e itens
    private TableView<Comanda> tabela = new TableView<>();
    private TableView<ItemComanda> tabelaItens = new TableView<>();

    // Armazena a comanda que está sendo manipulada no momento
    private Comanda comandaAtual;

    // Define o formato de exibição das datas
    private final DateTimeFormatter formatoData
            = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public ComandaBoundary(Stage stage) {

        controller = new ComandaController();

        // Campos da comanda
        Label lblId = new Label("ID:");
        TextField txtId = new TextField();
        txtId.setEditable(false);

        Label lblNumero = new Label("Número:");
        TextField txtNumero = new TextField();

        Label lblAbertura = new Label("Data de abertura:");
        TextField txtAbertura = new TextField();
        txtAbertura.setEditable(false);

        Label lblFechamento = new Label("Data de fechamento:");
        TextField txtFechamento = new TextField();
        txtFechamento.setEditable(false);

        Label lblTotal = new Label("Valor total:");
        TextField txtTotal = new TextField();
        txtTotal.setEditable(false);

        Label lblStatus = new Label("Status:");
        TextField txtStatus = new TextField();
        txtStatus.setEditable(false);

        // Vincula os campos às propriedades do controller
        txtId.textProperty().bind(
                Bindings.convert(controller.idProperty())
        );

        txtNumero.textProperty().bindBidirectional(
                controller.numeroTextoProperty()
        );

        txtAbertura.textProperty().bind(
                Bindings.createStringBinding(
                        () -> controller.dataAberturaProperty().get() == null
                        ? ""
                        : controller.dataAberturaProperty().get()
                                .format(formatoData),
                        controller.dataAberturaProperty()
                )
        );

        txtFechamento.textProperty().bind(
                Bindings.createStringBinding(
                        () -> controller.dataFechamentoProperty().get() == null
                        ? ""
                        : controller.dataFechamentoProperty().get()
                                .format(formatoData),
                        controller.dataFechamentoProperty()
                )
        );

        txtTotal.textProperty().bind(
                Bindings.createStringBinding(
                        () -> String.format(
                                "%.2f",
                                controller.valorTotalProperty().get()
                        ),
                        controller.valorTotalProperty()
                )
        );

        txtStatus.textProperty().bind(
                Bindings.createStringBinding(
                        () -> controller.statusProperty().get() == null
                        ? ""
                        : controller.statusProperty().get().toString(),
                        controller.statusProperty()
                )
        );

        // Campos para adicionar produtos à comanda
        ComboBox<Produto> cbProduto = new ComboBox<>();
        cbProduto.setPromptText("Selecione um produto");
        cbProduto.setItems(controller.buscarProdutos());

        // Define como os produtos serão apresentados no ComboBox
        cbProduto.setConverter(new StringConverter<Produto>() {

            @Override
            public String toString(Produto produto) {
                return produto == null ? "" : produto.getNome();
            }

            @Override
            public Produto fromString(String texto) {
                return null;
            }
        });

        TextField txtQuantidade = new TextField("1");
        txtQuantidade.setPromptText("Quantidade");
        txtQuantidade.setPrefWidth(100);

        // Botões de operações da comanda
        Button btnVoltar = new Button("Voltar");
        Button btnSalvar = new Button("Salvar");
        Button btnExcluir = new Button("Excluir");
        Button btnLimpar = new Button("Limpar");

        Button btnAdicionar = new Button("Adicionar produto");
        Button btnFechar = new Button("Fechar comanda");

        // Tabela de comandas
        // Coluna que apresenta o ID da comanda
        TableColumn<Comanda, Long> colId
                = new TableColumn<>("ID");

        colId.setCellValueFactory(item
                -> new ReadOnlyObjectWrapper<>(
                        item.getValue().getIdComanda()
                )
        );

        // Coluna que apresenta o número da comanda
        TableColumn<Comanda, Long> colNumero
                = new TableColumn<>("Número");

        colNumero.setCellValueFactory(item
                -> new ReadOnlyObjectWrapper<>(
                        item.getValue().getNumero()
                )
        );

        // Coluna que apresenta a data de abertura formatada
        TableColumn<Comanda, String> colAbertura
                = new TableColumn<>("Abertura");

        colAbertura.setCellValueFactory(item
                -> Bindings.createStringBinding(
                        () -> item.getValue().getDataAbertura() == null
                        ? ""
                        : item.getValue().getDataAbertura()
                                .format(formatoData)
                )
        );

        // Coluna que apresenta a data de fechamento formatada
        TableColumn<Comanda, String> colFechamento
                = new TableColumn<>("Fechamento");

        colFechamento.setCellValueFactory(item
                -> Bindings.createStringBinding(
                        () -> item.getValue().getDataFechamento() == null
                        ? ""
                        : item.getValue().getDataFechamento()
                                .format(formatoData)
                )
        );

        // Coluna que apresenta o valor total da comanda
        TableColumn<Comanda, Double> colTotal
                = new TableColumn<>("Valor total");

        colTotal.setCellValueFactory(item
                -> new ReadOnlyObjectWrapper<>(
                        item.getValue().getValorTotal()
                )
        );

        // Formata o valor total para apresentar sempre duas casas decimais
        colTotal.setCellFactory(col
                -> new TableCell<Comanda, Double>() {

            @Override
            protected void updateItem(Double valor, boolean vazio) {
                super.updateItem(valor, vazio);

                setText(vazio || valor == null
                        ? null
                        : String.format("%.2f", valor));
            }
        });

        // Coluna que apresenta o status da comanda
        TableColumn<Comanda, String> colStatus
                = new TableColumn<>("Status");

        colStatus.setCellValueFactory(item
                -> new javafx.beans.property.ReadOnlyStringWrapper(
                        item.getValue().getStatus().toString()
                )
        );

        // Coluna de ações para abrir uma comanda
        TableColumn<Comanda, Void> colAcoes
                = new TableColumn<>("Ações");

        colAcoes.setCellFactory(col
                -> new TableCell<Comanda, Void>() {

            private final Button btnAbrir = new Button("Abrir");

            {
                // Carrega os dados da comanda selecionada nos campos
                btnAbrir.setOnAction(e -> {

                    Comanda comanda = getTableRow().getItem();

                    if (comanda != null) {

                        comandaAtual = comanda;

                        // Atualiza automaticamente os campos vinculados
                        controller.carregarComanda(comandaAtual);

                        // Carrega os produtos pertencentes à comanda
                        tabelaItens.setItems(
                                controller.buscarItensDaComanda(
                                        comanda.getIdComanda()
                                )
                        );

                        // Habilita as operações somente se a comanda estiver aberta
                        boolean aberta
                                = comanda.getStatus() == EStatusComanda.ABERTA;

                        btnAdicionar.setDisable(!aberta);
                        btnFechar.setDisable(!aberta);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean vazio) {
                super.updateItem(item, vazio);

                // Exibe o botão somente nas linhas que possuem uma comanda
                setGraphic(vazio ? null : btnAbrir);
            }
        });

        // Adiciona as colunas à tabela de comandas
        tabela.getColumns().addAll(
                colId,
                colNumero,
                colAbertura,
                colFechamento,
                colTotal,
                colStatus,
                colAcoes
        );

        // Tabela de itens da comanda
        // Coluna que apresenta o nome do produto
        TableColumn<ItemComanda, String> colProduto
                = new TableColumn<>("Produto");

        colProduto.setCellValueFactory(item
                -> new javafx.beans.property.ReadOnlyStringWrapper(
                        item.getValue().getProduto().getNome()
                )
        );

        // Coluna que apresenta a categoria do produto
        TableColumn<ItemComanda, edu.curso.enums.ECategoria> colCategoria
                = new TableColumn<>("Categoria");

        colCategoria.setCellValueFactory(
                item -> new ReadOnlyObjectWrapper<>(
                        item.getValue().getProduto().getCategoria()
                )
        );

        // Coluna que apresenta a quantidade de produtos
        TableColumn<ItemComanda, Integer> colQuantidade
                = new TableColumn<>("Quantidade");

        colQuantidade.setCellValueFactory(item
                -> new ReadOnlyObjectWrapper<>(
                        item.getValue().getQuantidade()
                )
        );

        // Coluna que apresenta o preço unitário do produto
        TableColumn<ItemComanda, Double> colPreco
                = new TableColumn<>("Preço unitário");

        colPreco.setCellValueFactory(item
                -> new ReadOnlyObjectWrapper<>(
                        item.getValue().getPrecoUnitario()
                )
        );

        // Formata o preço para apresentar sempre duas casas decimais
        colPreco.setCellFactory(col
                -> new TableCell<ItemComanda, Double>() {

            @Override
            protected void updateItem(Double valor, boolean vazio) {
                super.updateItem(valor, vazio);

                setText(vazio || valor == null
                        ? null
                        : String.format("%.2f", valor));
            }
        });

        // Coluna de ações para remover um item da comanda
        TableColumn<ItemComanda, Void> colAcoesItem
                = new TableColumn<>("Ações");

        colAcoesItem.setCellFactory(col
                -> new TableCell<ItemComanda, Void>() {

            private final Button btnRemover = new Button("Remover");

            {
                // Executa a remoção do item selecionado
                btnRemover.setOnAction(e -> {

                    ItemComanda item = getTableRow().getItem();

                    if (item != null && comandaAtual != null) {

                        // Impede a remoção de produtos de comandas fechadas
                        if (comandaAtual.getStatus()
                                != EStatusComanda.ABERTA) {

                            new Alert(
                                    Alert.AlertType.ERROR,
                                    "Não é possível remover produtos de uma comanda fechada."
                            ).showAndWait();

                            return;
                        }

                        // Solicita confirmação antes de remover o produto
                        Alert confirmacao = new Alert(
                                Alert.AlertType.CONFIRMATION,
                                "Deseja remover " + item.getProduto().getNome() + "?",
                                ButtonType.OK,
                                ButtonType.CANCEL
                        );

                        if (confirmacao.showAndWait()
                                .orElse(ButtonType.CANCEL) == ButtonType.OK) {

                            long idAtual = comandaAtual.getIdComanda();

                            // Solicita ao controller a exclusão do item
                            controller.excluirItemComanda(
                                    item.getIdItemComanda(),
                                    idAtual
                            );

                            // Atualiza a tabela de comandas após a exclusão
                            controller.pesquisarTodos();
                            tabela.setItems(controller.getLista());

                            // Localiza novamente a comanda atualizada
                            for (Comanda c : controller.getLista()) {
                                if (c.getIdComanda() == idAtual) {

                                    comandaAtual = c;

                                    // Atualiza os itens e as propriedades da comanda
                                    tabelaItens.setItems(
                                            controller.buscarItensDaComanda(idAtual)
                                    );

                                    controller.carregarComanda(c);

                                    break;
                                }
                            }
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean vazio) {
                super.updateItem(item, vazio);

                // Exibe o botão somente nas linhas que possuem um item
                setGraphic(vazio ? null : btnRemover);
            }
        });

        // Adiciona as colunas à tabela de itens
        tabelaItens.getColumns().addAll(
                colProduto,
                colQuantidade,
                colPreco,
                colCategoria,
                colAcoesItem
        );

        // Formulário com os dados da comanda
        GridPane formulario = new GridPane();

        formulario.setHgap(10);
        formulario.setVgap(10);
        formulario.setPadding(new Insets(10));

        // Posiciona os campos no formulário
        formulario.add(lblId, 0, 0);
        formulario.add(txtId, 1, 0);

        formulario.add(lblNumero, 0, 1);
        formulario.add(txtNumero, 1, 1);

        formulario.add(lblAbertura, 0, 2);
        formulario.add(txtAbertura, 1, 2);

        formulario.add(lblFechamento, 0, 3);
        formulario.add(txtFechamento, 1, 3);

        formulario.add(lblTotal, 0, 4);
        formulario.add(txtTotal, 1, 4);

        formulario.add(lblStatus, 0, 5);
        formulario.add(txtStatus, 1, 5);

        // Agrupa os botões de operações da comanda
        HBox botoes = new HBox(10);
        botoes.getChildren().addAll(
                btnSalvar,
                btnExcluir,
                btnLimpar
        );

        // Agrupa os campos e botões de adição de produtos
        HBox formularioProduto = new HBox(10);
        formularioProduto.getChildren().addAll(
                new Label("Produto:"),
                cbProduto,
                new Label("Quantidade:"),
                txtQuantidade,
                btnAdicionar,
                btnFechar
        );

        // Layout principal da tela
        VBox layout = new VBox(10);

        layout.setPadding(new Insets(10));

        // Organiza todos os componentes verticalmente
        layout.getChildren().addAll(
                btnVoltar,
                formulario,
                botoes,
                new Label("Comandas cadastradas"),
                tabela,
                new Label("Produtos da comanda selecionada"),
                tabelaItens,
                formularioProduto
        );

        scene = new Scene(layout, 850, 700);

        // Eventos dos botões
        // Cadastra uma nova comanda
        btnSalvar.setOnAction(e -> {

            try {

                // Valida o número informado
                Long.parseLong(controller.numeroTextoProperty().get());

                // Solicita ao controller o cadastro da comanda
                if (controller.salvar()) {

                    new Alert(
                            Alert.AlertType.INFORMATION,
                            "Comanda cadastrada com sucesso!"
                    ).showAndWait();

                    // Atualiza a tabela após o cadastro
                    tabela.setItems(controller.getLista());

                    // Limpa os campos após o cadastro
                    btnLimpar.fire();

                } else {

                    new Alert(
                            Alert.AlertType.ERROR,
                            "Não foi possível cadastrar a comanda."
                    ).showAndWait();
                }

            } catch (NumberFormatException ex) {

                // Informa quando o número digitado não é válido
                new Alert(
                        Alert.AlertType.ERROR,
                        "Informe um número de comanda válido."
                ).showAndWait();
            }
        });

        // Exclui a comanda selecionada
        btnExcluir.setOnAction(e -> {

            Comanda selecionada = tabela.getSelectionModel()
                    .getSelectedItem();

            // Verifica se uma comanda foi selecionada
            if (selecionada == null) {

                new Alert(
                        Alert.AlertType.ERROR,
                        "Selecione uma comanda para excluir."
                ).showAndWait();

                return;
            }

            // Solicita ao controller a exclusão da comanda
            if (controller.excluir(selecionada.getIdComanda())) {
                new Alert(
                        Alert.AlertType.INFORMATION,
                        "Comanda excluída com sucesso!",
                        ButtonType.OK
                ).showAndWait();

            } else {
                new Alert(
                        Alert.AlertType.ERROR,
                        "Não foi possível excluir a comanda.",
                        ButtonType.OK
                ).showAndWait();
            }

            // Atualiza a tabela e limpa os campos
            tabela.setItems(controller.getLista());

            btnLimpar.fire();
        });

        // Limpa os campos e redefine o estado inicial da tela
        btnLimpar.setOnAction(e -> {

            comandaAtual = null;

            // Redefine as propriedades vinculadas aos campos
            controller.limparCampos();

            tabela.getSelectionModel().clearSelection();

            // Esvazia a tabela de itens
            tabelaItens.setItems(
                    FXCollections.observableArrayList()
            );

            cbProduto.setValue(null);
            txtQuantidade.setText("1");

            // Desabilita as operações que dependem de uma comanda aberta
            btnAdicionar.setDisable(true);
            btnFechar.setDisable(true);
        });

        // Adiciona um produto à comanda atual
        btnAdicionar.setOnAction(e -> {

            // Verifica se uma comanda foi aberta
            if (comandaAtual == null) {

                new Alert(
                        Alert.AlertType.ERROR,
                        "Abra uma comanda primeiro."
                ).showAndWait();

                return;
            }

            // Recupera o produto selecionado
            Produto produto = cbProduto.getValue();

            if (produto == null) {

                new Alert(
                        Alert.AlertType.ERROR,
                        "Selecione um produto."
                ).showAndWait();

                return;
            }

            try {

                // Converte e valida a quantidade informada
                int qtd = Integer.parseInt(txtQuantidade.getText());

                if (qtd <= 0) {

                    new Alert(
                            Alert.AlertType.ERROR,
                            "A quantidade deve ser maior que zero."
                    ).showAndWait();

                    return;
                }

                long idAtual = comandaAtual.getIdComanda();

                // Solicita ao controller a adição do produto
                boolean sucesso = controller.adicionarProduto(
                        idAtual,
                        produto,
                        qtd
                );

                if (!sucesso) {

                    new Alert(
                            Alert.AlertType.ERROR,
                            "Não foi possível adicionar o produto."
                    ).showAndWait();

                    return;
                }

                // Atualiza a tabela de comandas
                tabela.setItems(controller.getLista());

                // Localiza a comanda atualizada para renovar os dados
                for (Comanda c : controller.getLista()) {

                    if (c.getIdComanda() == idAtual) {

                        comandaAtual = c;

                        // Atualiza os itens e as propriedades da comanda
                        tabelaItens.setItems(
                                controller.buscarItensDaComanda(idAtual)
                        );

                        controller.carregarComanda(c);

                        break;
                    }
                }

                // Limpa os campos de adição de produto
                cbProduto.setValue(null);
                txtQuantidade.setText("1");

            } catch (NumberFormatException ex) {

                // Informa quando a quantidade não é um número válido
                new Alert(
                        Alert.AlertType.ERROR,
                        "Informe uma quantidade válida."
                ).showAndWait();
            }
        });

        // Fecha a comanda atual
        btnFechar.setOnAction(e -> {

            if (comandaAtual == null) {
                return;
            }

            // Solicita confirmação antes de fechar a comanda
            Alert confirmacao = new Alert(
                    Alert.AlertType.CONFIRMATION,
                    "Deseja realmente fechar esta comanda?",
                    ButtonType.OK,
                    ButtonType.CANCEL
            );

            if (confirmacao.showAndWait()
                    .orElse(ButtonType.CANCEL) == ButtonType.OK) {

                long idAtual = comandaAtual.getIdComanda();

                // Solicita ao controller o fechamento da comanda
                if (controller.fecharComanda(idAtual)) {

                    // Atualiza a tabela de comandas
                    tabela.setItems(controller.getLista());

                    // Localiza a comanda atualizada
                    for (Comanda c : controller.getLista()) {

                        if (c.getIdComanda() == idAtual) {

                            comandaAtual = c;

                            // Atualiza automaticamente os campos vinculados
                            controller.carregarComanda(c);

                            // Impede novas alterações na comanda fechada
                            btnAdicionar.setDisable(true);
                            btnFechar.setDisable(true);

                            break;
                        }
                    }

                } else {

                    new Alert(
                            Alert.AlertType.ERROR,
                            "Não foi possível fechar a comanda."
                    ).showAndWait();
                }
            }
        });

        // Retorna ao menu principal
        btnVoltar.setOnAction(e
                -> stage.setScene(new MenuBoundary(stage).getScene())
        );

        // Define o estado inicial da tela
        btnLimpar.fire();

        // Carrega as comandas cadastradas na tabela
        controller.pesquisarTodos();
        tabela.setItems(controller.getLista());
    }

    // Retorna a cena para ser exibida pelo Stage
    public Scene getScene() {
        return scene;
    }
}
