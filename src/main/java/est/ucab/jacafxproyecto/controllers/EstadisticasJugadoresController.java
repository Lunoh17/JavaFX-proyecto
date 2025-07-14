package est.ucab.jacafxproyecto.controllers;
import java.io.IOException;
import java.util.List;


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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EstadisticasJugadoresController {
    public AnchorPane paneWilcommen;
    public AnchorPane container1;
    public TableColumn columna1;
    public TableColumn columna2;
    public TableColumn columna3;
    public TableColumn columna4;
    public TableColumn columna5;
    public TableColumn columna6;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TableView<Usuario> toptiers;

    @FXML
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
        toptiers.setStyle("""
                -fx-background-color: #ffd2e3;
                """);
        columna1.setStyle("""
                -fx-background-color: #cfb0e3;
                """);

        // setup statistics table
        toptiers.getColumns().clear();
        LoadUsuario tops = new LoadUsuario();
        tops.topTier();
        List<Usuario> estadisticsUsers = tops.usuarios;
        // define columns
        TableColumn<Usuario, String> colNombre = new TableColumn<>("userName");
        TableColumn<Usuario, Integer> colPartidas = new TableColumn<>("partidas");
        TableColumn<Usuario, Integer> colVictorias = new TableColumn<>("victory");
        TableColumn<Usuario, Integer> colDerrotas = new TableColumn<>("loses");
        TableColumn<Usuario, Double> colTiempo = new TableColumn<>("totalTimeQuestions");
        TableColumn<Usuario, Integer> colCategoryDeporte = new TableColumn<>("answeredDeporte");
        TableColumn<Usuario, Integer> colCategoryGeografia = new TableColumn<>("answeredGeografia");
        TableColumn<Usuario, Integer> colCategoryHistoria = new TableColumn<>("answeredHistoria");
        TableColumn<Usuario, Integer> colCategoryCiencias = new TableColumn<>("answeredCiencia");
        TableColumn<Usuario, Integer> colCategoryArte = new TableColumn<>("answeredArte");
        TableColumn<Usuario, Integer> colCategoryEntretenimiento = new TableColumn<>("answeredEntretenimiento");
        toptiers.getColumns().addAll(colNombre, colPartidas, colVictorias, colDerrotas, colTiempo, colCategoryDeporte, colCategoryGeografia, colCategoryHistoria, colCategoryCiencias, colCategoryArte, colCategoryEntretenimiento);
        // cell value factories
        colNombre.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colPartidas.setCellValueFactory(new PropertyValueFactory<>("partidas"));
        colVictorias.setCellValueFactory(new PropertyValueFactory<>("victory"));
        colDerrotas.setCellValueFactory(new PropertyValueFactory<>("loses"));
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("totalTimeQuestions"));
        colCategoryDeporte.setCellValueFactory(new PropertyValueFactory<>("answeredDeporte"));
        colCategoryGeografia.setCellValueFactory(new PropertyValueFactory<>("answeredGeografia"));
        colCategoryHistoria.setCellValueFactory(new PropertyValueFactory<>("answeredHistoria"));
        colCategoryCiencias.setCellValueFactory(new PropertyValueFactory<>("answeredCiencia"));
        colCategoryArte.setCellValueFactory(new PropertyValueFactory<>("answeredArte"));
        colCategoryEntretenimiento.setCellValueFactory(new PropertyValueFactory<>("answeredEntretenimiento"));
        // load data
        toptiers.setItems(FXCollections.observableArrayList(estadisticsUsers));
    }
}
