import java.io.FileReader;
import java.io.IOException;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class calificaciones{

    double[][] calificaciones;
    int numPelis;
    int numUsuarios = 0;
    String archivo;

    calificaciones(int numPelis) throws CsvValidationException, NumberFormatException, IOException {
        this.numPelis = numPelis;
        // Leer cuantos usuarios hay
        archivo = "./files/ratings.csv";
        calcularUsuarios();

        calificaciones = new double[numUsuarios + 1][numPelis];
        for (int i = 0; i < numUsuarios+1; i++) {
            for (int j = 0; j < numPelis; j++) {
                calificaciones[i][j] = -1;
            }
        }

        // Rellenamos calificaciones
        try (CSVReader csvReader = new CSVReader(new FileReader(archivo))) {
            String[] fila = null;
            boolean a = true;
            while ((fila = csvReader.readNext()) != null) {
                if (a) {
                    a = false;
                } else {
                    // Rellenamos las calificaciones
                    int usuario = Integer.parseInt(fila[0]) - 1;
                    int pelicula = Integer.parseInt(fila[1]) - 1;
                    double voto = Double.parseDouble(fila[2]);

                    calificaciones[usuario][pelicula] = voto;
                }
            }
        }

    }

    void calcularUsuarios() throws CsvValidationException, NumberFormatException, IOException {
        try (CSVReader csvReader = new CSVReader(new FileReader(archivo))) {
            String[] fila = null;
            boolean a = true;
            while ((fila = csvReader.readNext()) != null) {
                if (a) {
                    a = false;
                } else {
                    if (Integer.parseInt(fila[0]) > numUsuarios) {
                        numUsuarios = Integer.parseInt(fila[0]);
                    }
                }
            }
        }
    }
}
