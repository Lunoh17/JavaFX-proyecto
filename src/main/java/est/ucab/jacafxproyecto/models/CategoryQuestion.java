package est.ucab.jacafxproyecto.models;

import est.ucab.jacafxproyecto.controllers.FichaController;

/**
 * La interfaz {@code CategoryQuestion} define el comportamiento esperado para las casillas
 * de categoría que contienen preguntas dentro del tablero del juego.
 * Implementaciones de esta interfaz deben proporcionar la lógica para reaccionar a una
 * pregunta y verificar si la respuesta del jugador es correcta.
 */
public interface CategoryQuestion {

    /**
     * Ejecuta la lógica de reacción cuando un jugador cae en una casilla de categoría.
     *
     * @param jugador   La ficha del jugador actual.
     * @param questions El conjunto de preguntas disponibles para la categoría.
     * @param fichaController El controlador de la ficha actual.
     * @return true si la respuesta es correcta, false en caso contrario.
     */
    boolean reaction(Ficha jugador, Questions questions);


    /**
     * Verifica si la respuesta del jugador a una pregunta es correcta.
     *
     * @param question La pregunta que se le presenta al jugador.
     * @param fichaController El controlador de la ficha actual.
     * @return {@code true} si la respuesta es correcta, de lo contrario {@code false}.
     */
    boolean revisarRespuesta(Question question);
}
