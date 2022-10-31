package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.springframework.web.reactive.function.client.WebClient;

public class App 
{
    public static void main( String[] args )
    {
        

        // ex.1

/*         WebClient.create("http://localhost:8080").get().uri("/student").retrieve().bodyToFlux(Student.class)
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



        // ex.6
         ArrayList<Float> grades = new ArrayList<>();

        WebClient.create("http://localhost:8080").get().uri("/student").retrieve().bodyToFlux(Student.class)
        .subscribe(s -> {  

          File log = new File("avg_std.txt");
      
          try{
          if(log.exists()==false){
                  System.out.println("We had to make a new file.");
                  log.createNewFile();
          }

          grades.add(s.getGrade());

          float std = 0;
          float allSum = 0;
          for(float el : grades){
            allSum += el;
          }
 
          float avg = allSum / grades.size();

         for(float temp: grades) {
            std += Math.pow(temp - avg, 2);
         }
         double standardDev = Math.sqrt(std/grades.size());


          PrintWriter out = new PrintWriter(log);
          String toWrite = "Average: " + avg + "\nStandard Deviation: " + standardDev;
          out.append(toWrite);
          out.close();    

          }catch(IOException e){
              System.out.println("COULD NOT LOG!!");
          }
      });



      // ex 7

      //grades.clear();
      
      WebClient.create("http://localhost:8080").get().uri("/student").retrieve().bodyToFlux(Student.class)
      .filter(s -> s.getCredits() == 180)
      .subscribe(s -> {  

        File log = new File("finalist_avg_std.txt");
    
        try{
        if(log.exists()==false){
                System.out.println("We had to make a new file.");
                log.createNewFile();
        }

        grades.add(s.getGrade());

        
        float std = 0;
        float allSum = 0;
        for(float el : grades){
            allSum += el;
        }
                
        float avg = allSum / grades.size();

       for(float temp: grades) {
          std += Math.pow(temp - avg, 2);
       }
       double standardDev = Math.sqrt(std/grades.size());


        PrintWriter out = new PrintWriter(log);
        String toWrite = "Average: " + avg + "\nStandard Deviation: " + standardDev;
        out.append(toWrite);
        out.close();    

        }catch(IOException e){
            System.out.println("COULD NOT LOG!!");
        }
    }); */


/*           // ex 8

      ArrayList<Date> bd = new ArrayList<>();
      ArrayList<String> bd2 = new ArrayList<>();
      ArrayList<String> names = new ArrayList<>();
      
      WebClient.create("http://localhost:8080").get().uri("/student").retrieve().bodyToFlux(Student.class)
      .subscribe(s -> {  

        File log = new File("eldest.txt");
    
        try{
        if(log.exists()==false){
                System.out.println("We had to make a new file.");
                log.createNewFile();
        }

        bd2.add(s.getBirthdate());
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date birth;
        birth = formatter.parse(s.getBirthdate());
        names.add(s.getName());
        bd.add(birth);

        Date minDate = Collections.min(bd);
        int index = 0;
        for(int i = 0; i < bd.size(); i++){
            if(bd.get(i).compareTo(minDate) == 0){
                index = i;
            }
        }


        PrintWriter out = new PrintWriter(log);
        String toWrite = "Eldest Student: " + names.get(index);
        out.append(toWrite);
        out.close();    

        }catch(IOException e){
            System.out.println("COULD NOT LOG!!");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }); */



         // ex 9
/* 
         try{
            String nos;
            
            BufferedReader br = new BufferedReader(new FileReader("total_students.txt"));
            
            nos = br.readLine().split(": ")[1];

            br.close();

        int NumOfStudents = Integer.parseInt(nos);


        //TODO: Add students without teachers to the sql table in a way tbat they also show up and are accounted for in the counting process
        ArrayList<Integer> arr = new ArrayList<>();
        ArrayList<Integer> rows = new ArrayList<>();

        
        WebClient.create("http://localhost:8080").get().uri("/student_teacher").retrieve().bodyToFlux(StudentTeacher.class)
        .sort((s1, s2) -> {
            return s1.getStudent_id() - s2.getStudent_id();
          })
        .subscribe(s -> {  

          rows.add(1);
  
          File log = new File("avg_teacher_per_student.txt");
      
          try{
          if(log.exists()==false){
                  System.out.println("We had to make a new file.");
                  log.createNewFile();
          }

          if(!arr.contains(s.getStudent_id())){
            arr.add(s.getStudent_id());
          }
  
          
  
          PrintWriter out = new PrintWriter(log);
          String toWrite = "Average Number of Professors per Student: " + (float)rows.size() / NumOfStudents;
          out.append(toWrite);
          out.close();    
  
          }catch(IOException e){
              System.out.println("COULD NOT LOG!!");
          } 
      });
                
        }catch(IOException e){
            e.printStackTrace();
        }
 */
 

      //TODO: Add an arraylist to teachers so i can continue to account for the students of each teacher in a way this can be done automatically in this webclient
        WebClient.create("http://localhost:8080").get().uri("/teacher").retrieve().bodyToFlux(Teacher.class)
        .sort((s1, s2) -> s1.getId() - s2.getId())
        .subscribe(s -> {

            
            File log = new File("teachers.txt");
        
            try{
            if(log.exists()==false){
                    System.out.println("We had to make a new file.");
                    log.createNewFile();
            }
            PrintWriter out = new PrintWriter(new FileWriter(log, true));
            String toWrite = s.getName() + "\n";
            out.append(toWrite);
            out.close();
            }catch(IOException e){
                System.out.println("COULD NOT LOG!!");
            }

        });

        WebClient.create("http://localhost:8080").get().uri("/student").retrieve().bodyToFlux(Student.class)
        .sort((s1, s2) -> s1.getId() - s2.getId())
        .subscribe(s -> {

            
            File log = new File("students.txt");
        
            try{
            if(log.exists()==false){
                    System.out.println("We had to make a new file.");
                    log.createNewFile();
            }
            PrintWriter out = new PrintWriter(new FileWriter(log, true));
            String toWrite = s.getName() + "\n";
            out.append(toWrite);
            out.close();
            }catch(IOException e){
                System.out.println("COULD NOT LOG!!");
            }

        });

        WebClient.create("http://localhost:8080").get().uri("/student_teacher").retrieve().bodyToFlux(StudentTeacher.class)
        .sort((s1, s2) -> s1.getStudent_id() - s2.getStudent_id())
        .subscribe(s -> {

            
            File log = new File("relationship.txt");
        
            try{
            if(log.exists()==false){
                    System.out.println("We had to make a new file.");
                    log.createNewFile();
            }
            PrintWriter out = new PrintWriter(new FileWriter(log, true));
            String toWrite = s.getStudent_id() + " - " + s.getTeacher_id() + "\n";
            out.append(toWrite);
            out.close();
            }catch(IOException e){
                System.out.println("COULD NOT LOG!!");
            }

        });


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<String> teach = new ArrayList<>();
        ArrayList<String> stud = new ArrayList<>();
        ArrayList<String> rela = new ArrayList<>();

        try {
        BufferedReader br = new BufferedReader(new FileReader("teachers.txt"));
 
        String value;

        while ((value = br.readLine()) != null){
            teach.add(value);
        }
        
        br.close();

        br = new BufferedReader(new FileReader("students.txt"));

        while ((value = br.readLine()) != null){
            stud.add(value);
        }

        br.close();

        br = new BufferedReader(new FileReader("relationship.txt"));

        while ((value = br.readLine()) != null){
            rela.add(value);
        }

        br.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }


        //TODO: Compare all the arraylists and select accordingly (see below)


        File log = new File("students_per_prof.txt");

        try{
        if(log.exists()==false){
                System.out.println("We had to make a new file.");
                log.createNewFile();
            }     

            PrintWriter out = new PrintWriter(new FileWriter(log, true));
            for(int i = 0; i < teach.size(); i++){
                
                String toWrite = "Teacher: " + teach.get(i) + "\n";
                out.append(toWrite);
                for(int j = 0; j < rela.size(); j++){
                    if(Integer.parseInt(rela.get(j).split(" - ")[1])-1 == i){
                        int tmp = Integer.parseInt(rela.get(j).split(" - ")[0]);
                        toWrite = "\tStudent: " + stud.get(tmp-1) + "\n";
                        out.append(toWrite);
                    }
                }
                out.append("\n");
            }
            out.close();
            
        }catch(IOException e){
            System.out.println("COULD NOT LOG!!");
        }    
        
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
   
    }
}
