package est.ucab.jacafxproyecto.models;
import java.io.File;
import java.util.ArrayList;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;

public class LoadUsuario {
        public List<Usuario> usuarios;

        public void loadUsuarioJson() {
            Gson gson = new Gson();
            String rutaRelativa = "src";
            File carpetaConfig = new File(rutaRelativa);

            if (!carpetaConfig.exists()) {
                boolean creada = carpetaConfig.mkdir();
                if (!creada) throw new RuntimeException("No se pudo crear la carpeta .config");
            }

            File archivoJson = new File(carpetaConfig, "users.json");

            if (!archivoJson.exists()) {
                try {
                    boolean creado = archivoJson.createNewFile();
                    if (!creado) throw new IOException("No se pudo crear el archivo users.json");
                    this.usuarios = new ArrayList<>();  // se inicializa lista vacía
                } catch (IOException e) {
                    throw new RuntimeException("Error al crear el archivo JSON", e);
                }
            } else {
                try (BufferedReader reader = new BufferedReader(new FileReader(archivoJson))) {
                    Type tipoLista = new TypeToken<List<Usuario>>() {}.getType();
                    System.out.println("se lleno la lita");
                    this.usuarios = gson.fromJson(reader, tipoLista);  // se llena la lista desde JSON
                } catch (IOException e) {
                    throw new RuntimeException("Error al leer el archivo JSON", e);
                }
            }
        }


    public void topTier(){
        loadUsuarioJson();
        usuarios.sort(Comparator.comparingInt(Usuario::getVictory).reversed());
    }

}
