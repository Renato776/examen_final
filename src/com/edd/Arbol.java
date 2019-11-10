package com.edd;

public class Arbol {
    Alumno[] contenido;
    int size;
    public Arbol(){
        size = 100;
        contenido = new Alumno[size];
    }
    public void insert(Alumno alumno){
        if(contenido[0] == null){
            //Empty tree, we set the root:
            contenido[0] = alumno;
            return;
        }
        insert(0,alumno);
    }
    private void insert(int node, Alumno alumno){
        if(alumno.carnet<contenido[node].carnet){
            //Goes to the left
            int izq = left(node);
            if(izq >= size){
                //Overflow alert!!!
                //Either, resize or throw exception.
                System.out.println("Overflow exception. Could not insert alumno: "+alumno.get_visualization());
                return;
            }
            if(contenido[izq] == null){
                //There's no left child. Insert here:
                contenido[izq] = alumno;
            }else{
                //A left child already exists, insert there:
                insert(izq,alumno);
            }
        }else{
            //Goes to the right
            int der = right(node);
            if(der >= size){
                //Overflow alert!!!
                //Either, resize or throw exception.
                System.out.println("Overflow exception. Could not insert alumno: "+alumno.get_visualization());
                return;
            }
            if(contenido[der] == null){
                //There's no right child. Insert here:
                contenido[der] = alumno;
            }else{
                //A right child already exists, insert there:
                insert(der,alumno);
            }
        }
    }
    public int get_parent(int index){
        if(index == 0) return 0;
        double res = (index-1)/2;
        return (int) Math.floor(res);
    }
    public int left(int index){
        return 2*index + 1;
    }
    public int right(int index){
        return 2*index + 2;
    }
    public void simple_visualization(){
        int i = 0;
        for (Alumno al: contenido
             ) {
            String res = ""+i+") ";
            if(al == null){
                res = res + "null.";
                System.out.println(res);
            }else{
                System.out.println(res+al.get_visualization());
            }
            i++;
        }
    }
}
