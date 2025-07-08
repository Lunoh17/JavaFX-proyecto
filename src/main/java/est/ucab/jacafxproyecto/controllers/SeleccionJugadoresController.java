package est.ucab.jacafxproyecto.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SeleccionJugadoresController {
    public Button aceptar;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private AnchorPane paneWilcommen;
    @FXML
    private AnchorPane container1;
    @FXML
    private AnchorPane avatars;
    @FXML
    private TextField nickname;

    public void switchToMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/est/ucab/jacafxproyecto/menu-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToJuego(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/est/ucab/jacafxproyecto/juego-view.fxml"));
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
        avatars.setStyle("""
                -fx-border-color: #7a87ff;\s
                    -fx-border-width: 5;\s
                    -fx-border-radius: 15;\s
                    -fx-border-style: solid;
                    -fx-background-color: #fbefff;
                    -fx-background-radius: 20;""");
        nickname.setStyle(""" 
                    -fx-background-color: #ebdcff;""");
    }

    public void agregarJugador(ActionEvent actionEvent) {
        String nombreJugador = nickname.getText();
        if (nombreJugador.isEmpty()) {
            // Aquí podrías mostrar un mensaje de error si el campo está vacío
        } else {
            // Aquí puedes manejar el nombre del jugador, por ejemplo, guardarlo en una lista o base de datos
            nickname.clear();  // Limpiar el campo de texto después de agregar el jugador
        }
    }
}
