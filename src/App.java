import java.io.FileReader;

import com.opencsv.*;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Vamos a leer un archivo");
        String archCSV = "./files/movies.csv";
        try (CSVReader csvReader = new CSVReader(new FileReader(archCSV))) {
            String[] fila = null; // Lo estamos guardando aquí
            while ((fila = csvReader.readNext()) != null) {
                System.out.println(fila[0] + " | " + fila[1] + " |  " + fila[2]);
            }
        }
    }
}
