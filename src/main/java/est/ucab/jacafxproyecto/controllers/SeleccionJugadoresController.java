package est.ucab.jacafxproyecto.controllers;

import est.ucab.jacafxproyecto.models.LoadUsuario;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class SeleccionJugadoresController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public Button selector;
    protected ArrayList<selectedPlayers> selected = new ArrayList<>();
    public LoadUsuario usuarioJson = new LoadUsuario();

    public void crearSelected(){
        usuarioJson.loadUsuarioJson();
        for(int i =0; i<usuarioJson.jugadores.size(); i++){
            this.selected.add(new selectedPlayers(usuarioJson.jugadores.get(i)));
        }
    }

    @FXML
    public void meterJugadores(){
        crearSelected();
        for(int i=0; i<this.selected.size(); i++){

        }
    }

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
}
