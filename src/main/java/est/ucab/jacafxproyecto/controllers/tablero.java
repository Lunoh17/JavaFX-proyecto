package est.ucab.jacafxproyecto.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class tablero {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("JAVA NO MERECE EXISTIR");
    }
}