package com.edd;

import java.io.PrintWriter;
import java.util.Scanner;

public class Printing {
    String[] options;
    Scanner in;
    public Printing(int options){
        this.options = new String[options];
        i = 0;
        in = new Scanner(System.in);
    }
    private int i;
    public void add(String opt){
        options[i] = opt;
        i++;
    }
    public void print_options(){
        int c= 0;
        System.out.println("Type the number of the option you'd like to choose:");
        for (String opt: options
             ) {
            System.out.println(c+") "+opt);
            c++;
        }
    }
    public int get_choice(){
        int choice = in.nextInt();
        while (choice<0 || choice>=options.length){
            System.out.println("Not a valid option. Try again: ");
            choice = in.nextInt();
        }
        return choice;
    }
}
