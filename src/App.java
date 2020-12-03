import java.util.ArrayList;
import java.util.Scanner;

public class App {

    static listaPeliculas lista;
    static calificaciones ratings;

    public static void main(String[] args) throws Exception {

        lista = new listaPeliculas();
        ratings = new calificaciones(lista.maxIndex());

        //Enseñarle al usuario las peliculas y decirle que vote
        ArrayList<String[]> listaAleatoria = lista.generarPeliculas();

        System.out.println("A continuación, puntúe las siguientes películas [1.0 - 5.0]");
        Scanner sc = new Scanner(System.in);



        for (int i = 0; i < listaAleatoria.size(); i++) {
            //Mostrar la película
            System.out.println(listaAleatoria.get(i)[1]);
            boolean correcto = false;
            double voto = Double.parseDouble(sc.nextLine());
            while(!correcto){
                if(voto >= 1.0 && voto <= 5.0){
                    correcto = true;
                }
                else{
                    System.out.println("Valor incorrecto introducido. Pruebe otra vez [1.0 - 5.0]");
                    voto = Double.parseDouble(sc.nextLine());
                }
            }

            //Introducimos el voto en la matriz de calificaciones
            int usuario = ratings.numUsuarios;
            int pelicula = Integer.parseInt(listaAleatoria.get(i)[0]);

            ratings.calificaciones[usuario][pelicula] = voto;
        }



    }

}