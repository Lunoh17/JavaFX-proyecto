package est.ucab.jacafxproyecto.models;

import javafx.scene.control.ChoiceDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Representa una casilla especial del tablero que permite movimiento bidireccional.
 * Puede redirigir al jugador hacia adelante o atrás dependiendo de su decisión.
 */
public class SquareSpecial extends Square implements movimientoBidireccional {

    protected SquareCategory next;
    protected SquareCategory previous;

    /**
     * Constructor de la clase SquareSpecial.
     *
     * @param next     Casilla siguiente.
     * @param previous Casilla anterior.
     * @param position Posición de esta casilla en el tablero.
     */
    public SquareSpecial(SquareCategory next, SquareCategory previous, int position) {
        this.position = position;
        this.next = next;
        this.previous = previous;
    }

    /**
     * Establece la casilla siguiente.
     *
     * @param next Nueva casilla siguiente.
     */
    public void setNext(SquareCategory next) {
        this.next = next;
    }

    /**
     * Devuelve la casilla siguiente.
     *
     * @return Casilla siguiente.
     */
    @Override
    public SquareCategory getNext() {
        return next;
    }

    /**
     * Devuelve la casilla anterior.
     *
     * @return Casilla anterior.
     */
    @Override
    public SquareCategory getPrevious() {
        return previous;
    }

    /**
     * Movimiento del jugador desde esta casilla, considerando dirección y cantidad de pasos.
     *
     * @param move    Cantidad de pasos a mover.
     * @param exit    Dirección del movimiento (0: atrás, 1: adelante).
     * @param jugador Ficha del jugador.
     * @return Casilla destino tras el movimiento.
     */
    public Square movimiento(int move, int exit, Ficha jugador) {
        this.cantidadFichas--;
        Square iter = this;
        for (int i = 0; i < move; i++) {
            if (exit == 1 && iter instanceof movimientoBidireccional next)
                iter = next.getNext();
            else if (exit == 0 && iter instanceof movimientoBidireccional prev)
                iter = prev.getPrevious();
        }
        return iter;
    }

    /**
     * Devuelve la siguiente casilla como avance directo (sin lógica adicional).
     *
     * @return Casilla siguiente.
     */
    public SquareCategory advance() {
        return next;
    }

    /**
     * Dibuja visualmente la casilla.
     *
     * @return Representación visual de la casilla.
     */
    @Override
    public String paint() {
        if (cantidadFichas > 0) {
            return "┌────┐\n" +
                    "│S " + cantidadFichas + " │\n" +
                    "└────┘";
        }
        return "┌────┐\n" +
                "│ S  │\n" +
                "└────┘";
    }

    /**
     * Solicita al jugador que seleccione una dirección de movimiento.
     *
     * @param jugador Ficha del jugador.
     * @return Dirección elegida (0: atrás, 1: adelante).
     */
    @Override
    public int action(Ficha jugador) {
        List<String> choices = new ArrayList<>();
        choices.add("0. Atrás");
        choices.add("1. Adelante");

        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(1), choices);
        dialog.setTitle("Selección de Ruta");
        dialog.setHeaderText("Tienes 2 posibles rutas, ¿a dónde te quieres mover?");
        dialog.setContentText("Elige tu ruta:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String selected = result.get();
            return Integer.parseInt(selected.substring(0, 1));
        }
        return 1; // Default or cancel option
    }

    /**
     * Reacción del jugador al caer en esta casilla:
     * lanza un dado y se mueve según la dirección seleccionada.
     *
     * @param jugador Ficha del jugador.
     * @return Nueva casilla luego del movimiento.
     */
    @Override
    public Square reaction(Ficha jugador) {
        int dado = (int) (Math.random() * 6) + 1; // Número aleatorio entre 1 y 6
        System.out.println("Tirando el dado... " + dado);
        int direction = action(jugador);
        return movimiento(dado, direction, jugador);
    }
}
