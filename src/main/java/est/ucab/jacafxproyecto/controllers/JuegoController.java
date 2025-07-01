package est.ucab.jacafxproyecto.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;



public class JuegoController {

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("JAVA NO MERECE EXISTIR");
    }

    @FXML
    public void onClickSalir() {
        Platform.exit();  // Cerrar la aplicación
    }
}