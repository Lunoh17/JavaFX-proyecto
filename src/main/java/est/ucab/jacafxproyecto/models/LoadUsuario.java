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
    private final String path = "src/main/resources/est/ucab/jacafxproyecto/users.json";
    private ArrayList<Usuario> usuarios;
    private final Gson gson = new Gson();

    public void loadUsuarioJson() {
        String destinyFolder = System.getProperty("user.dir") + File.separator + ".config";
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
                this.usuarios = new ArrayList<Usuario>();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (FileReader r = new FileReader(destinyFolderFile + File.separator + "users.json")) {
                BufferedReader bufferedReader = new BufferedReader(r);
                Type listType = new TypeToken<ArrayList<Usuario>>() {
                }.getType();
                this.usuarios = gson.fromJson(bufferedReader, listType);
                if (this.usuarios == null) {
                    this.usuarios = new ArrayList<>();
                }
            } catch (IOException e) {
                throw new RuntimeException("Error al leer el archivo JSON", e);
            }
        }

    }

    public ArrayList<Usuario> topTier(){
        loadUsuarioJson();
        ArrayList<Usuario> tops= new ArrayList<>();
        Usuario jugador=new Usuario("","");
        for(int i=0;i<usuarios.size();i++){
            for(int j=0;j<usuarios.size();j++){
                if(usuarios.get(i).getVictory()<usuarios.get(j).getVictory()){
                    jugador=usuarios.get(j);
                }
            }
            tops.add(jugador);
        }
        return tops;
    }

}
