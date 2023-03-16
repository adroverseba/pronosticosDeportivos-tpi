/*
 */
package main.com.pronosticos;

public class Pronostico {

    private Partido partido;
    private Equipo equipo;
    private ResultadoEnum resultado;
    private Persona persona;

    public Pronostico(Partido partido, Equipo equipo, ResultadoEnum resultado, Persona persona) {
        this.partido = partido;
        this.equipo = equipo;
        this.resultado = resultado;
        this.persona = persona;
    }

    public int puntos() {
        int puntos = 1;
//        ResultadoEnum resultadoPartido = partido.resultado(equipo);
//        if (resultado == resultadoPartido) {
//            puntos = 1;
//        }
        return puntos;
    }

    public Partido getPartido() {
        return partido;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public ResultadoEnum getResultado() {
        return resultado;
    }

    public Persona getPersona() {
        return persona;
    }

}
