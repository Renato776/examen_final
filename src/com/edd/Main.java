package com.edd;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
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
            graph_traversal(arbol.get_preorder(),"preorder");
            graph_traversal(arbol.get_inorden(),"inorden");
            graph_traversal(arbol.get_postorder(),"postorder");
            graph_Treant(arbol,false);
        }
        else{
            System.out.println("File: "+file_name+" Does not exist. Operation failed.");
            return;
        }

    }
    public static void graph_traversal(Alumno[] traversal, String name){
        String graph = "digraph foo {rankdir=LR; node [shape=record];"+'\n';
        int i = 0;
        while(i<traversal.length){
            graph += "n"+i+" [label = \""+traversal[i].get_visualization()+"\"]; \n";
            if((i+1)<traversal.length){
                graph += "n"+i+" -> "+"n"+(i+1)+"; \n";
            }
            i++;
        }
        graph += "}";
        print_archive(name+".dot",graph);
    }
    public static void print_archive(String name, String content){
        File file = new File(name);
        try{
            file.createNewFile();
            PrintWriter writer = new PrintWriter(name, "UTF-8");
            writer.println(content);
            writer.close();
        }catch (Exception e){
        }
    }
    public static void graph_Treant(Arbol arbol,boolean show_all_details){
        String graph = "config = {\n" +
                "    container: \"#tree-simple\"\n" +
                "};\n";
        int i = 0;
        String node_names = "";
        boolean is_first = true;
        for (Alumno al: arbol.contenido) {
            if(al != null){
                if(is_first){
                    graph += "n"+i+"={text:{name: "+al.get_text(show_all_details)+" }}; \n";
                    node_names += ", n"+i;
                    is_first = false;
                }else{
                    graph += "n"+i+"={parent: n"+arbol.get_parent(i)+", text: {name: "+al.get_text(show_all_details)+" }}; \n";
                    node_names += ", n"+i;
                }
            }
            i++;
        }
        graph+= "simple_chart_config = [config "+node_names+"]; \n" +
                "var my_chart = new Treant(simple_chart_config);\n";
        print_archive("edd_tree_report.js",graph);
    }
}
