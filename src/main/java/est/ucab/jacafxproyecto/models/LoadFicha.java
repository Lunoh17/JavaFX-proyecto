package est.ucab.jacafxproyecto.models;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class LoadFicha {
    public ArrayList<Ficha> jugadores= new ArrayList<>();

    public void loadFichaJson() {
        Gson gson = new Gson();
        String destinyFolder = homeFolder + File.separator + ".config";
        File destinyFolderFile = new File(destinyFolder);
        if (!destinyFolderFile.exists()) {
            boolean created = destinyFolderFile.mkdir();
            if (!created) {
                throw new RuntimeException();
            }
        }
        var a = new File(destinyFolderFile + File.separator + "fichas.json");
        if (!(a.exists())) {
            try {
                boolean created = a.createNewFile();
                if (!created)
                    throw new IOException();
                this.jugadores = new ArrayList<Ficha>();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (FileReader r = new FileReader(destinyFolderFile + File.separator + "fichas.json")) {
                BufferedReader bufferedReader = new BufferedReader(r);
                Type listType = new TypeToken<ArrayList<Ficha>>() {
                }.getType();
                this.jugadores = gson.fromJson(bufferedReader, listType);
            } catch (IOException e) {
                throw new RuntimeException("Error al leer el archivo JSON", e);
            }
        }

    }

    public void cargarPositions() {
        loadFichaJson();
        SquareCenter centro = new SquareCenter(jugadores.size());
        for(int i=0; i<jugadores.size(); i++){
            for(Square squareActual: centro.rayos){
                for (int j = 0; j <13 ; j++) {
                    if (squareActual instanceof  SquareRayo sr){
                        if (jugadores.get(i).getPosition()==sr.position) {
                            jugadores.get(i).posicion = sr;
                            jugadores.get(i).positionTable = sr.position;
                            jugadores.get(i).posicion.cantidadFichas++;
                            break;
                        }
                        squareActual=(Square) sr.next;
                    }else if (squareActual instanceof SquareCategory sC)
                    {
                        if (jugadores.get(i).getPosition()==sC.position) {
                            jugadores.get(i).posicion = sC;
                            jugadores.get(i).positionTable = sC.position;
                            jugadores.get(i).posicion.cantidadFichas++;
                            break;
                        }
                        squareActual=((Square) sC.next);
                    } else if (squareActual instanceof SquareSpecial sS) {
                        if (jugadores.get(i).getPosition()==sS.position) {
                            jugadores.get(i).posicion = sS;
                            jugadores.get(i).positionTable = sS.position;
                            jugadores.get(i).posicion.cantidadFichas++;
                            break;
                        }
                        squareActual=((Square) sS.next);
                    }

                }
            }
        }
    }

}
