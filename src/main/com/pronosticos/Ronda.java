/*
 */
package main.com.pronosticos;

public class Ronda {

    private String nro;
    private Partido[] partidos;

    public Ronda(String nro, Partido[] partidos) {
        this.nro = nro;
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
