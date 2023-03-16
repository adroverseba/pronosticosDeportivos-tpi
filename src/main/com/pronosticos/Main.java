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
//        if (args.length < 2) {
//            System.out.println("Debe espicificar el nombre de los archivos de pronostico y resultado.");
//            return;
//        }

        //obtengo las rutas de los archivos
//        String rutaPartidos = args[0];
//        String rutaPronosticos = args[1];
        String rutaPartidos = "src/data/resultado.txt";
        String rutaPronosticos = "src/data/pronostico.txt";

        List<Persona> personas = new ArrayList<>();
        Liga liga = new Liga();
        //leer los archivos y crear las instancias necesarias
        List<Partido> partidos = leerPartidos(rutaPartidos);
        List<Pronostico> pronosticos = leerPronosticos(rutaPronosticos, partidos);

        //calcular el puntaje de cada pronostico
        Map<String, ResultadoEnum> resultados = new HashMap<>();
        for (Partido partido : partidos) {
            resultados.put(partido.getEquipo1().getNombre(), partido.resultado(partido.getEquipo1()));
            resultados.put(partido.getEquipo2().getNombre(), partido.resultado(partido.getEquipo2()));
        }

        for (Map.Entry<String, ResultadoEnum> entry : resultados.entrySet()) {
            System.out.println("Equipo: " + entry.getKey() + ", Resultado: " + entry.getValue());
        }

        int puntajeTotal = 0;
        for (Pronostico pronostico : pronosticos) {
            //almaceno las personas participantes en liga
            if (!liga.getPersonas().contains((pronostico.getPersona()))) {
                liga.agregarPersona(pronostico.getPersona());
            }

            //obtengo el equipo de pronostico
            Equipo equipoPronostico = pronostico.getEquipo();
            // obtengo el resultado real de los partidos 
            ResultadoEnum resultadoReal = resultados.get(equipoPronostico.getNombre());
            System.out.println("\nPrediccion realizada por " + pronostico.getPersona().getNombre() + ", " + equipoPronostico.getNombre() + " " + pronostico.getResultado());

            //verifico si el resultado real es igual al pronosticado, si es asi sumo un punto a la persona que acerto
            if (resultadoReal == pronostico.getResultado()) {
                int puntos = pronostico.puntos();
                pronostico.getPersona().sumarPuntaje(puntos);
            }
        }

        //imprimo los puntajes por persona 
        System.out.println("\n\tResultados Finales: ");
        liga.imprimirPuntajes();
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
                Equipo equipo2 = new Equipo(datos[2], "");
                int golesEquipo1 = Integer.parseInt(datos[1]);
                int golesEquipo2 = Integer.parseInt(datos[3]);
                Partido partido = new Partido(equipo1, equipo2, golesEquipo1, golesEquipo2);
                partidos.add(partido);
            }

        } catch (IOException e) {
            System.err.println("Error: " + e);
        }
        return partidos;
    }

    //lee el pronostico realizado por personas
    private static List<Pronostico> leerPronosticos(String rutaArchivo, List<Partido> partidos) {
        List<Pronostico> pronosticos = new ArrayList<>();

        try {
            BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo));
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(",");
                Persona persona = new Persona(datos[0]);

                Equipo equipo = new Equipo(datos[1], "");
                ResultadoEnum resultadoEnum = ResultadoEnum.valueOf(datos[2]);
                Partido partido = buscarPartido(partidos, datos[3], datos[4]);
                if (partido != null) {
                    Pronostico pronostico = new Pronostico(partido, equipo, resultadoEnum, persona);
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
            if (partido.getEquipo1().getNombre().equals(equipo1) && partido.getEquipo2().getNombre().equals(equipo2)) {
                return partido;
            }
        }
        return null;
    }
}
