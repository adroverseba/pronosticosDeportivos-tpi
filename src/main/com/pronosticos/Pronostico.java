/*
 */
package main.com.pronosticos;

public class Pronostico {

    private Partido partido;
    private Equipo equipo;
    private ResultadoEnum resultado;

    public Pronostico(Partido partido, ResultadoEnum resultado) {
        this.partido = partido;
        this.resultado = resultado;
    }

    public int puntos() {
        int puntos = 0;
        ResultadoEnum resultadoPartido = partido.resultado(equipo);
        if (resultado == resultadoPartido) {
            puntos = 1;
        }
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
    
    
}
