/*
    Clase principal del programa
 */
package main.com.pronosticos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        //verificar que se pasen los parametros necesarios
        if (args.length < 2) {
            System.out.println("Debe espicificar el nombre de los archivos de pronostico y resultado.");
            return;
        }

        //obtengo las rutas de los archivos
        String rutaPronostico = args[0];
        String rutaResultado = args[1];
        //leer los archivos y crear las instancias necesarias

        //calcular el puntaje de cada pronostico
    }

    /**
     * Lee las predicciones desde un archivo y crea las instancias
     * correspondientes.
     * @param rutaArchivo la ruta del archivo que contiene los partidos
     * @return una lista de instancias de Partido
     */
    private static List<Partido> leerPartidos(String rutaArchivo) {
        List<Partido> partidos = new ArrayList<>();
        try {
            BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo));
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(",");
                Equipo equipo1 = new Equipo(datos[0], "");
                Equipo equipo2 = new Equipo(datos[1], "");
                int golesEquipo1 = Integer.parseInt(datos[3]);
                int golesEquipo2 = Integer.parseInt(datos[4]);
                Partido partido = new Partido(equipo1, equipo2, golesEquipo1, golesEquipo2);
                partidos.add(partido);

            }

        } catch (IOException e) {
            System.err.println("Error: " + e);
        }
        return partidos;
    }
}
