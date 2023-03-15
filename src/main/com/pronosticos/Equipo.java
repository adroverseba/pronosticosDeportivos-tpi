/*
*/
package main.com.pronosticos;

public class Equipo {
    private String nombre;
    private String descripcion;
    
    public Equipo(String nombre, String descripcion){
        this.nombre = nombre;
        this.descripcion=descripcion;
    }
    
    //getters y setters
    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
}
