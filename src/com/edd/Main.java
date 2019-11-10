package com.edd;
import java.awt.*;
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
        Printing aux = new Printing(6);
        aux.add("Cargar Archivo");
        aux.add("Reporte de recorrido inorden");
        aux.add("Reporte de recorrido preorden");
        aux.add("Reporte de recorrido postorden");
        aux.add("Reporte del arbol avl");
        aux.add("salir");
        System.out.println("Examen Final");
        int choice = 0;
        while (choice != (aux.options.length-1)){
            aux.print_options();
            choice = aux.get_choice();
            if(choice == 0){
                arbol = new Arbol();
                System.out.println("Escribe el nombre del archivo csv a cargar: ");
                //Cargar Archivo
                String file_name = in.nextLine();
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
                                    continue;
                                }
                            }
                        }
                    }catch (Exception e){
                        System.out.println(e.toString());
                        System.out.println(e.getMessage());
                        System.out.println("An Error occurred while reading file: "+file_name+" Operation failed.");
                    }
                    System.out.println("Finished loading all students!!");
                 }
                else{
                    System.out.println("File: "+file_name+" Does not exist. Operation failed.");
                    continue;
                }
            } else if(choice == 1){
                //Recorrido inorden
                graph_traversal(arbol.get_inorden(),"inorden");
                try{
                    Process p = Runtime.getRuntime().exec("dot "+"inorden"+".dot -Tpng -o "+"inorden"+".png");
                    Thread.currentThread().sleep(2000);
                    Process p1 = Runtime.getRuntime().exec("eog "+"inorden"+".png");
                    System.out.println("If the Image is too big, I recommend open it manually with a more potent viewer.");
                }catch (Exception ex){
                    System.out.println("An error occurred while trying to compile the dot code: "+ex.toString());
                }
            }else if(choice == 2){
                //Recorrido preorden:
                graph_traversal(arbol.get_preorder(),"preorden");
                try{
                    Process p = Runtime.getRuntime().exec("dot "+"preorden"+".dot -Tpng -o "+"preorden"+".png");
                    Thread.currentThread().sleep(2000);
                    Process p1 = Runtime.getRuntime().exec("eog "+"preorden"+".png");
                }catch (Exception ex){
                    System.out.println("An error occurred while trying to compile the dot code: "+ex.toString());
                }
            }else if(choice == 3){
                //Recorrido post orden:
                graph_traversal(arbol.get_postorder(), "postorder");
                try{
                    Process p = Runtime.getRuntime().exec("dot "+"preorden"+".dot -Tpng -o "+"preorden"+".png");
                    Thread.currentThread().sleep(2000);
                    Process p1 = Runtime.getRuntime().exec("eog "+"preorden"+".png");
                }catch (Exception ex){
                    System.out.println("An error occurred while trying to compile the dot code: "+ex.toString());
                }
                //Use eog to open images.
            }else if(choice == 4){
                graph_Treant(arbol,false);
                try{
                    File htmlFile = new File("index.html");
                    Desktop.getDesktop().browse(htmlFile.toURI());
                }catch (Exception ex){
                    System.out.println("An error while opening the html with default browser. However, you can open it manually.");
                }
            }
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
