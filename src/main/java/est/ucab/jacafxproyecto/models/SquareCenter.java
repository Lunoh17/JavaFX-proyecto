package est.ucab.jacafxproyecto.models;

import est.ucab.jacafxproyecto.controllers.FichaController;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Clase que representa la casilla central del tablero en el juego.
 * Desde esta casilla se puede salir hacia cualquiera de los seis brazos (rayos) que
 * conforman el tablero.
 * <p>
 * Implementa las interfaces {@link brazo} y {@link CategoryQuestion}.
 */
public class SquareCenter extends Square implements brazo, CategoryQuestion {

    /**
     * Arreglo que contiene los 6 brazos del tablero, cada uno compuesto por varias casillas de categoría.
     */
    public SquareCategory[] rayos = new SquareCategory[6];

    /**
     * Dibuja la casilla central en la consola.
     *
     * @return Una representación textual de la casilla central.
     */
    @Override
    public String paint() {
        if (cantidadFichas > 0) {
            return "/────\\\n" +
                    "│  " + cantidadFichas + " │\n" +
                    "\\────/";
        }
        return "/────\\\n" +
                "│    │\n" +
                "\\────/";
    }

    /**
     * Permite al jugador seleccionar una de las seis rutas posibles para salir del centro.
     *
     * @param jugador Ficha del jugador.
     * @return Índice del rayo seleccionado (0 a 5).
     */
    @Override
    public int action(Ficha jugador) {
        List<String> choices = new ArrayList<>();
        choices.add("0. Derecha");
        choices.add("1. Abajo a la Derecha");
        choices.add("2. Abajo a la Izquierda");
        choices.add("3. Izquierda");
        choices.add("4. Arriba a la Izquierda");
        choices.add("5. Arriba a la Derecha");

        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Selección de Ruta");
        dialog.setHeaderText("Tienes 6 posibles rutas, ¿a dónde te quieres mover?");
        dialog.setContentText("Elige tu ruta:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String selected = result.get();
            return Integer.parseInt(selected.substring(0, 1));
        }
        return 0; // Default or cancel option
    }

    @Override
    public Square reaction(Ficha jugador) {
        return this;
    }

    /**
     * Lógica para salir del centro hacia un brazo específico del tablero.
     *
     * @param move    Número de pasos a mover.
     * @param exit    Índice del brazo por el cual salir (0 a 5).
     * @param jugador Ficha del jugador.
     * @return Casilla destino después del movimiento.
     */
    public Square salir(int move, int exit, Ficha jugador) {
        if (move < 1 || move > 6) {
            throw new IllegalArgumentException("El movimiento debe estar entre 1 y 6");
        }
        SquareCategory ciclar = this.rayos[exit];
        Square salida = ciclar;
        if (this.cantidadFichas > 0) {
            this.cantidadFichas--;
            for (int i = 1; i < move; i++) {
                if (ciclar.next instanceof SquareCategory sig) {
                    ciclar = sig;
                    salida = ciclar;
                } else if (ciclar.next instanceof SquareRayo sig) {
                    salida = sig;
                    jugador.salido = true;
                }
            }
        } else {
            throw new IllegalStateException("No hay cantidadFichas en el centro para salir.");
        }
        return salida;
    }

    /**
     * Constructor de la clase. Inicializa todos los rayos del tablero, conectando
     * las casillas en un patrón predeterminado.
     *
     * @param jugadores Número de jugadores para determinar cantidad inicial de fichas.
     */
    public SquareCenter(int jugadores) {
        int position = 0;
        this.position = position++;
        int currentCategory = 0;
        this.cantidadFichas = jugadores;
        var categorias = Category.values();
        SquareCategory lastSquare = null;

        for (int i = 0; i < this.rayos.length; i++) {
            this.rayos[i] = new SquareCategory(categorias[i], null, this, position++);
            SquareCategory squareActual = this.rayos[i];
            currentCategory = i + 1;
            if (categorias.length == currentCategory) currentCategory = 0;

            for (int j = 1; j < 5; j++) {
                squareActual.next = new SquareCategory(categorias[currentCategory++], null, squareActual, position++);
                if (categorias.length == currentCategory) currentCategory = 0;
                squareActual = (SquareCategory) squareActual.next;
            }

            squareActual.next = new SquareRayo(categorias[currentCategory++], null, lastSquare, squareActual, position++);
            if (lastSquare != null) lastSquare.next = squareActual.next;
            SquareRayo rayoActual = (SquareRayo) squareActual.next;

            if (categorias.length == currentCategory) currentCategory = 0;
            squareActual = rayoActual.next = new SquareCategory(categorias[currentCategory++], null, rayoActual, position++);
            if (categorias.length == currentCategory) currentCategory = 0;
            for (int j = 1; j < 6; j++) {
                if (j == 1 || j == 4) {
                    squareActual.next = new SquareSpecial(null, squareActual, position++);
                    ((SquareSpecial) squareActual.next).next = new SquareCategory(categorias[currentCategory++], null, squareActual.next, position++);
                    j++;
                    if (categorias.length == currentCategory) currentCategory = 0;
                    squareActual = ((SquareSpecial) squareActual.next).next;
                } else {
                    squareActual.next = new SquareCategory(categorias[currentCategory++], null, squareActual, position++);
                    if (categorias.length == currentCategory) currentCategory = 0;
                    squareActual = (SquareCategory) squareActual.next;
                }
            }

            lastSquare = squareActual;
        }

        // Conecta el último SquareRayo al final del recorrido
        SquareCategory iter = this.rayos[0];
        for (int i = 1; i < 5; i++) {
            iter = (SquareCategory) iter.next;
        }
        if (iter.next instanceof SquareRayo a) {
            a.previous = lastSquare;
            lastSquare.next = a;
        }
    }

    /**
     * Reacción al caer en la casilla central: permite seleccionar una categoría
     * y responder una pregunta para intentar ganar.
     *
     * @param jugador  Ficha del jugador.
     * @param questions Banco de preguntas.
     * @param fichaController Controlador de la ficha actual.
     * @return true si la respuesta fue correcta, false en caso contrario.
     */
    public boolean reaction(Ficha jugador, Questions questions) {
        Category[] categorias = Category.values();
        List<String> choices = new ArrayList<>();
        for (Category c : categorias) {
            choices.add(c.name());
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Selección de Categoría");
        dialog.setHeaderText("Seleccione una categoría:");
        dialog.setContentText("Categoría:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            Category categoria = Category.valueOf(result.get());
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
                jugador.gano = true;
                return true;
            } else {
                alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, "Respuesta incorrecta.");
                alert.setHeaderText(null);
                alert.showAndWait();
                return false;
            }
        }
        return false;
    }

    /**
     * Revisa si la respuesta ingresada por el jugador es correcta.
     *
     * @param question Pregunta a evaluar.
     * @return true si la respuesta es válida; false si es incorrecta.
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
     * Acción al entrar a la casilla central. No hay movimiento adicional desde aquí.
     *
     * @param move    Número de pasos a mover.
     * @param exit    Dirección de entrada.
     * @param jugador Ficha del jugador.
     * @return Esta misma casilla.
     */
    @Override
    public Square entrar(int move, int exit, Ficha jugador) {
        return this;
    }

}
