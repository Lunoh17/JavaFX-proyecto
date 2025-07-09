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
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;


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

    /**
     * Carpeta de inicio del sistema donde se guardarán los datos de los jugadores.
     */
    String homeFolder = System.getProperty("user.home");

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

        Square listaSquare[] = new Square[MAX_PLAYERS];
        int contador = 0;
        for (Ficha fa : jugadores) {
            listaSquare[contador++] = fa.posicion;
            fa.posicion = null;
        }
        String json = gson.toJson(this.jugadores);
        File data = new File(destinyFolder + File.separator + "fichas.json");
        contador = 0;
        for (Ficha fa : jugadores) {
            fa.posicion = listaSquare[contador++];
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

    public void cargarPositions() {
        loadFichaJson();
        for (int i = 0; i < jugadores.size(); i++) {
            for (Square squareActual : centro.rayos) {
                for (int j = 0; j < 13; j++) {
                    if (squareActual instanceof SquareRayo sr) {
                        if (jugadores.get(i).getPosition() == sr.getPosition()) {
                            jugadores.get(i).posicion = sr;
                            jugadores.get(i).positionTable = sr.getPosition();
                            jugadores.get(i).posicion.sumarCantidadFichas();
                            break;
                        }
                        squareActual = (Square) sr.getNext();
                    } else if (squareActual instanceof SquareCategory sC) {
                        if (jugadores.get(i).getPosition() == sC.getPosition()) {
                            jugadores.get(i).posicion = sC;
                            jugadores.get(i).positionTable = sC.getPosition();
                            jugadores.get(i).posicion.sumarCantidadFichas();
                            break;
                        }
                        squareActual = ((Square) sC.getNext());
                    } else if (squareActual instanceof SquareSpecial sS) {
                        if (jugadores.get(i).getPosition() == sS.getPosition()) {
                            jugadores.get(i).posicion = sS;
                            jugadores.get(i).positionTable = sS.getPosition();
                            jugadores.get(i).posicion.sumarCantidadFichas();
                            break;
                        }
                        squareActual = ((Square) sS.getNext());
                    }

                }
            }
        }
    }

    /**
     * Inicia el juego, realizando los turnos de los jugadores hasta que uno gane.
     *
     * @param scanner   Objeto Scanner para capturar entradas del usuario.
     * @param questions Preguntas que se usarán durante el juego.
     */
    public void startGame(Scanner scanner, Questions questions) {
        System.out.println(jugadores);
        int jugadorActual = 0;
        int enums = 0;
        var categories = Category.values();
        while (!ganador) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            this.printBoard();
            System.out.println("Turno del jugador: " + jugadores.get(jugadorActual).getNickName());
            System.out.println("estadisticas: ");
            System.out.println("victorias: " + jugadores.get(jugadorActual).getUsuario().getVictory());
            for (int j = 0; j < 6; j++) {
                for (int i = 0; i < jugadores.get(jugadorActual).triangulos.length; i++) {
                    if (j == jugadores.get(jugadorActual).triangulos[i]) {
                        enums++;
                    }
                }
                System.out.println("categoria: " + categories[j] + " hay un total de respondidas: " + enums);
                enums = 0;
            }
            ganador = jugadores.get(jugadorActual).avanzar(scanner, questions);
            if (ganador) {
                jugadores.get(jugadorActual).getUsuario().setVictory(jugadores.get(jugadorActual).getUsuario().getVictory() + 1);
            }
            jugadorActual++;
            try {
                saveFichaJson();
            } catch (IOException e) {
                System.err.println(e + "no se pudo guardar el turno");
            }
            if (jugadorActual == jugadores.size()) jugadorActual = 0;
            System.out.println("Posición actual:\n" + jugadores.get(jugadorActual).posicion.paint());
        }
        System.out.println("Jugador actual ganó la partida: " + jugadores.get(jugadorActual).getNickName());
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

    public void startGame(ActionEvent actionEvent) {

            this.startGame(new Scanner(System.in), Validator.loadJson());
    }
}
