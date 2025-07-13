package est.ucab.jacafxproyecto.controllers;

import est.ucab.jacafxproyecto.models.Usuario;

public class selectedPlayers {
    private Boolean dentro=false;
    public Usuario usuario;

    public selectedPlayers(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setDentro() {
        if(dentro==false) {
            this.dentro = true;
        }
        else{
            this.dentro=false;
        }
    }



}
