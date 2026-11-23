package edu.curso;

import edu.curso.boundaries.MenuBoundary;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        MenuBoundary menu = new MenuBoundary(stage);

        stage.setTitle("Sistema de Controle de Comandas");
        stage.setScene(menu.getScene());
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}