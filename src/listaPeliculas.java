import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class listaPeliculas {

    String archivo;
    ArrayList<String[]> lista;
    int maxIndex = 0;
    Random r;

    listaPeliculas() throws CsvValidationException, IOException {

        lista = new ArrayList<>();
        r = new Random();
        
        // Leer las peliculas
        archivo = "./files/movies.csv";
        try (CSVReader csvReader = new CSVReader(new FileReader(archivo))) {
            String[] fila = null;
            boolean a = true;
            while ((fila = csvReader.readNext()) != null) {
                if (a) {
                    a = false;
                } else {
                    if(Integer.parseInt(fila[0]) > maxIndex){
                        maxIndex = Integer.parseInt(fila[0]);
                    }
                    lista.add(fila);
                }
            }
        }

    }

    String[] get(int a) {
        return lista.get(a);
    }

    int size() {
        return lista.size();
    }

    int maxIndex(){
        return maxIndex;
    }

    ArrayList<String[]> generarPeliculas() {
        ArrayList<String[]> aleatorias = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            int rand = r.nextInt((lista.size()) + 1) + 0;
            aleatorias.add(lista.get(rand));
        }

        return aleatorias;
    }
}
