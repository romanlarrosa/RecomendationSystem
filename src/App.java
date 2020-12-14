import java.util.ArrayList;
import java.util.Scanner;

public class App {

    static double mediaUsuario = 0;

    static listaPeliculas lista;
    static calificaciones ratings;
    static ArrayList<String[]> listaAleatoria;
    static ArrayList<Integer> vecinos = new ArrayList<>();
    static ArrayList<Double> pearson = new ArrayList<>();

    static ArrayList<Integer> vecinosCercanos = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        lista = new listaPeliculas();
        ratings = new calificaciones(lista.maxIndex());

        // El usuario realiza la puntuación
        puntuar();

        // Se calculan los vecinos mas cercanos
        for (int i = 0; i < ratings.numUsuarios; i++) {
            vecinos.add(i);
            pearson.add(CoeficientePearson(i, mediaUsuario));
        }

        // Ordenar los vecinos de mayor a menor
        ordenar();
        // Nos quedamos con los 5 mejores vecinos
        for (int i = 0; i < 5; i++) {
            vecinosCercanos.add(vecinos.get(i));
        }

        ArrayList<Integer> pelisRecomendadas = calcularR();

        // Mostrar las recomendaciones
        if (pelisRecomendadas.size() > 0) {
            System.out.println();
            System.out.println("Éstas son las peliculas que nuestro sistema le recomienda:");
            for (int i = 0; i < pelisRecomendadas.size(); i++) {
                for (int j = 0; j < lista.lista.size(); j++) {
                    if (pelisRecomendadas.get(i) == Integer.parseInt(lista.lista.get(j)[0])) {
                        System.out.println(lista.lista.get(j)[1]);
                    }
                }
            }
        }
        else {
            System.out.println("Lo sentimos, no tenemos ninguna película que recomendarle");
        }

        System.out.println("¿Gracias por usar nuestro servicio!");

    }

    private static ArrayList<Integer> calcularR() {
        ArrayList<Integer> ret = new ArrayList<>();

        for (int i = 0; i < ratings.numPelis; i++) {
            if (ratings.calificaciones[ratings.numUsuarios][i] != -1) {
                double arriba = 0;
                double abajo = 0;

                for (int j = 0; j < vecinosCercanos.size(); j++) {
                    if (ratings.calificaciones[vecinosCercanos.get(j)][i] != -1) {
                        double cof_pearson = pearson.get(vecinosCercanos.get(j));
                        double valoracion = ratings.calificaciones[vecinosCercanos.get(j)][i];
                        arriba += (cof_pearson * valoracion);
                        abajo += cof_pearson;
                    }
                }

                if (abajo != 0) {
                    double r = arriba / abajo;
                    if (r >= 4.0) {
                        ret.add(i);
                    }
                }

            }
        }

        return ret;
    }

    static void ordenar() {
        boolean flag = false;
        while (flag) {
            flag = false;
            for (int i = 0; i < pearson.size() - 1; i++) {
                if (pearson.get(i) < pearson.get(i + 1)) {
                    double aux = pearson.get(i);
                    int auxV = vecinos.get(i);

                    pearson.set(i, pearson.get(i + 1));
                    vecinos.set(i, vecinos.get(i + 1));

                    pearson.set(i + 1, aux);
                    vecinos.set(i + 1, auxV);

                    flag = true;
                }
            }
        }
    }

    static void puntuar() {
        // Enseñarle al usuario las peliculas y decirle que vote
        listaAleatoria = lista.generarPeliculas();

        System.out.println("A continuación, puntúe las siguientes películas [1.0 - 5.0]");
        Scanner sc = new Scanner(System.in);

        for (int i = 0; i < listaAleatoria.size(); i++) {
            // Mostrar la película
            System.out.println(listaAleatoria.get(i)[1]);
            boolean correcto = false;
            String aux = sc.nextLine();
            double voto;
            if (aux.isEmpty()) {
                voto = -1;
            } else {
                voto = Double.parseDouble(aux);
            }
            while (!correcto) {
                if (voto >= 1.0 && voto <= 5.0) {
                    correcto = true;
                } else {
                    System.out.println("Valor incorrecto introducido. Pruebe otra vez [1.0 - 5.0]");
                    aux = sc.nextLine();
                    if (aux.isEmpty()) {
                        // Cadena vacia
                        voto = -1;
                    } else {
                        voto = Double.parseDouble(aux);
                    }

                }
            }

            // Introducimos el voto en la matriz de calificaciones
            int usuario = ratings.numUsuarios;
            int pelicula = Integer.parseInt(listaAleatoria.get(i)[0]);

            ratings.calificaciones[usuario][pelicula] = voto;
            mediaUsuario += voto;

        }
        mediaUsuario = mediaUsuario / 20;
    }

    static double CoeficientePearson(int idVecino, double media) {
        double coeficiente;
        double mediaVecino = 0;
        int n = 0; // Cuantas peliculas ha calificado el vecino
        double arriba, raizI, raizD;
        arriba = raizD = raizI = 0.0;

        // Media del vecino
        for (int i = 0; i < ratings.numPelis; i++) {
            if (ratings.calificaciones[idVecino][i] != -1) {
                n++;
                mediaVecino += ratings.calificaciones[idVecino][i];
            }
        }
        if (n != 0) {
            mediaVecino = mediaVecino / n;
        } else {
            mediaVecino = Double.NEGATIVE_INFINITY;
        }

        for (int i = 0; i < listaAleatoria.size(); i++) {
            // Contar solo si el usuario idVecino ha votado la pelicula
            int idPelicula = Integer.parseInt(listaAleatoria.get(i)[0]);
            if (ratings.calificaciones[idVecino][idPelicula] != -1) {
                double votoVecino = ratings.calificaciones[idVecino][idPelicula];
                double votoUsuario = ratings.calificaciones[ratings.numUsuarios][idPelicula];

                arriba += (votoUsuario - media) * (votoVecino - mediaVecino);
                raizI += Math.pow((votoUsuario - media), 2);
                raizD += Math.pow((votoVecino - mediaVecino), 2);
            }
        }

        if(raizI == 0 || raizD == 0){
            //Calcular sin ajustar
            arriba = raizD = raizI = 0.0;
            for (int i = 0; i < listaAleatoria.size(); i++) {
                // Contar solo si el usuario idVecino ha votado la pelicula
                int idPelicula = Integer.parseInt(listaAleatoria.get(i)[0]);
                if (ratings.calificaciones[idVecino][idPelicula] != -1) {
                    double votoVecino = ratings.calificaciones[idVecino][idPelicula];
                    double votoUsuario = ratings.calificaciones[ratings.numUsuarios][idPelicula];
    
                    arriba += (votoUsuario ) * (votoVecino );
                    raizI += Math.pow((votoUsuario ), 2);
                    raizD += Math.pow((votoVecino ), 2);
                }
            }

        }
        coeficiente = arriba / (Math.sqrt(raizI) * Math.sqrt(raizD));

        return coeficiente;
    }

}