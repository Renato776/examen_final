package com.edd;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
public class Main {

    public static void main(String[] args) {
	// write your code here
        Scanner in = new Scanner(System.in);
        Arbol arbol = new Arbol();
        System.out.println("Examen Final");
        System.out.println("Puede escribir \"exit\" cada vez que se le pregunte input para finalizar la ejecucion.");
        System.out.println("Escriba el nombre del archivo csv que desea cargar:");
        String file_name = in.nextLine();
        if(file_name.equals("exit")){
            return;
        }
        File file = new File(file_name);
        if(file.exists()){
            try{
                BufferedReader br = new BufferedReader(new FileReader(file));
                String st;
                while ((st = br.readLine()) != null)
                {
                    String[] datos;
                    datos = st.split(",");
                    if(datos.length != 2){
                        System.out.println("Syntax error in line: "+st);
                        System.out.println("Skipped this line.");
                        continue;
                    }
                    boolean is_number = false;
                    try{
                        int io = Integer.parseInt(datos[0]);
                        is_number = true;
                    }catch (Exception ex){
                        is_number = false;
                    }
                    if(is_number){
                        Alumno alumno = new Alumno(datos[1],datos[0]);
                        arbol.insert(alumno);
                    }else{
                        if(!datos[0].trim().toLowerCase().equals("carne")){
                            System.out.println("El carnet del alumno debe ser un numero. Se encontro: "+st);
                            System.out.println("Se ignorara esta linea");
                        }
                    }
                }
            }catch (Exception e){
                System.out.println(e.toString());
                System.out.println(e.getMessage());
                System.out.println("An Error occurred while reading file: "+file_name+" Operation failed.");
            }
            System.out.println("Finished loading all students!!");
            arbol.simple_visualization();
        }
        else{
            System.out.println("File: "+file_name+" Does not exist. Operation failed.");
            return;
        }

    }
}
