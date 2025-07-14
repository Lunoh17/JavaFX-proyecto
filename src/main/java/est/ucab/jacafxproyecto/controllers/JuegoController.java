package est.ucab.jacafxproyecto.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import est.ucab.jacafxproyecto.models.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;


public class JuegoController {
    /**
     * Definimos el tamaño de cada celda en el tablero.
     */
    private static final int CELL_WIDTH = 6;
    private static final int CELL_HEIGHT = 4;
    /**
     * Número de columnas y filas en el tablero.
     */
    private static final int GRID_COLS = 11;
    private static final int GRID_ROWS = 8;
    @FXML
    public VBox vBoxJugadores;
    @FXML
    public GridPane grid00;
    public AnchorPane dado;
    private DadoController dadoController;

    private Stage stage;
    private Scene scene;
    /**
     * Lista de jugadores que participan en el juego.
     */
    public ArrayList<Ficha> jugadores = new ArrayList<Ficha>();
    /**
     * Número máximo de jugadores en el juego.
     */
    int MAX_PLAYERS = 6;
    /**
     * Indicador de si el juego ha terminado o no.
     */
    boolean ganador = false;
    /**
     * Carpeta de inicio del sistema donde se guardarán los datos de los jugadores.
     */
    String homeFolder = System.getProperty("user.dir");
    @FXML
    private Group board;
    @FXML
    private AnchorPane paneWilcommen;
    @FXML
    private AnchorPane container1;
    private FichaController[] fichaControllers;
    private ArrayList<Node> fichaNodes = new ArrayList<>(); // Store ficha nodes
    /**
     * El centro del tablero.
     */
    private SquareCenter centro;
    /**
     * Índice del jugador actual en turno.
     */
    private int jugadorActual = 0;
    // game state and utilities
    private Questions questionsGame = Validator.loadJson();
    private Random rng = new Random();
    private Category[] categories = Category.values();

    /**
     * Llena la lista de jugadores a partir de un conjunto de usuarios.
     *
     * @param usuarios Conjunto de usuarios a convertir en fichas.
     */
    public void setJugadoresFromUsuarios(Set<Usuario> usuarios) {
        if (usuarios.size() > MAX_PLAYERS) {
            throw new IllegalArgumentException("El número de jugadores no puede ser mayor a " + MAX_PLAYERS);
        }
        this.centro = new SquareCenter(usuarios.size());
        fichaControllers = new FichaController[usuarios.size()];
        jugadores.clear();
        fichaNodes.clear(); // Clear previous nodes
        if (grid00 != null) {
            grid00.getChildren().clear();
        }
        int idx = 0;
        for (Usuario usuario : usuarios) {
            Ficha ficha = new Ficha(usuario.userName, usuario, centro);
            jugadores.add(ficha);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/est/ucab/jacafxproyecto/ficha.fxml"));
                Node fichaNode = loader.load();
                // Puedes pasar la ficha al controller si lo necesitas:
                FichaController fichaController = loader.getController();
                fichaController.setJugador(ficha);
                fichaControllers[idx] = fichaController;
                fichaNodes.add(fichaNode); // Store the node
                if (grid00 != null) grid00.add(fichaNode, idx % 2, idx / 2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            idx++;
        }
        // update accordion of players and their triangle scores
        updatePlayerAccordion();
    }

    @FXML
    public void initialize() {
        paneWilcommen.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #5270ff, #fe66c4);");
        container1.setStyle("""
                -fx-border-color: black;\s
                    -fx-border-width: 12;\s
                    -fx-border-radius: 15;\s
                    -fx-border-style: solid;
                    -fx-background-color: #ebdcff;
                    -fx-background-radius: 20;""");
        // Load and display players with their triangle scores on startup
        cargarPositions();
        if (jugadores.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "No hay jugadores cargados. Por favor, añade jugadores.").showAndWait();
        } else {
            jugadorActual = 0; // Reset to first player
            printBoard(); // Initial board print
        }
        try{
            var loader = new  FXMLLoader(getClass().getResource("/est/ucab/jacafxproyecto/Dado-view.fxml"));
            Parent f = loader.load();
            dado.getChildren().clear(); // Clear previous content
            dadoController = loader.getController();
            dadoController.root = dado; // Set the root for the DadoController
            dadoController.root.setOnMouseClicked(event -> {
                dadoController.rollDice(event);
                handleTurn(dadoController.getRandomNumber()); // Handle turn after rolling the dice
            });
            dado.getChildren().add(f); // Add the new Dado view

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void cargarPositions() {
        jugadores = DBController.loadFichaJson();
        if (jugadores == null) {
            jugadores = new ArrayList<>();
            return; // No hay jugadores que cargar
        }
        this.centro = new SquareCenter(this.jugadores.size());
        fichaControllers = new FichaController[jugadores.size()];
        fichaNodes.clear();
        if (grid00 != null) {
            grid00.getChildren().clear();
        }
        for (int i = 0; i < jugadores.size(); i++) {
            Ficha ficha = jugadores.get(i);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/est/ucab/jacafxproyecto/ficha.fxml"));
                Node fichaNode = loader.load();
                FichaController fichaController = loader.getController();
                fichaController.setJugador(ficha);
                for (int sector = 0; sector < ficha.triangulos.length; sector++) {
                    if (ficha.triangulos[sector] > 0) {
                        fichaController.resaltarSector(sector + 1);
                    }
                }
                fichaControllers[i] = fichaController;
                fichaNodes.add(fichaNode);
                if (grid00 != null) {
                    grid00.add(fichaNode, i % 2, i / 2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Square squareActual : centro.rayos) {
                for (int j = 0; j < 13; j++) {
                    if (squareActual instanceof SquareRayo sr) {
                        if (ficha.getPosition() == sr.getPosition()) {
                            ficha.posicion = sr;
                            ficha.positionTable = sr.getPosition();
                            ficha.posicion.cantidadFichas++;
                            break;
                        }
                        squareActual = (Square) sr.getNext();
                    } else if (squareActual instanceof SquareCategory sC) {
                        if (ficha.getPosition() == sC.getPosition()) {
                            ficha.posicion = sC;
                            ficha.positionTable = sC.getPosition();
                            ficha.posicion.cantidadFichas++;
                            break;
                        }
                        squareActual = ((Square) sC.getNext());
                    } else if (squareActual instanceof SquareSpecial sS) {
                        if (ficha.getPosition() == sS.getPosition()) {
                            ficha.posicion = sS;
                            ficha.positionTable = sS.getPosition();
                            ficha.posicion.cantidadFichas++;
                            break;
                        }
                        squareActual = ((Square) sS.getNext());
                    }

                }
            }
        }
        // update accordion after loading positions
        updatePlayerAccordion();
        printBoard();
    }

    private void saveFichaJson() throws IOException {
        DBController.saveFichaJson(jugadores, MAX_PLAYERS);
    }

    public void switchToMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/est/ucab/jacafxproyecto/menu-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handle a single player's turn, invoked by UI button.
     */
    private void handleTurn(int dadoValue) {
        if (ganador) {
            new Alert(Alert.AlertType.INFORMATION, "Game over. Winner: " + jugadores.get(jugadorActual).getNickName()).showAndWait();
            return;
        }
        // clear console and redraw
        System.out.print("\033[H\033[2J");
        System.out.flush();
        this.printBoard();
        System.out.println("Turno del jugador: " + jugadores.get(jugadorActual).getNickName());
        // compute stats
        System.out.println("estadísticas: ");
        System.out.println("victorias: " + jugadores.get(jugadorActual).getUsuario().getVictory());
        for (int j = 0; j < categories.length; j++) {
            int count = jugadores.get(jugadorActual).triangulos[j];
            System.out.println("categoria: " + categories[j] + " hay un total de respondidas: " + count);
        }
        // advance and determine next player or win
        int avance = jugadores.get(jugadorActual).avanzar(questionsGame, fichaControllers[jugadorActual], dadoValue);

        this.printBoard();
        if (avance == 2) {
            ganador = true;
            // record victory and show alert
            Ficha winnerFicha = jugadores.get(jugadorActual);
            Usuario winnerUser = winnerFicha.getUsuario();
            winnerUser.setVictory(winnerUser.getVictory() + 1);
            new Alert(Alert.AlertType.INFORMATION, "Game over. Winner: " + winnerFicha.getNickName()).showAndWait();
            // save updated users to JSON
            try {
                ArrayList<Usuario> usuariosToSave = new ArrayList<>();
                // update each user's stats from their ficha
                for (Ficha f : jugadores) {
                    Usuario u = f.getUsuario();
                    // increment total games played
                    u.setPartidas();
                    // increment losses for non-winners
                    if (!u.getUserName().equals(winnerUser.getUserName())) {
                        u.setLoses();
                    }
                    // add triangles per category to user stats
                    int[] tri = f.triangulos;
                    Category[] cats = Category.values();
                    for (int i = 0; i < tri.length; i++) {
                        for (int j = 0; j < tri[i]; j++) {
                            switch (cats[i]) {
                                case GEOGRAFIA -> u.setCategoriesGeografia();
                                case HISTORIA -> u.setCategoriesHistoria();
                                case DEPORTESPASATIEMPO -> u.setCategoriesDeporte();
                                case CIENCIASNATURALEZA -> u.setCategoriesCiencia();
                                case ARTELITERATURA -> u.setCategoriesArte();
                                case ENTRETENIMIENTO -> u.setCategoriesEntretenimiento();
                            }
                        }
                    }
                    usuariosToSave.add(u);
                }
                DBController.saveUsuariosJson(usuariosToSave);
            } catch (IOException e) {
                System.err.println("Error al guardar usuarios: " + e.getMessage());
            }
            return;
        }
        // update current player index: 0-> next, 1-> same
        if (avance == 0) {
            jugadorActual = (jugadorActual + 1) % jugadores.size();
        }
        try {
            saveFichaJson();
        } catch (IOException e) {
            System.err.println(e + " no se pudo guardar el turno");
        }
        // Fix: Check for null before calling paint()
        Square posActual = jugadores.get(jugadorActual).posicion;
        if (posActual != null) {
            System.out.println("Posición actual:\n" + posActual.paint());
        } else {
            System.out.println("Posición actual: (sin posición asignada)");
        }
    }

    public void printBoard() {
        updatePlayerAccordion();
        Platform.runLater(() -> {
            // clear existing tokens
            for (Node n : board.getChildren()) {
                if (n instanceof StackPane sp) {
                    for (Node c : sp.getChildren()) {
                        if (c instanceof GridPane gp) {
                            gp.getChildren().clear();
                        }
                    }
                }
            }
            // place tokens for each player
            for (int i = 0; i < fichaControllers.length; i++) {
                FichaController f = fichaControllers[i];
                int pos = f.getJugador().getPosition();
                String hexId = "#hex" + String.format("%02d", pos);
                Node h = board.lookup(hexId);
                if (h instanceof StackPane sp) {
                    GridPane gp = sp.getChildren().stream().filter(c -> c instanceof GridPane).map(c -> (GridPane) c).findFirst().orElse(null);
                    if (gp != null) {
                        int cols = gp.getColumnConstraints().size();
                        int col = i % cols;
                        int row = i / cols;
                        Node fichaNode = fichaNodes.get(i); // Get the correct node
                        gp.add(fichaNode, col, row); // Add the node, not the controller array
                    }
                }
            }
        });
    }

    /**
     * Initialize game questions before starting turns.
     */
    public void setQuestions(Questions questions) {
        this.questionsGame = questions;
    }

    @FXML
    public void startGame(ActionEvent event) {
        dadoController.rollDice(null);
        handleTurn(dadoController.getRandomNumber());
    }

    /**
     * Populates the vBoxJugadores with an Accordion listing each player's triangle scores.
     */
    private void updatePlayerAccordion() {
        if (vBoxJugadores == null) return;
        vBoxJugadores.getChildren().clear();
        Accordion accordion = new Accordion();
        for (Ficha jugador : jugadores) {
            VBox content = new VBox(5);
            for (Category cat : Category.values()) {
                int count = jugador.triangulos[cat.ordinal()];
                Label label = new Label(cat.name() + ": " + count);
                content.getChildren().add(label);
            }
            TitledPane pane = new TitledPane(jugador.getNickName(), content);
            accordion.getPanes().add(pane);
        }
        vBoxJugadores.getChildren().add(accordion);
    }

    public void handleRegresar(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/est/ucab/jacafxproyecto/bienvenida.fxml"));
            Parent root = loader.load();
            // Get the current stage
            javafx.stage.Stage stage = (javafx.stage.Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Error al regresar a la bienvenida.").showAndWait();
            e.printStackTrace();
        }
    }
} // end of class JuegoController
