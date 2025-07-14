package est.ucab.jacafxproyecto.models;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gson.Gson;

import est.ucab.jacafxproyecto.models.DBController;

public class LoadUsuario {
        public List<Usuario> usuarios;

        public void loadUsuarioJson() {
            this.usuarios = DBController.loadUsuariosJson();
        }


    public void topTier(){
        loadUsuarioJson();
        usuarios.sort(Comparator.comparingInt(Usuario::getVictory).reversed());
    }
}
