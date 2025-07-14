package est.ucab.jacafxproyecto.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DBController {
    private static final String HOME_FOLDER = System.getProperty("user.dir");
    private static final String SRC_FOLDER = HOME_FOLDER + File.separator + "src";

    public static void saveFichaJson(List<Ficha> jugadores, int maxPlayers) throws IOException {
        String destinyFolder = SRC_FOLDER;
        File destinyFolderFile = new File(destinyFolder);
        if (!destinyFolderFile.exists()) {
            boolean created = destinyFolderFile.mkdir();
            if (!created) throw new IOException();
        }
        Gson gson = new Gson();
        Square[] listaSquare = new Square[maxPlayers];
        int contador = 0;
        for (Ficha fa : jugadores) {
            listaSquare[contador++] = fa.posicion;
            fa.posicion = null;
        }
        String json = gson.toJson(jugadores);
        File data = new File(destinyFolder + File.separator + "fichas.json");
        contador = 0;
        for (Ficha fa : jugadores) {
            fa.posicion = listaSquare[contador++];
        }
        try (FileWriter writer = new FileWriter(data)) {
            writer.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Ficha> loadFichaJson() {
        Gson gson = new Gson();
        String destinyFolder = SRC_FOLDER;
        File destinyFolderFile = new File(destinyFolder);
        if (!destinyFolderFile.exists()) {
            boolean created = destinyFolderFile.mkdir();
            if (!created) {
                throw new RuntimeException();
            }
        }
        File a = new File(destinyFolderFile + File.separator + "fichas.json");
        if (!(a.exists())) {
            try {
                boolean created = a.createNewFile();
                if (!created) throw new IOException();
                return new ArrayList<>();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (FileReader r = new FileReader(destinyFolderFile + File.separator + "fichas.json")) {
                BufferedReader bufferedReader = new BufferedReader(r);
                Type listType = new TypeToken<ArrayList<Ficha>>() {}.getType();
                return gson.fromJson(bufferedReader, listType);
            } catch (IOException e) {
                throw new RuntimeException("Error al leer el archivo JSON", e);
            }
        }
    }

    public static ArrayList<Usuario> loadUsuariosJson() {
        Gson gson = new Gson();
        String destinyFolderFile = SRC_FOLDER;
        File a = new File(destinyFolderFile + File.separator + "users.json");
        if (!(a.exists())) {
            try {
                boolean created = a.createNewFile();
                if (!created) throw new IOException();
                return new ArrayList<>();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (FileReader r = new FileReader(destinyFolderFile + File.separator + "users.json")) {
                BufferedReader bufferedReader = new BufferedReader(r);
                Type listType = new TypeToken<ArrayList<Usuario>>() {}.getType();
                return gson.fromJson(bufferedReader, listType);
            } catch (IOException e) {
                throw new RuntimeException("Error al leer el archivo JSON", e);
            }
        }
    }

    // Add more static methods as needed for other JSON operations
}

