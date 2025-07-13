package est.ucab.jacafxproyecto.models;


/**
 * Clase que representa un usuario del sistema de trivia.
 * Contiene el nombre de usuario y la contraseña (almacenada como hash SHA-256).
 */
public class Usuario {
    /**
     * Nombre de usuario del usuario.
     */
    public String userName;

    private String nickName;

    private int partidas=0;

    private int loses=0;

    private double totalTimeQuestions=0;


    private int victory=0;

    private int answeredGeografia=0;
    private int answeredHistoria=0;
    private int answeredDeporte=0;
    private int answeredCiencia=0;
    private int answeredArte=0;
    private int answeredEntretenimiento=0;


    /**
     * Constructor de la clase Usuario.
     *
     * @param userName userName El nombre de usuario.
     */
    public Usuario(String userName, String nickName) {
        this.userName = userName;
        this.nickName = nickName;
    }

    /**
     * Obtiene el nombre de usuario.
     *
     * @return El nombre de usuario.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Establece un nuevo nombre de usuario.
     *
     * @param userName El nuevo nombre de usuario.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getVictory() {
        return this.victory;
    }
    public void setVictory(int numero) {
        this.victory = numero;

    }

    public int getPartidas() {
        return partidas;
    }

    public void setPartidas() {
        this.partidas++;
    }

    public int getLoses() {
        return loses;
    }

    public void setLoses() {
        this.loses++;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


    public void setTotalTimeQuestions(double totalTimeQuestions) {
        this.totalTimeQuestions = this.totalTimeQuestions+ totalTimeQuestions;
    }

    public int getAnsweredDeporte() {
        return answeredDeporte;
    }
    public int getAnsweredGeografia(){return answeredGeografia;}
    public int getAnsweredHistoria(){return answeredHistoria;}
    public int getAnsweredCiencia(){return answeredCiencia;}
    public int getAnsweredArte(){return answeredArte;}
    public int getAnsweredEntretenimiento(){return answeredEntretenimiento;}


    public double getTotalTimeQuestions(){return totalTimeQuestions;}

    public void setCategoriesDeporte(){
        answeredDeporte++;
    }
    public void setCategoriesGeografia(){
        answeredGeografia++;
    }
    public void setCategoriesHistoria(){
        answeredHistoria++;
    }
    public void setCategoriesCiencia(){
        answeredCiencia++;
    }
    public void setCategoriesArte(){
        answeredArte++;
    }
    public void setCategoriesEntretenimiento(){
        answeredEntretenimiento++;
    }
}
