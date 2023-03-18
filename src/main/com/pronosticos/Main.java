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

//        List<Persona> personas = new ArrayList<>();
        Liga liga = new Liga();
        //leer los archivos y crear las instancias necesarias
        List<Ronda> rondas = leerPartidos(rutaPartidos);

        //verifico los puntajes por Rondas 
        for (Ronda ronda : rondas) {
            List<Partido> partidos = ronda.getPartidos();

            List<Pronostico> pronosticos = leerPronosticos(rutaPronosticos, partidos, liga);

            //calcular el puntaje de cada pronostico
            //primero mapeo los resultados reales de los partidos con clave igual al equipo y valor a su resultado
            Map<String, ResultadoEnum> resultados = new HashMap<>();
            for (Partido partido : partidos) {
                resultados.put(partido.getEquipo1().getNombre(), partido.resultado(partido.getEquipo1()));
                resultados.put(partido.getEquipo2().getNombre(), partido.resultado(partido.getEquipo2()));
            }

            System.out.println("\nResultados de los partidos: ");
            for (Map.Entry<String, ResultadoEnum> entry : resultados.entrySet()) {
                System.out.println("Equipo: " + entry.getKey() + ", Resultado: " + entry.getValue());
            }

            System.out.println("\nPronosticos de los participantes: ");
            for (Pronostico pronostico : pronosticos) {
                //almaceno las personas participantes en Liga si no existen en la misma
                if (!liga.buscarPersonaNombre(pronostico.getPersona().getNombre())) {
                    liga.agregarPersona(pronostico.getPersona());
                }

                //obtengo el equipo de pronostico
                Equipo equipoPronostico = pronostico.getEquipo();
                // obtengo el resultado real de los partidos usando los equipos pasados en el pronostico 
                ResultadoEnum resultadoReal = resultados.get(equipoPronostico.getNombre());
                //imprimo las predicciones realizadas por los participantes
                System.out.println("Prediccion realizada por " + pronostico.getPersona().getNombre() + ", " + equipoPronostico.getNombre() + " " + pronostico.getResultado());

                //verifico si el resultado real es igual al pronosticado, si es asi sumo un punto a la persona que acerto
                if (resultadoReal == pronostico.getResultado()) {
                    int puntos = pronostico.puntos();
                    pronostico.getPersona().sumarPuntaje(puntos);
                }
            }

            //imprimo los puntajes por persona 
            System.out.println("\n\tRonda " + ronda.getNro() + " terminada\n. ");
//        System.out.println("cantidad de participantes: " + liga.getPersonas().size());

        }
        System.out.println("\t*** RESULTADO FINAL ***");
        liga.imprimirPuntajes();

    }

    /**
     * Lee los partidos desde un archivo y crea las instancias correspondientes.
     *
     * @param rutaArchivo la ruta del archivo que contiene los partidos
     * @return una lista de instancias de Partido
     */
    private static List<Ronda> leerPartidos(String rutaArchivo) {
//        List<Partido> partidos = new ArrayList<>();
//        Partido[] partidos = new Partido[4];

        List<Ronda> rondas = new ArrayList<>();
        Ronda ronda;
        try {
            BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo));
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(",");

                String numeroRonda = datos[0];
                Equipo equipo1 = new Equipo(datos[1], "");
                Equipo equipo2 = new Equipo(datos[3], "");
                int golesEquipo1 = Integer.parseInt(datos[2]);
                int golesEquipo2 = Integer.parseInt(datos[4]);
                Partido partido = new Partido(equipo1, equipo2, golesEquipo1, golesEquipo2);

                //verifico si la ronda ya existe en la lista de rondas
                boolean existeRondaEnLista = false;
                Ronda rondaEncontrada = null;
                for (Ronda r : rondas) {
//                    System.out.println("numero de ronda: "+ r.getNro());
//                    System.out.println("numero de ronda: "+ numeroRonda);
//                    System.out.println(r.getNro().equals(numeroRonda));
                    if (r.getNro().equals(numeroRonda)) {
                        existeRondaEnLista = true;
                        rondaEncontrada = r;
//                        System.out.println(rondaEncontrada);
                        break;
                    }
                }
                if (existeRondaEnLista) {
                    if (rondaEncontrada != null) {
                        rondaEncontrada.agregarPartido(partido);
//                        System.out.println("aca estoy");
                    }
                } else {
                    ronda = new Ronda();
                    ronda.setNro(numeroRonda);
                    ronda.agregarPartido(partido);
                    rondas.add(ronda);
                }

            }
            lector.close();
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }

        return rondas;
    }

    //lee el pronostico realizado por personas
    private static List<Pronostico> leerPronosticos(String rutaArchivo, List<Partido> partidos, Liga liga) {
        List<Pronostico> pronosticos = new ArrayList<>();

        try {
            BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo));
            String linea;
            Persona persona;

            //creo una persona nueva, si ya existe solo la llamo de Liga
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(",");
                if (!liga.buscarPersonaNombre(datos[0])) {
                    persona = new Persona(datos[0]);
                    System.out.println("se crea participante: " + datos[0]);
                    liga.agregarPersona(persona);
                } else {
                    persona = liga.obtenerPersonaPorNombre(datos[0]);
                }

                Equipo equipo = new Equipo(datos[1], "");
                ResultadoEnum resultadoEnum = ResultadoEnum.valueOf(datos[2]);
                Partido partido = buscarPartido(partidos, datos[3], datos[4]);
                if (partido != null) {
                    Pronostico pronostico = new Pronostico(partido, equipo, resultadoEnum, persona);
                    pronosticos.add(pronostico);
                } else {
//                    System.out.println("Error: no se encontro un partido para la linea.");
                }
//                System.out.println(partido);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }

//        System.out.println("aca estoy 1");
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
