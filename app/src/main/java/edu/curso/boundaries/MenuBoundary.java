package edu.curso.boundaries;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class MenuBoundary {

    private Stage stage;
    private Scene scene;

    public MenuBoundary(Stage stage) {
        this.stage = stage;

        Label lblTitulo = new Label("Sistema de controle de comandas");
        Button btnProdutos = new Button("Produtos");
        Button btnComandas = new Button("Comandas");

        // Faz os botões ocuparem toda a largura disponível
        btnProdutos.setMaxWidth(Double.MAX_VALUE);
        btnComandas.setMaxWidth(Double.MAX_VALUE);

        // Layout principal
        VBox layout = new VBox(15);

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(30, 40, 30, 40));

        layout.getChildren().addAll(
                lblTitulo,
                btnProdutos,
                btnComandas
        );

        scene = new Scene(layout, 400, 300);
        stage.setMaximized(true);

        btnProdutos.setOnAction(e -> {
            ProdutoBoundary tela = new ProdutoBoundary(stage);
            stage.setScene(tela.getScene());
        });

        btnComandas.setOnAction(e -> {
            ComandaBoundary telaComanda = new ComandaBoundary(stage);
            stage.setScene(telaComanda.getScene());
        });
    }

    public Scene getScene() {
        return scene;
    }
}
