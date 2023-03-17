/*
    clase liga encarga de llevar la lista de todas las personas que participan 
 */
package main.com.pronosticos;

import java.util.ArrayList;
import java.util.List;

public class Liga {

    private List<Persona> personas;

    public Liga() {
        personas = new ArrayList<>();
    }

    //metodo para agregar personas a la lista
    public void agregarPersona(Persona persona) {
        personas.add(persona);
    }

    //metodo para imprimir los puntajes de todas las personas 
    public void imprimirPuntajes() {
        for (Persona persona : personas) {
            System.out.println("Nombre: " + persona.getNombre() + ", Puntaje: " + persona.getPuntaje());
        }
    }

    //buscar a una persona por nombre
    public boolean buscarPersonaNombre(String nombrePersona) {
        for (Persona persona : personas) {
            if (persona.getNombre().equalsIgnoreCase(nombrePersona)) {
                return true;
            }
        }
        return false;
    }
    
    public Persona obtenerPersonaPorNombre(String nombrePersona) {
        for (Persona persona : personas) {
            if (persona.getNombre().equalsIgnoreCase(nombrePersona)) {
                return persona;
            }
        }
        return null;
    }

    public List<Persona> getPersonas(){
        return personas;
    }
}
