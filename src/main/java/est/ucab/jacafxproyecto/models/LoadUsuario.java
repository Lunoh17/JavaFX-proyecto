package est.ucab.jacafxproyecto.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class LoadUsuario {
    public ArrayList<Usuario> jugadores= new ArrayList<>();

    public void loadUsuarioJson() {
        Gson gson = new Gson();
        String destinyFolder = "User.dir" + File.separator + ".config";
        File destinyFolderFile = new File(destinyFolder);
        if (!destinyFolderFile.exists()) {
            boolean created = destinyFolderFile.mkdir();
            if (!created) {
                throw new RuntimeException();
            }
        }
        var a = new File(destinyFolderFile + File.separator + "users.json");
        if (!(a.exists())) {
            try {
                boolean created = a.createNewFile();
                if (!created)
                    throw new IOException();
                this.jugadores = new ArrayList<Usuario>();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (FileReader r = new FileReader(destinyFolderFile + File.separator + "users.json")) {
                BufferedReader bufferedReader = new BufferedReader(r);
                Type listType = new TypeToken<ArrayList<Ficha>>() {
                }.getType();
                this.jugadores = gson.fromJson(bufferedReader, listType);
            } catch (IOException e) {
                throw new RuntimeException("Error al leer el archivo JSON", e);
            }
        }

    }

    public ArrayList<Usuario> topTier(){
        loadUsuarioJson();
        ArrayList<Usuario> tops= new ArrayList<>();
        Usuario jugador=new Usuario("","");
        for(int i=0;i<jugadores.size();i++){
            for(int j=0;j<jugadores.size();j++){
                if(jugadores.get(i).getVictory()<jugadores.get(j).getVictory()){
                    jugador=jugadores.get(j);
                }
            }
            tops.add(jugador);
        }
        return tops;
    }

}
