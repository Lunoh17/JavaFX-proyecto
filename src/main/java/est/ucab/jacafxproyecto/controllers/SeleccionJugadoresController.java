package est.ucab.jacafxproyecto.controllers;

import est.ucab.jacafxproyecto.models.Jugadores;
import est.ucab.jacafxproyecto.models.Usuario;
import est.ucab.jacafxproyecto.models.Validator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class SeleccionJugadoresController {
    public Button aceptar;
    public VBox players;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private AnchorPane paneWilcommen;
    @FXML
    private AnchorPane container1;
    @FXML
    private AnchorPane avatars;

    private Set<Usuario> jugadores = new LinkedHashSet<Usuario>();
    private List<Usuario> listaUsuarios = new ArrayList<Usuario>();

    public void switchToMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/est/ucab/jacafxproyecto/menu-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToJuego(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/est/ucab/jacafxproyecto/juego-view.fxml"));
        Parent root = loader.load();
        JuegoController controller = loader.getController();
        controller.setJugadoresFromUsuarios(jugadores);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onClickSalir() {
        Platform.exit();  // Cerrar la aplicación
    }

    @FXML
    public void initialize() {
        listaUsuarios.clear();
        listaUsuarios.addAll(Validator.loadUsuariosJson());
        jugadores.clear();
        // Optionally, update the UI here to show the loaded users

        System.out.println(listaUsuarios);
        // Cambia los id de los primeros 12 botones existentes en avatars
        int max = Math.min(12, Math.min(listaUsuarios.size(), avatars.getChildren().size()));
        for (int i = 0; i < max; i++) {
            if (avatars.getChildren().get(i) instanceof Button) {
                Button btn = (Button) avatars.getChildren().get(i);
                btn.setId(listaUsuarios.get(i).userName);
                btn.setText(listaUsuarios.get(i).userName); // opcional: muestra el nombre
                btn.setOnAction(this::añadirJugador);
            }
        }
    }

    private void actualizarVBoxJugadores() {
        players.getChildren().clear();
        for (Usuario u : jugadores) {
            players.getChildren().add(new Label(u.userName));
        }
    }

    @FXML
    public void añadirJugador(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource();
        var idActual = btn.getId();
        if (!idActual.isEmpty()) {
            System.out.println("Clicked button ID: " + btn.getId());

            var usuarioOpt = listaUsuarios.stream().filter(u -> u.userName.equals(idActual)).findFirst();
            usuarioOpt.ifPresent(usuario -> {
                if (jugadores.add(usuario)) { // solo añade si no está
                    actualizarVBoxJugadores();
                }
            });
        }

    }
}
