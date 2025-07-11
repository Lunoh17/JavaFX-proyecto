package est.ucab.jacafxproyecto.models;

import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Clase que representa una casilla especial del tablero llamada "SquareRayo".
 * Esta casilla permite movimiento bidireccional y sirve de conexión con el centro del tablero.
 * Además, contiene lógica para responder preguntas de una categoría específica.
 */
public class SquareRayo extends Square implements movimientoBidireccional, CategoryQuestion {

    protected Category categoria;
    protected SquareCategory next;
    protected SquareCategory previous;
    protected SquareCategory toCenter;

    /**
     * Constructor de la clase SquareRayo.
     *
     * @param categoria Categoría asignada a esta casilla.
     * @param next Casilla siguiente.
     * @param previous Casilla anterior.
     * @param toCenter Casilla que conecta con el centro del tablero.
     * @param position Posición de esta casilla en el tablero.
     */
    public SquareRayo(Category categoria, SquareCategory next, SquareCategory previous, SquareCategory toCenter, int position) {
        this.categoria = categoria;
        this.position = position;
        this.next = next;
        this.previous = previous;
        this.toCenter = toCenter;
    }

    /**
     * Dibuja la representación visual de la casilla.
     *
     * @return Cadena que representa la casilla visualmente.
     */
    @Override
    public String paint() {
        if (cantidadFichas > 0) {
            return "┌────┐\n" +
                    "│R " + cantidadFichas + " │\n" +
                    "└────┘";
        }
        return "┌────┐\n" +
                "│ R  │\n" +
                "└────┘";
    }

    /**
     * Define la acción del jugador al llegar a esta casilla.
     *
     * @param jugador Ficha del jugador.
     * @return Entero que indica la dirección del movimiento: 1 (adelante), 0 (atrás), 2 (al centro).
     */
    @Override
    public int action(Ficha jugador) {
        if (jugador.triangulo()) {
            jugador.entrado = true;
            return 2;
        } else {
            List<String> choices = new ArrayList<>();
            choices.add("0. Atrás");
            choices.add("1. Adelante");

            ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(1), choices);
            dialog.setTitle("Selección de Ruta");
            dialog.setHeaderText("¿Quieres ir hacia adelante o hacia atrás?");
            dialog.setContentText("Elige tu ruta:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String selected = result.get();
                return Integer.parseInt(selected.substring(0, 1));
            }
            return 1; // Default or cancel option
        }
    }

    /**
     * Realiza el movimiento de salida desde esta casilla.
     *
     * @param move Número de pasos a mover.
     * @param exit Dirección del movimiento (0: atrás, 1: adelante, 2: centro).
     * @param jugador Ficha del jugador.
     * @return Casilla destino tras el movimiento.
     */
    public Square salir(int move, int exit, Ficha jugador) {
        Square iter = this;
        for (int i = 0; i < move; i++) {
            if (exit == 1 && iter instanceof movimientoBidireccional next)
                iter = next.getNext();
            else if (exit == 0 && iter instanceof movimientoBidireccional prev)
                iter = prev.getPrevious();
            else if (exit == 2)
                iter = this.toCenter;
        }
        return iter;
    }

    /**
     * Mueve al jugador a lo largo del rayo en la dirección indicada.
     *
     * @param move Número de pasos.
     * @param exit Dirección (0: atrás, 1: adelante, 2: centro).
     * @param jugador Ficha del jugador.
     * @return Casilla resultante después del movimiento.
     */
    public Square movimiento(int move, int exit, Ficha jugador) {
        Square iter = this;
        this.cantidadFichas--;
        for (int i = 0; i < move; i++) {
            if (exit == 1 && iter instanceof movimientoBidireccional next)
                iter = next.getNext();
            else if (exit == 0 && iter instanceof movimientoBidireccional prev)
                iter = prev.getPrevious();
            else if (exit == 2)
                iter = this.toCenter;
        }
        return iter;
    }

    /**
     * Devuelve la categoría de esta casilla.
     *
     * @return Categoría asignada.
     */
    public Category getCategoria() {
        return categoria;
    }

    /**
     * Devuelve la casilla que conecta con el centro.
     *
     * @return Casilla del centro conectada.
     */
    public SquareCategory getToCenter() {
        return toCenter;
    }

    /**
     * Devuelve la siguiente casilla en el rayo.
     *
     * @return Casilla siguiente.
     */
    public Square getNext() {
        return this.next;
    }

    /**
     * Devuelve la casilla anterior en el rayo.
     *
     * @return Casilla anterior.
     */
    public Square getPrevious() {
        return previous;
    }

    /**
     * Reacción al caer en esta casilla (sin preguntas). Retorna null por compatibilidad.
     *
     * @param jugador Ficha del jugador.
     * @return null (sin acción).
     */
    @Override
    public Square reaction(Ficha jugador) {
        return null;
    }

    /**
     * Evalúa si la respuesta del jugador a la pregunta es correcta.
     *
     * @param question Pregunta a responder.
     * @return true si la respuesta es correcta; false en caso contrario.
     */
    public boolean revisarRespuesta(Question question) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Respuesta");
        dialog.setHeaderText(question.getQuestion()); // Show the actual question
        dialog.setContentText("Ingrese su respuesta:");
        Optional<String> result = dialog.showAndWait();
        String respuesta = result.orElse("");
        return respuesta.equalsIgnoreCase(question.getAnswer())
                || question.getAnswer().toLowerCase().contains(respuesta.toLowerCase())
                || respuesta.toLowerCase().contains(question.getAnswer().toLowerCase());
    }

    /**
     * Lógica de reacción cuando el jugador cae en esta casilla: se presenta una pregunta.
     *
     * @param jugador Ficha del jugador.
     * @param questions Banco de preguntas.
     * @return Casilla resultante luego de responder la pregunta.
     */
    @Override
    public boolean reaction(Ficha jugador, Questions questions) {
        Question question = questions.getRandomQuestion(categoria);

        if (question == null) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION, "No hay preguntas disponibles para esta categoría.");
            alert.setHeaderText(null);
            alert.showAndWait();
            return false;
        }

        boolean respuestaCorrecta = revisarRespuesta(question);

        javafx.scene.control.Alert alert;
        if (respuestaCorrecta) {
            alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION, "¡Respuesta correcta!");
            alert.setHeaderText(null);
            alert.showAndWait();
            jugador.incrementarPuntos(categoria);
            return true;
        } else {
            alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, "Respuesta incorrecta.");
            alert.setHeaderText(null);
            alert.showAndWait();
            return false;
        }
    }
}
