package est.ucab.jacafxproyecto.models;

import java.util.ArrayList;
import est.ucab.jacafxproyecto.controllers.FichaController;
import est.ucab.jacafxproyecto.models.SquareCategory;
import est.ucab.jacafxproyecto.models.SquareRayo;


/**
 * Representa una ficha de un jugador dentro del juego.
 * Administra el estado del jugador, su posición en el tablero,
 * los triángulos obtenidos y el avance en el juego.
 */
public class Ficha {

    /**
     * Apodo del jugador asociado a esta ficha.
     */
    String nickName;

    /**
     * Usuario al que pertenece esta ficha.
     */
    Usuario usuario;

    /**
     * Array que representa los triángulos (categorías ganadas).
     */
    public int[] triangulos = new int[Category.values().length];


    /**
     * Casilla actual en la que se encuentra esta ficha.
     */
    public Square posicion;

    /**
     * Indica si el jugador ya salió del centro.
     */
    boolean salido;

    /**
     * Indica si el jugador ya ha entrado al centro.
     */
    boolean entrado = false;

    /**
     * Indica si el jugador ha ganado.
     */
    boolean gano = false;

    /**
     * Posición numérica de la ficha en el tablero.
     */
    public int positionTable;

    /**
     * Constructor de la ficha.
     *
     * @param nickName Apodo del jugador.
     * @param usuario  Usuario propietario.
     * @param posicion Posición inicial de la ficha.
     */
    public Ficha(String nickName, Usuario usuario, Square posicion) {
        this.nickName = nickName;
        this.usuario = usuario;
        this.posicion = posicion;
        this.salido = false;
        this.positionTable = 0;
    }

    public String getNickName() {
        return nickName;
    }

    /**
     * Retorna el usuario asociado a esta ficha.
     *
     * @return El usuario.
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Simula la tirada de un dado (valores entre 1 y 6).
     *
     * @return Número aleatorio del dado.
     */
    private int tirarDado() {
        int dado = (int) (Math.random() * 6) + 1;
        System.out.println("Tirando el dado..." + dado);
        return dado;
    }

    /**
     * Lógica para avanzar en el tablero dependiendo del estado de la ficha.
     *
     * @param questions Banco de preguntas del juego.
     * @param controller
     * @return 0 si no se pudo avanzar, 1 si se avanzó y no se ganó, 2 si se ganó.
     */
    public int avanzar(Questions questions, FichaController controller) {
        // Roll and move one time
        int dado = tirarDado();
        if (!salido && posicion instanceof brazo saliendo) {
            posicion = (posicion instanceof SquareCenter)
                ? saliendo.salir(dado, this.posicion.action(this), this)
                : saliendo.salir(dado, 1, this);
        } else if (entrado && posicion instanceof brazo saliendo) {
            posicion = saliendo.entrar(dado, 0, this);
        } else if (posicion instanceof movimientoBidireccional casilla) {
            posicion = casilla.movimiento(dado, this.posicion.action(this), this);
        }
        // Update count and position table
        posicion.cantidadFichas++;
        this.positionTable = posicion.position;
        // Handle question reaction if applicable
        if (posicion instanceof CategoryQuestion cQ) {
            boolean correct = cQ.reaction(this, questions);
            if (correct) {
                int sector = -1;
                if (posicion instanceof SquareCategory sc) sector = sc.categoria.ordinal() + 1;
                else if (posicion instanceof SquareRayo sr) sector = sr.getCategoria().ordinal() + 1;
                if (controller != null) controller.resaltarSector(sector);
                return this.gano ? 2 : 1;
            }
        }else {
            return 1; // Avanzó sin ganar porque seguro es un repetir
        }
        return 0;
    }

    /**
     * Obtiene las preguntas aprobadas de una categoría específica.
     *
     * @param questions Banco de preguntas.
     * @param category  Categoría deseada.
     * @return Lista de preguntas filtradas por categoría.
     */
    public ArrayList<Question> getQuestions(Questions questions, Category category) {
        ArrayList<Question> aprobadas = questions.getApproved();
        ArrayList<Question> filtradas = new ArrayList<>();
        for (Question q : aprobadas) {
            if (q.getCategory().equals(category)) {
                filtradas.add(q);
            }
        }
        return filtradas;
    }

    /**
     * Verifica si el jugador ha completado todos los triángulos.
     *
     * @return {@code true} si el jugador tiene todos los triángulos, {@code false} si no.
     */
    public boolean triangulo() {
        for (var actual : triangulos) {
            if (actual == 0) return false;
        }
        return true;
    }


    /**
     * Marca un triángulo como completado para una categoría específica.
     *
     * @param categoria Categoría ganada.
     */
    public void incrementarPuntos(Category categoria) {
        int n = categoria.ordinal();
        triangulos[n]++;
        this.entrado = this.triangulo();
    }

    public int getPosition() {
        return positionTable;
    }

}
