package est.ucab.jacafxproyecto.models;

/**
 * Clase abstracta que representa una casilla en el tablero del juego.
 * Define el comportamiento común que deben implementar las casillas específicas.
 */
abstract public class Square {

    /**
     * Cantidad de fichas que actualmente se encuentran en esta casilla.
     */
    public int cantidadFichas;

    /**
     * Posición de la casilla en el tablero.
     */
    protected int position;

    /**
     * Devuelve una representación visual de la casilla.
     *
     * @return una cadena que representa gráficamente la casilla.
     */
    abstract public String paint();

    /**
     * Ejecuta la acción que debe realizarse al caer una ficha en esta casilla.
     *
     * @param jugador  la ficha que realiza la acción.
     * @return un valor numérico que puede representar una dirección o estado.
     */
    abstract public int action(Ficha jugador);

    /**
     * Ejecuta la reacción de la casilla ante la llegada de una ficha.
     *
     * @param jugador  la ficha que llegó a esta casilla.
     * @return la nueva casilla a la que se moverá la ficha (puede ser la misma).
     */
    abstract public Square reaction(Ficha jugador);

    public int getCantidadFichas() {
        return cantidadFichas;
    }
    public void sumarCantidadFichas() {
        this.cantidadFichas++;
    }

    public int getPosition() {
        return position;
    }
}
