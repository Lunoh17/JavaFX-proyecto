package est.ucab.jacafxproyecto.controllers;
import java.io.IOException;
import java.util.ArrayList;

import est.ucab.jacafxproyecto.models.LoadUsuario;
import est.ucab.jacafxproyecto.models.Usuario;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class EstadisticasJugadoresController {
    public AnchorPane paneWilcommen;
    public AnchorPane container1;
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    public void Ranking(){
        LoadUsuario tops = new LoadUsuario();
        ArrayList<Usuario> estadisticsUsers= tops.topTier();
        TableView<Usuario> toptiers= new TableView<>();
        TableColumn<Usuario, String> colNombre = new TableColumn<>("nombre");
        TableColumn<Usuario, Integer> colPartidas = new TableColumn<>("partidas");
        TableColumn<Usuario, Integer> colVictorias = new TableColumn<>("victorias");
        TableColumn<Usuario, Integer> colDerrotas = new TableColumn<>("derrotas");
        TableColumn<Usuario, Integer> colTiempo = new TableColumn<>("tiempo respuestas");
        TableColumn<Usuario, Integer> colCategoryDeporte = new TableColumn<>("deporte ");
        TableColumn<Usuario, Integer> colCategoryGeografia = new TableColumn<>("Geografia ");
        TableColumn<Usuario, Integer> colCategoryHistoria = new TableColumn<>("Historia ");
        TableColumn<Usuario, Integer> colCategoryCiencias = new TableColumn<>("ciencias Naturaleza ");
        TableColumn<Usuario, Integer> colCategoryArte = new TableColumn<>("Arte ");
        TableColumn<Usuario, Integer> colCategoryEntretenimiento = new TableColumn<>("Entretenimiento ");
        toptiers.getColumns().addAll(colNombre, colPartidas, colVictorias, colDerrotas, colTiempo, colCategoryDeporte, colCategoryArte, colCategoryEntretenimiento, colCategoryCiencias, colCategoryGeografia,colCategoryHistoria);

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nickName"));
        colPartidas.setCellValueFactory(new PropertyValueFactory<>("partidas"));
        colVictorias.setCellValueFactory(new PropertyValueFactory<>("victory"));
        colDerrotas.setCellValueFactory(new PropertyValueFactory<>("loses"));
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("totalTimeQuestions"));


        ObservableList<Usuario> estadisticsObservable = FXCollections.observableArrayList(estadisticsUsers);
        for(int i=0;i<estadisticsUsers.size(); i++){

        }


    }


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

