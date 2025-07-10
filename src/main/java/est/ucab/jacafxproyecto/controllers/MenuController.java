package est.ucab.jacafxproyecto.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuController {
    @FXML
    public AnchorPane container1;
    @FXML
    private AnchorPane paneWilcommen;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToEstadisticasJuegadores(ActionEvent event) throws IOException {
        EstadisticasJugadoresController tops = new EstadisticasJugadoresController();
        VBox table= new VBox(tops.toptiers);
        scene = new Scene(table, 600, 800);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSeleccionJugadores(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/est/ucab/jacafxproyecto/SeleccionJugadores-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchTojuego(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/est/ucab/jacafxproyecto/juego-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onClickSalir() {
        Platform.exit();  // Cerrar la aplicación
    }

    @FXML
    public void initialize (){
        paneWilcommen.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #5270ff, #fe66c4);");
        container1.setStyle("""
                -fx-border-color: black;\s
                    -fx-border-width: 12;\s
                    -fx-border-radius: 15;\s
                    -fx-border-style: solid;
                    -fx-background-color: #ebdcff;
                    -fx-background-radius: 20;""");
    }


}
