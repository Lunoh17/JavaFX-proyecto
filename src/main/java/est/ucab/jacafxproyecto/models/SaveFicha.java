package est.ucab.jacafxproyecto.models;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SaveFicha {


    /**
     * Guarda el estado actual de las fichas de los jugadores en un archivo JSON.
     *
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    public void saveFichaJson(ArrayList<Ficha> jugadores) throws IOException {
        String destinyFolder = "User.dir" + File.separator + ".config";
        File destinyFolderFile = new File(destinyFolder);
        if (!destinyFolderFile.exists()) {
            boolean created = destinyFolderFile.mkdir();
            if (!created)
                throw new IOException();
        }
        Gson gson = new Gson();

        Square listaSquare[]= new Square[99];//99 son la cantidad maxima de jugadores
        int contador = 0;
        for (Ficha fa:jugadores){
            listaSquare[contador++]=fa.posicion;
            fa.posicion=null;
        }
        String json = gson.toJson(jugadores);
        File data = new File(destinyFolder + File.separator + "fichas.json");
        contador=0;
        for (Ficha fa:jugadores){
            fa.posicion=listaSquare[contador++];
        }
        try (FileWriter writer = new FileWriter(data)) {
            writer.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
