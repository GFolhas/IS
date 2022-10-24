package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.springframework.web.reactive.function.client.WebClient;

public class App 
{
    public static void main( String[] args )
    {
        

        // ex.1

        WebClient.create("http://localhost:8080").get().uri("/student").retrieve().bodyToFlux(Student.class)
        .map(s -> {

            File log = new File("names_and_birthdates.txt");
            //File log = new File("log.txt");
            
            try{
            if(log.exists()==false){
                    System.out.println("We had to make a new file.");
                    log.createNewFile();
            }
            PrintWriter out = new PrintWriter(new FileWriter(log, true));
            String toWrite = s.getName() + " | " + s.getBirthdate() + "\n";
            out.append(toWrite);
            out.close();
            }catch(IOException e){
                System.out.println("COULD NOT LOG!!");
            }
            
            return s;
        })
        .subscribe();


        // ex.2

        ArrayList<Integer> ids = new ArrayList<>();

        WebClient.create("http://localhost:8080").get().uri("/student").retrieve().bodyToFlux(Student.class)
        .map(s -> {
            File log = new File("total_students.txt");
            ids.add(s.getId());
            try{
            if(log.exists()==false){
                    System.out.println("We had to make a new file.");
                    log.createNewFile();
            }
            PrintWriter out = new PrintWriter(log);
            out.append("Total Students: " + ids.size());
            out.close();
            }catch(IOException e){
                System.out.println("COULD NOT LOG!!");
            } 
            return s;
        })
        .subscribe();       


        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
   
    }
}
