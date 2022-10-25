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

        WebClient.create("http://localhost:8080").get().uri("/student").retrieve().bodyToFlux(Student.class)
        .count()
        .subscribe(v->{

            File log = new File("total_students.txt");
            try{
            if(log.exists()==false){
                    System.out.println("We had to make a new file.");
                    log.createNewFile();
            }
            PrintWriter out = new PrintWriter(log);
            out.append("Total Students: " + v);
            out.close();
            }catch(IOException e){
                System.out.println("COULD NOT LOG!!");
            } 
        });       



        // ex.3

        

        WebClient.create("http://localhost:8080").get().uri("/student").retrieve().bodyToFlux(Student.class)
        .filter(s -> s.getCredits()!=180 )
        .count()
        .subscribe(v -> {
            File log = new File("active_students.txt");
            try{
            if(log.exists()==false){
                    System.out.println("We had to make a new file.");
                    log.createNewFile();
            }
            PrintWriter out = new PrintWriter(log);
            out.append("Active Students: " + v);
            out.close();
            }catch(IOException e){
                System.out.println("COULD NOT LOG!!");
            } 
        }); 


         // ex.4

         WebClient.create("http://localhost:8080").get().uri("/student").retrieve().bodyToFlux(Student.class)
         .map(s -> {
 
             File log = new File("total_courses.txt");
             
             try{
             if(log.exists()==false){
                     System.out.println("We had to make a new file.");
                     log.createNewFile();
             }
             PrintWriter out = new PrintWriter(new FileWriter(log, true));
             int courses = s.getCredits()/6;
             String toWrite = s.getName() + " -> " + courses + " courses completed\n";
             out.append(toWrite);
             out.close();
             }catch(IOException e){
                 System.out.println("COULD NOT LOG!!");
             }
             
             return s;
         })
         .subscribe(v -> {

            // if i want i can open file, read everything and then sum all the courses
         });



          // ex.5

          WebClient.create("http://localhost:8080").get().uri("/student").retrieve().bodyToFlux(Student.class)
          .filter(s ->  s.getCredits() < 180 && s.getCredits() >= 120)
          .sort((s1, s2) -> {
            return s2.getCredits() - s1.getCredits();
          })
          .subscribe(s -> {  

            File log = new File("finalists.txt");
        
            try{
            if(log.exists()==false){
                    System.out.println("We had to make a new file.");
                    log.createNewFile();
            }
            PrintWriter out = new PrintWriter(new FileWriter(log, true));
            String toWrite = s.getName() + " | " + s.getBirthdate()  + " | " + s.getCredits() + " credits | " + s.getGrade() + "/20 (grade)\n";
            out.append(toWrite);
            out.close();
            }catch(IOException e){
                System.out.println("COULD NOT LOG!!");
            }
        });



        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
   
    }
}
