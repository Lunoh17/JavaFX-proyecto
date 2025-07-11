package est.ucab.jacafxproyecto.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import est.ucab.jacafxproyecto.models.Ficha;
import est.ucab.jacafxproyecto.controllers.JuegoController;
import javafx.application.Platform;
import javafx.fxml.FXML;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MenuController {
    @FXML
    public AnchorPane container1;
    @FXML
    private AnchorPane paneWilcommen;

    @FXML
    private javafx.scene.layout.GridPane grid00;
    private ArrayList<Ficha> jugadores;
    private ArrayList<Node> fichaNodes;
    private FichaController[] fichaControllers;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToEstadisticasJuegadores(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/est/ucab/jacafxproyecto/EstadisticasJugadores-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
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
    public void cargarPartida(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/est/ucab/jacafxproyecto/juego-view.fxml"));
        Parent root = loader.load();
        JuegoController juegoController = loader.getController();
        juegoController.cargarEstadoGuardado(); // Llama correctamente al método para cargar la partida
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void printBoard() {

    }

}
