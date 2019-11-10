package com.edd;

public class Alumno {
    String nombre;
    int carnet;
    int height;

    public Alumno(String name, int carnet){
        this.nombre = name;
        this.carnet = carnet;
        height = 1;
    }
    public Alumno(String name, String carnet){
        this.nombre = name;
        this.carnet = Integer.parseInt(carnet);
        height = 1;
    }
    public String get_visualization(){
        return "nombre: "+nombre+" carnet: "+carnet;
    }
}
