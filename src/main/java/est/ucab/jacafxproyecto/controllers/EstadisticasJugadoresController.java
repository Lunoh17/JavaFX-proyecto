package est.ucab.jacafxproyecto.controllers;
import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EstadisticasJugadoresController {
    public AnchorPane paneWilcommen;
    public AnchorPane container1;
    private Stage stage;
    private Scene scene;
    private Parent root;


    public void switchToMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/est/ucab/jacafxproyecto/menu-view.fxml"));
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

