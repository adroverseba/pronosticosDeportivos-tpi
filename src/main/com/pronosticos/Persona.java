/*
*/
package main.com.pronosticos;

public class Persona {

    private String nombre;
    private int puntaje;

    //constructor
    public Persona(String nombre) {
        this.nombre = nombre;
        this.puntaje = 0;
    }

    //getter y setters
    public String getNombre() {
        return nombre;
    }

    public int getPuntaje() {
        return puntaje;
    }
    

    //metodo sumar puntaje
    public void sumarPuntaje(int puntos) {
        this.puntaje += puntos;
    }

}
