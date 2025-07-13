package est.ucab.jacafxproyecto.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;


public class BienvenidaController {
    @FXML
    private Pane container1;
    @FXML
    private Pane paneWilcommen;
    @FXML
    public void initialize(){
//        "En caso de querer tocar el javafx xon duh java, entonces descomentar estas lineas y comentar el segundo paneWilcommen raro"

//        Stop[] stops = new Stop[] {
//                new Stop(0, Color.DARKSLATEBLUE),
//                new Stop(1, Color.DARKRED)
//        };
//
//        LinearGradient gradiente = new LinearGradient(
//                0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops
//        );
//
//        paneWilcommen.setBackground(new Background(new BackgroundFill(gradiente, null, null)));
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
