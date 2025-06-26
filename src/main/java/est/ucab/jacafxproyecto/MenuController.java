package est.ucab.jacafxproyecto;

import javafx.application.Platform;
import javafx.fxml.FXML;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToEstadisticasJuegadores(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("EstadisticasJugadores-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSeleccionJugadores(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("SeleccionJugadores-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchTojuego(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("juego-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onClickSalir() {
        Platform.exit();  // Cerrar la aplicación
    }

}
