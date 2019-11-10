package com.edd;

public class Alumno {
    String nombre;
    int carnet;
    public Alumno(String name, int carnet){
        this.nombre = name;
        this.carnet = carnet;
    }
    public Alumno(String name, String carnet){
        this.nombre = name;
        this.carnet = Integer.parseInt(carnet);
    }
    public String get_visualization(){
        return "nombre: "+nombre+" carnet: "+carnet;
    }
}
