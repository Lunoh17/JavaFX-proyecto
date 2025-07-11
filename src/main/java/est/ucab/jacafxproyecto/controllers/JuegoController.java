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
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;


public class JuegoController {

    @FXML
    private Group board;
    @FXML
    public GridPane grid00;
    @FXML
    private AnchorPane paneWilcommen;

    @FXML
    private AnchorPane container1;

    @FXML
    private StackPane g00;
    @FXML
    private StackPane g01;
    @FXML
    private StackPane g02;
    @FXML
    private StackPane g03;
    @FXML
    private StackPane g04;

    /**
     * Lista de jugadores que participan en el juego.
     */
    public ArrayList<Ficha> jugadores = new ArrayList<Ficha>();

    private FichaController[] fichaControllers;
    private ArrayList<Node> fichaNodes = new ArrayList<>(); // Store ficha nodes
    /**
     * El centro del tablero.
     */
    private SquareCenter centro;

    /**
     * Número máximo de jugadores en el juego.
     */
    int MAX_PLAYERS = 6;

    /**
     * Indicador de si el juego ha terminado o no.
     */
    boolean ganador = false;
    /** Índice del jugador actual en turno. */
    private int jugadorActual = 0;

    /**
     * Carpeta de inicio del sistema donde se guardarán los datos de los jugadores.
     */
    String homeFolder = System.getProperty("user.home");

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
                if (grid00 != null)
                    grid00.add(fichaNode, idx % 2, idx / 2);
            } catch (IOException e) {
                e.printStackTrace();
            }
            idx++;
        }
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

    }

    /**
     * Guarda el estado actual de las fichas de los jugadores en un archivo JSON.
     *
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    private void saveFichaJson() throws IOException {
        String destinyFolder = homeFolder + File.separator + ".config";
        File destinyFolderFile = new File(destinyFolder);
        if (!destinyFolderFile.exists()) {
            boolean created = destinyFolderFile.mkdir();
            if (!created)
                throw new IOException();
        }
        Gson gson = new Gson();

        Square listaSquare[]= new Square[MAX_PLAYERS];
        int contador = 0;
        for (Ficha fa:jugadores){
            listaSquare[contador++]=fa.posicion;
            fa.posicion=null;
        }
        String json = gson.toJson(this.jugadores);
        File data = new File(destinyFolder + File.separator + "fichas.json");
        contador=0;
        for (Ficha fa:jugadores){
            fa.posicion=listaSquare[contador++];
        }
        try (FileWriter writer = new FileWriter(data)) {
            writer.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadFichaJson() {
        Gson gson = new Gson();
        String destinyFolder = homeFolder + File.separator + ".config";
        File destinyFolderFile = new File(destinyFolder);
        if (!destinyFolderFile.exists()) {
            boolean created = destinyFolderFile.mkdir();
            if (!created) {
                throw new RuntimeException();
            }
        }
        var a = new File(destinyFolderFile + File.separator + "fichas.json");
        if (!(a.exists())) {
            try {
                boolean created = a.createNewFile();
                if (!created)
                    throw new IOException();
                this.jugadores = new ArrayList<Ficha>();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (FileReader r = new FileReader(destinyFolderFile + File.separator + "fichas.json")) {
                BufferedReader bufferedReader = new BufferedReader(r);
                Type listType = new TypeToken<ArrayList<Ficha>>() {
                }.getType();
                jugadores = gson.fromJson(bufferedReader, listType);
            } catch (IOException e) {
                throw new RuntimeException("Error al leer el archivo JSON", e);
            }
        }

    }

    public void cargarPositions(){
        loadFichaJson();
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

        for(int i=0; i<jugadores.size(); i++){
            Ficha ficha = jugadores.get(i);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/est/ucab/jacafxproyecto/ficha.fxml"));
                Node fichaNode = loader.load();
                FichaController fichaController = loader.getController();
                fichaController.setJugador(ficha);
                fichaControllers[i] = fichaController;
                fichaNodes.add(fichaNode);
                if (grid00 != null) {
                    grid00.add(fichaNode, i % 2, i / 2);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            for(Square squareActual: centro.rayos){
                for (int j = 0; j <13 ; j++) {
                    if (squareActual instanceof  SquareRayo sr){
                        if (ficha.getPosition()== sr.getPosition()) {
                            ficha.posicion = sr;
                            ficha.positionTable = sr.getPosition();
                            ficha.posicion.cantidadFichas++;
                            break;
                        }
                        squareActual=(Square) sr.getNext();
                    }else if (squareActual instanceof SquareCategory sC)
                    {
                        if (ficha.getPosition()== sC.getPosition()) {
                            ficha.posicion = sC;
                            ficha.positionTable = sC.getPosition();
                            ficha.posicion.cantidadFichas++;
                            break;
                        }
                        squareActual=((Square) sC.getNext());
                    } else if (squareActual instanceof SquareSpecial sS) {
                        if (ficha.getPosition()== sS.getPosition()) {
                            ficha.posicion = sS;
                            ficha.positionTable = sS.getPosition();
                            ficha.posicion.cantidadFichas++;
                            break;
                        }
                        squareActual=((Square) sS.getNext());
                    }

                }
            }
        }
    }

    /**
     * Handle a single player's turn, invoked by UI button.
     */
    private void handleTurn() {
        if (ganador) {
            new Alert(Alert.AlertType.INFORMATION, "Game over. Winner: " + jugadores.get(jugadorActual).getNickName()).showAndWait();
            return;
        }
        // clear console and redraw
        System.out.print("\033[H\033[2J");
        System.out.flush();
        this.printBoard();
        System.out.println("Turno del jugador: " + jugadores.get(jugadorActual).getNickName());
        System.out.println("estadisticas: ");
        System.out.println("victorias: "+ jugadores.get(jugadorActual).getUsuario().getVictory());
        for(int j=0; j<categories.length; j++) {
            System.out.println("categoria: "+categories[j]+" hay un total de respondidas: "+ jugadores.get(jugadorActual).triangulos[j]);
        }
        boolean correctAnswer = false;
        ganador = jugadores.get(jugadorActual).avanzar(questionsGame);
        this.printBoard();
        if (ganador) {
            jugadores.get(jugadorActual).getUsuario().setVictory(jugadores.get(jugadorActual).getUsuario().getVictory() + 1);
        }
        this.printBoard();
        jugadorActual = (jugadorActual + 1) % jugadores.size();
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

    /**
     * Guarda el estado de la partida (posiciones) en fichas.json dentro de resources.
     */
    public void guardarEstadoPartida() {
        try {
            List<Square> stash = new ArrayList<>();
            for (Ficha f : jugadores) {
                stash.add(f.posicion);
                if (f.posicion != null) {
                    f.positionTable = f.posicion.getPosition();
                    f.posicion = null;
                }
            }
            Gson gson = new Gson();
            String json = gson.toJson(jugadores);
            URL resUrl = getClass().getResource("/est/ucab/jacafxproyecto/fichas.json");
            Path fichasPath = Paths.get(resUrl.toURI());
            Files.write(fichasPath, json.getBytes(StandardCharsets.UTF_8));
            for (int i = 0; i < jugadores.size(); i++) {
                jugadores.get(i).posicion = stash.get(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(AlertType.ERROR, "Error al guardar la partida: " + e.getMessage()).showAndWait();
        }
    }

    /**
     * Carga la partida guardada desde fichas.json dentro de resources.
     */
    public void cargarEstadoGuardado() {
        try {
            // Leer JSON de posiciones guardadas
            URL resUrl = getClass().getResource("/est/ucab/jacafxproyecto/fichas.json");
            Path fichasPath = Paths.get(resUrl.toURI());
            Type listType = new TypeToken<ArrayList<Ficha>>(){}.getType();
            ArrayList<Ficha> saved = new Gson().fromJson(Files.newBufferedReader(fichasPath, StandardCharsets.UTF_8), listType);
            // Reset jugadores y nodes
            jugadores.clear();
            fichaNodes.clear();
            if (grid00 != null) grid00.getChildren().clear();
            fichaControllers = new FichaController[saved.size()];
            // Reconstruir centro
            this.centro = new SquareCenter(saved.size());
            // Assign positions and UI nodes
            for (int i = 0; i < saved.size(); i++) {
                Ficha ficha = saved.get(i);
                // localizar Square segun posición
                Square found = centro;
                outer:
                for (Square s : centro.rayos) {
                    Square curr = s;
                    do {
                        if (curr.getPosition() == ficha.getPosition()) { found = curr; break outer; }
                        if (curr instanceof SquareRayo sr) curr = sr.getNext();
                        else if (curr instanceof SquareCategory sc) curr = sc.getNext();
                        else if (curr instanceof SquareSpecial ss) curr = ss.getNext();
                        else break; // no next
                    } while (curr != s);
                    inner: ;
                }
                ficha.posicion = found;
                ficha.positionTable = found.getPosition();
                found.sumarCantidadFichas();
                jugadores.add(ficha);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/est/ucab/jacafxproyecto/ficha.fxml"));
                Node node = loader.load();
                FichaController ctrl = loader.getController();
                ctrl.setJugador(ficha);
                fichaControllers[i] = ctrl;
                fichaNodes.add(node);
                if (grid00 != null) grid00.add(node, i % GRID_COLS, i / GRID_COLS);
            }
            printBoard();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(AlertType.ERROR, "Error al cargar la partida: " + e.getMessage()).showAndWait();
        }
    }

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

    public void printBoard() {
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
                    GridPane gp = sp.getChildren().stream()
                        .filter(c -> c instanceof GridPane)
                        .map(c -> (GridPane)c)
                        .findFirst().orElse(null);
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
        handleTurn();
    }
} // end of class JuegoController
