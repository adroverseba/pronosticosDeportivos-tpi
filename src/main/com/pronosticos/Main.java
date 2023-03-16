/*
    Clase principal del programa
 */
package main.com.pronosticos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        //verificar que se pasen los parametros necesarios
        if (args.length < 2) {
            System.out.println("Debe espicificar el nombre de los archivos de pronostico y resultado.");
            return;
        }

        //obtengo las rutas de los archivos
        String rutaPartidos = args[0];
        String rutaPronosticos = args[1];

        //leer los archivos y crear las instancias necesarias
        List<Partido> partidos = leerPartidos(rutaPartidos);
        List<Pronostico> pronosticos = leerPronosticos(rutaPronosticos, partidos);

        //calcular el puntaje de cada pronostico
        Map<Equipo, ResultadoEnum> resultados = new HashMap<>();
        for (Partido partido : partidos) {
            resultados.put(partido.getEquipo1(), partido.resultado(partido.getEquipo1()));
            resultados.put(partido.getEquipo2(), partido.resultado(partido.getEquipo2()));
        }

        int puntajeTotal = 0;
        for (Pronostico pronostico : pronosticos) {
            Partido partido = pronostico.getPartido();
            Equipo equipoPronostico = pronostico.getEquipo();
            ResultadoEnum resultadoReal = resultados.get(pronostico.getEquipo());
            if (resultadoReal == pronostico.getResultado()) {
                int puntos = pronostico.puntos();
                puntajeTotal += puntos;
                System.out.println("Pronostico acertado");
            } else {
                System.out.println("Pronostico fallido");
            }
        }
        System.out.println("Puntaje Total: "+puntajeTotal);
        
        
    }

    /**
     * Lee los partidos desde un archivo y crea las instancias correspondientes.
     *
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

    private static List<Pronostico> leerPronosticos(String rutaArchivo, List<Partido> partidos) {
        List<Pronostico> pronosticos = new ArrayList<>();
        try {
            BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo));
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(",");
                Equipo equipo = new Equipo(datos[0], "");
                ResultadoEnum resultadoEnum = ResultadoEnum.valueOf(datos[1]);
                Partido partido = buscarPartido(partidos, datos[2], datos[3]);
                if (partido != null) {
                    Pronostico pronostico = new Pronostico(partido, equipo, resultadoEnum);
                    pronosticos.add(pronostico);
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }
        return pronosticos;
    }

    /**
     * Busca un partido en la lista de partidos dados los nombres de los
     * equipos.
     *
     * @param partidos la lista de partidos a buscar
     * @param equipo1 el nombre del primer equipo
     * @param equipo2 el nombre del segundo equipo
     * @return el partido correspondiente si lo encuentra, o null si no lo
     * encuentra
     */
    private static Partido buscarPartido(List<Partido> partidos, String equipo1, String equipo2) {
        for (Partido partido : partidos) {
            if (partido.getEquipo1().getNombre().equals(equipo1) && partido.getEquipo1().getNombre().equals(equipo2)) {
                return partido;
            }
        }
        return null;
    }
}
