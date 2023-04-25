/*
    Clase principal del programa
 */
package main.com.pronosticos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Main {

    public static final String url = "jdbc:mysql://localhost:3306/pronostico_deportivo?autoReconnect=true&useSSL=false";
    public static final String user = "root";
    public static final String password = "root";
    static PreparedStatement ps;
    static ResultSet rs;

    public static Connection getConnection() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(url, user, password);
            JOptionPane.showMessageDialog(null, "conexion exitosa a DB");
        } catch (Exception e) {
            System.err.println("Error, " + e);
        }
        return conexion;
    }

    public static void main(String[] args) {

        //verificar que se pasen los parametros necesarios
        //obtengo las rutas de los archivos
        String rutaPartidos = "src/data/resultado.txt";
//        String rutaPronosticos = "src/data/pronostico.txt"; //esto pasa a ser leido por la db

        //creo una instancia de Liga donde se almacenaran los participantes del torneo
        Liga liga = new Liga();
        //leer los archivos y crear las instancias necesarias, primero leo el archivo con todos los resultados
        List<Ronda> rondas = leerPartidos(rutaPartidos);

        //por cada una de las rondas jugadas realizo lo siguiente 
        for (Ronda ronda : rondas) {
            //otengo la lista de todos lo partidos jugados
            List<Partido> partidos = ronda.getPartidos();
            List<Pronostico> pronosticos = leerPronosticos( partidos, liga);

            System.out.println("\nResultados de los partidos: ");

            for (Partido partido : partidos) {
                System.out.println(partido.getEquipo1().getNombre() + " " + partido.getGolesEquipo1() + " - " + partido.getGolesEquipo2() + " " + partido.getEquipo2().getNombre());
            }

            System.out.println("\nPronosticos de los participantes: ");
            int contadorDePartidos = 0;

            for (Pronostico pronostico : pronosticos) {
                if (contadorDePartidos == 4) {
                    contadorDePartidos = 0;
                }
                contadorDePartidos++;

                //almaceno las personas participantes en Liga si no existen en la misma
                if (!liga.buscarPersonaNombre(pronostico.getPersona().getNombre())) {
                    liga.agregarPersona(pronostico.getPersona());
                }

                //obtengo el equipo del pronostico
                Equipo equipoPronostico = pronostico.getEquipo();
                //obtengo el resultado del equipo en el pronostico
                ResultadoEnum resultadoPronostico = pronostico.getResultado();

                //obtengo el objeto Partido del Pronostico
                Partido partidoPronostico = pronostico.getPartido();
                // con el objeto Partido verifico el resultado del equipo1 y lo comparo con el resultado colocado en el pronostico

                if (resultadoPronostico == partidoPronostico.resultado(partidoPronostico.getEquipo1())) {
                    //si se cumple condicion entonces sumo puntos a la persona que hizo el pronostico
                    int puntos = pronostico.puntos();

                    pronostico.getPersona().sumarPuntaje(puntos);
                }
                System.out.println("Prediccion realizada por " + pronostico.getPersona().getNombre() + ", " + equipoPronostico.getNombre() + " " + pronostico.getResultado());

//                System.out.println("Contador de partidos " + contadorDePartidos);
                if (pronostico.getPersona().getPuntaje() == 4 && contadorDePartidos == 4) {
//                    System.out.println("aca estoy");
                    System.out.println("\nLa persona " + pronostico.getPersona().getNombre() + " ha acertado todos los resultados de la Fase" + ronda.getNro() + ". Gana 3 puntos extras\n");
                    pronostico.getPersona().sumarPuntaje(3);
                }

            }

            //imprimo el numero de  Ronda
            System.out.println("\n\tRonda " + ronda.getNro() + " terminada\n. ");
        }
        System.out.println("\t*** RESULTADO FINAL ***");
        liga.imprimirPuntajes(); //imprimo el puntaje total por participante

    }

    /**
     * Lee los partidos desde un archivo y crea las instancias correspondientes.
     * resultado.txt
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
            BufferedReader lector = new BufferedReader(new FileReader(rutaArchivo));//lector de archivo
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
                for (Ronda r : rondas) { //bucle for each
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
            lector.close();//cierre lector de archivo
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }

        return rondas;
    }

    //lee el pronostico realizado por personas - conexion base de datos
    private static List<Pronostico> leerPronosticos( List<Partido> partidos, Liga liga) {
        List<Pronostico> pronosticos = new ArrayList<>();
        Connection conexion = null;

        try {
            conexion = getConnection();
            ps = conexion.prepareStatement("Select * from pronostico");
            rs = ps.executeQuery();
            Persona persona;

            //creo una persona nueva, si ya existe solo la llamo de Liga
            while (rs.next()) {
                if (!liga.buscarPersonaNombre(rs.getString("persona"))) {
                    persona = new Persona(rs.getString("persona"));
                    System.out.println("se crea participante: " + rs.getString("persona"));
                    liga.agregarPersona(persona);
                } else {
                    persona = liga.obtenerPersonaPorNombre(rs.getString("persona"));
                }
                Equipo equipo = new Equipo(rs.getString("equipo_seleccionado"), "");
                ResultadoEnum resultadoEnum = ResultadoEnum.valueOf(rs.getString("resultado_equipo_seleccionado"));
                Partido partido = buscarPartido(partidos, rs.getString("equipo1"), rs.getString("equipo2"));
                if (partido != null) {
                    Pronostico pronostico = new Pronostico(partido, equipo, resultadoEnum, persona);
                    pronosticos.add(pronostico);
                }
            }
        } catch (Exception e) {
            System.err.println("Error, " + e);
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
