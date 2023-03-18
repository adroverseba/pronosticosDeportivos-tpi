/*
 */
package main.com.pronosticos;

import java.util.ArrayList;
import java.util.List;

public class Ronda {
    
    private String nro;
    private List<Partido> partidos=new ArrayList<>();
    
    public Ronda() {
    }
    
    public Ronda(String nro, List<Partido> partidos) {
        this.nro = nro;
        this.partidos = partidos;
    }

    //agregar partido a la lista de partidos
    public void agregarPartido(Partido partido) {
        partidos.add(partido);
    }

    //getter y setters
    public String getNro() {
        return nro;
    }
    
    public List<Partido> getPartidos() {
        return partidos;
    }
    
    public void setNro(String nro) {
        this.nro = nro;
    }
    
    public void setPartidos(List<Partido> partidos) {
        this.partidos = partidos;
    }
    

//    public int puntos() {
//        int puntosTotales = 0;
//        for (Partido partido : partidos) {
//            //verificar  si el equipo1 gano el partido
//            if (partido.resultado(partido.getEquipo1()) == ResultadoEnum.GANADOR || ResultadoEnum.EMPATE) {
//                puntosTotales += 1;
//            } else if (partido.resultado(partido.getEquipo1()) == ResultadoEnum.EMPATE) {
//
//            }
//        }
//    }
}
