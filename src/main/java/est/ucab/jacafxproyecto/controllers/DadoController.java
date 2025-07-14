package est.ucab.jacafxproyecto.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class DadoController {
    public AnchorPane root;
    @FXML
    private ImageView imgDado;
    private int randomNumber;

    public int getRandomNumber() {
        return randomNumber;
    }

    /**
     * Método que se ejecuta al inicializar el controlador.
     * Configura la imagen del dado con la cara 1 al cargar la vista.
     */
    @FXML
    public void initialize() {
        // Establece la imagen del dado a la cara 1 al iniciar
        imgDado.setImage(new Image(this.getClass().getResource("/images/Dado1.png").toExternalForm()));
    }

    /**
     * Método que se ejecuta al hacer clic en el dado.
     * Genera un número aleatorio entre 1 y 6, simula el lanzamiento del dado
     * y actualiza la imagen del dado con la cara correspondiente.
     *
     * @param mouseEvent Evento de clic del ratón.
     */
    @FXML
    public void rollDice(MouseEvent mouseEvent) {
        randomNumber = (int) (Math.random() * 6) + 1;
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            int face = (int) (Math.random() * 6) + 1;
            String path = "/images/Dado" + face + ".png";
            imgDado.setImage(new Image(getClass().getResource(path).toExternalForm()));
        }));
        timeline.setCycleCount(12);
        timeline.setOnFinished(event -> {
            String finalPath = "/images/Dado" + randomNumber + ".png";
            imgDado.setImage(new Image(getClass().getResource(finalPath).toExternalForm()));
        });
        timeline.play();
    }
}
