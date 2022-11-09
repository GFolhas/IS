package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;



public class App 
{
    public static void main( String[] args )
    {
        
        String path = System.getProperty("user.dir");
        path = path + "/outputs/";

        File f = new File(path);

        try {
            if(f.exists()){
                deleteDir(f);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        if (f.mkdir()) {
            System.out.println("Directory is created");
        }
        else {
            System.out.println("Directory cannot be created");
        }



        // ex.1

         WebClient.create("http://localhost:8080").get().uri("/student").retrieve().bodyToFlux(Student.class)
        .subscribe(s -> {

            String path12 = System.getProperty("user.dir");
            path12 = path12 + "/outputs/";

            File log = new File(path12 + "ex1.txt");
            
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
                System.out.println("Log failed!");
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // ex.2

        WebClient.create("http://localhost:8080").get().uri("/student").retrieve().bodyToFlux(Student.class)
        .count()
        .subscribe(v->{

            String path11 = System.getProperty("user.dir");
            path11 = path11 + "/outputs/";

            File log = new File(path11 + "ex2.txt");
            try{
            if(log.exists()==false){
                    System.out.println("We had to make a new file.");
                    log.createNewFile();
            }
            PrintWriter out = new PrintWriter(log);
            out.append("Total Students: " + v);
            out.close();
            }catch(IOException e){
                System.out.println("Log failed!");
            } 
        });    



        // ex.3

        

        WebClient.create("http://localhost:8080").get().uri("/student").retrieve().bodyToFlux(Student.class)
        .filter(s -> s.getCredits()!=180 )
        .count()
        .subscribe(v -> {

            String path10 = System.getProperty("user.dir");
            path10 = path10 + "/outputs/";

            File log = new File(path10 + "ex3.txt");
            try{
            if(log.exists()==false){
                    System.out.println("We had to make a new file.");
                    log.createNewFile();
            }
            PrintWriter out = new PrintWriter(log);
            out.append("Active Students: " + v);
            out.close();
            }catch(IOException e){
                System.out.println("Log failed!");
            } 
        }); 


         // ex.4

         WebClient.create("http://localhost:8080").get().uri("/student").retrieve().bodyToFlux(Student.class)
         .map(s -> {
 
            String path9 = System.getProperty("user.dir");
            path9 = path9 + "/outputs/";

             File log = new File(path9 + "ex4.txt");
             
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
                 System.out.println("Log failed!");
             }
             
             return s;
         })
         .subscribe();



          // ex.5

          WebClient.create("http://localhost:8080").get().uri("/student").retrieve().bodyToFlux(Student.class)
          .filter(s ->  s.getCredits() < 180 && s.getCredits() >= 120)
          .sort((s1, s2) -> {
            return s2.getCredits() - s1.getCredits();
          })
          .subscribe(s -> {  

            String path8 = System.getProperty("user.dir");
            path8 = path8 + "/outputs/";

            File log = new File(path8 + "ex5.txt");
        
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
                System.out.println("Log failed!");
            }
        });



        // ex.6
        ArrayList<Float> grades = new ArrayList<>();

        WebClient.create("http://localhost:8080").get().uri("/student").retrieve().bodyToFlux(Student.class)
        .subscribe(s -> {  

            String path7 = System.getProperty("user.dir");
            path7 = path7 + "/outputs/";
        
            File log = new File(path7 + "ex6.txt");
      
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

            System.out.println("\n\n\n\n");
            System.out.println(avg);
            System.out.println(standardDev);

            PrintWriter out = new PrintWriter(log);
            String toWrite = "Average: " + avg + "\nStandard Deviation: " + standardDev;
            out.append(toWrite);
            out.close();    

            }catch(IOException e){
                System.out.println("Log failed!");
            }
      });

    // ex 8

     DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");   

    WebClient.create("http://localhost:8080").get().uri("/student").retrieve().bodyToFlux(Student.class)
    .sort((s1, s2) -> {try {
            return formatter.parse(s1.getBirthdate()).compareTo(formatter.parse(s2.getBirthdate()));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return 0;
    })
    .take(1)
    .subscribe(s -> {

        String path5 = System.getProperty("user.dir");
        path5 = path5 + "/outputs/";
        File log = new File(path5 + "ex8.txt");
         
         try{
         if(log.exists()==false){
                 System.out.println("We had to make a new file.");
                 log.createNewFile();
         }
         PrintWriter out = new PrintWriter(new FileWriter(log, true));
         String toWrite = "Eldest Student: " + s.getName() + "\n";
         out.append(toWrite);
         out.close();
         }catch(IOException e){
             System.out.println("Log failed!");
         }
    }); 



          // ex 9

        WebClient client = WebClient.create("http://localhost:8080");

        client.get().uri("/student_teacher").retrieve().bodyToFlux(StudentTeacher.class)
        .count()
        .subscribe(s -> {  

        String path13 = System.getProperty("user.dir");
        path13 = path13 + "/outputs/";

        File log = new File(path13 + "ex9.txt");
    
        try{
        if(log.exists()==false){
                System.out.println("We had to make a new file.");
                log.createNewFile();
        }

        client.get()
                .uri("/student")
                .retrieve()
                .bodyToFlux(StudentTeacher.class)
                .count()
                .subscribe(v -> {

                    PrintWriter out;
                    try {
                        out = new PrintWriter(log);
                        String toWrite = "Average Number of Professors per Student: " + (float)s / v;
                        out.append(toWrite);
                        out.close();
                    } catch (FileNotFoundException e) {e.printStackTrace();}

                });  

        }catch(IOException e){
            System.out.println("Log failed!");
        } 
    });

                
        // ex 10

        WebClient wc = WebClient.create("http://localhost:8080");

        wc.get()
        .uri("/teacher/")
        .retrieve()
        .bodyToFlux(Teacher.class)
        .publishOn(Schedulers.boundedElastic())
        .map(v -> {

                Mono<Long> ml =  wc.get()
                .uri("/student_teacher")
                .retrieve()
                .bodyToFlux(StudentTeacher.class)
                .publishOn(Schedulers.boundedElastic())
                .filter(s ->  s.getTeacher_id() == v.getId())
                .count();

            return new TeacherCount(v.getName(), ml.block());
        })
        .sort((r1, r2) -> {
            return (int) (r2.getCounter() - r1.getCounter());
        })
        .subscribe(r -> {

            try{ 

                String path13 = System.getProperty("user.dir");
                path13 = path13 + "/outputs/";
                File log = new File(path13 + "ex10.txt");

                if(log.exists()==false){
                    System.out.println("We had to make a new file.");
                    log.createNewFile();
            }
                PrintWriter out = new PrintWriter(new FileWriter(log, true));
                String toWrite = "Name: " + r.getName() + "\tNumber of Students: " + r.getCounter() + "\n";
                out.append(toWrite);
                out.close();
                        
            } catch(IOException e){e.printStackTrace();}
        });



  /*       WebClient wc = WebClient.create("http://localhost:8080");

       wc.get()
        .uri("/teacher/")
        .retrieve()
        .bodyToFlux(Teacher.class)
        .sort((s1, s2) -> {
            return s1.getId() - s2.getId();
          })
        .subscribe(v -> {

            String path3 = System.getProperty("user.dir");
            path3 = path3 + "/outputs/Teachers/";
            
            File f3 = new File(path3);
              if (f3.mkdir()) {
                System.out.println("Directory is created");
            }
            else {
                System.out.println("Directory cannot be created");
            }

            File log = new File(path3 + v.getName() + ".txt");

            try{
                if(log.exists()==false){
                    System.out.println("We had to make a new file.");
                    log.createNewFile();
                }
                else{
                    System.out.println("File already exists.");  
                }

                wc.get()
                .uri("/student_teacher")
                .retrieve()
                .bodyToFlux(StudentTeacher.class)
                .filter(s ->  s.getTeacher_id() == v.getId())
                .count()
                .subscribe(w -> {
                    try{
                        PrintWriter out = new PrintWriter(log);
                        String toWrite = "Number of Students: " + w + "\n";
                        out.append(toWrite);
                        out.close();
                        
                    } catch(IOException e){e.printStackTrace();}
                });

                wc.get()
                .uri("/student_teacher")
                .retrieve()
                .bodyToFlux(StudentTeacher.class)
                .filter(s ->  s.getTeacher_id() == v.getId())
                .subscribe(s -> {

                    wc.get()
                    .uri("/student")
                    .retrieve()
                    .bodyToFlux(Student.class)
                    .filter(k -> k.getId() == s.getStudent_id())
                    .sort((k1, k2) -> {
                        return k2.getId() - k1.getId();
                      })
                    .subscribe( k -> {

                        try{
                            PrintWriter out = new PrintWriter(new FileWriter(log, true));
                            String toWrite = k.getName() + "\n";
                            out.append(toWrite);
                            out.close();
                            
                        } catch(IOException e){e.printStackTrace();}

                    });

                });

            } catch(IOException e){e.printStackTrace();}
        }); */


        // ex 11


        wc.get()
        .uri("/student/")
        .retrieve()
        .bodyToFlux(Student.class)
        .publishOn(Schedulers.boundedElastic())
        .sort((v1, v2) -> {
            return v1.getId() - v2.getId();
        })
        .map(v -> {


            Flux<String> t = wc.get()
            .uri("/student_teacher/")
            .retrieve()
            .bodyToFlux(StudentTeacher.class)
            .publishOn(Schedulers.boundedElastic())
            .filter(s -> s.getStudent_id() == v.getId())
            .map(s -> {

                Flux<String> x = wc.get()
                .uri("/teacher")
                .retrieve()
                .bodyToFlux(Teacher.class)
                .publishOn(Schedulers.boundedElastic())
                .filter(k ->  s.getTeacher_id() == k.getId())
                .map(k -> {
                    return k.getName();
                });

                String val = x.collectList().block().get(0);

                return val;
            });


            return new StudentFullInfo(v.getId(), v.getName(), v.getBirthdate(), v.getCredits(), v.getGrade(), t);
        })
        .subscribe(g -> {
            try{ 

                String path13 = System.getProperty("user.dir");
                path13 = path13 + "/outputs/";
                File log = new File(path13 + "ex11.txt");

                if(log.exists()==false){
                    System.out.println("We had to make a new file.");
                    log.createNewFile();
                }

                PrintWriter out = new PrintWriter(new FileWriter(log, true));

                String toWrite = "ID: " + g.getId() + 
                "\nName: " + g.getName() + 
                "\nBirthdate: " + g.getBirthdate() +
                "\nCredits: " + g.getCredits() +
                "\nGrade: " + g.getGrade() +
                "\nProfessors: ";

                out.append(toWrite);
                if(g.getProfs() != null && g.getProfs().collectList().block().size() > 0){
                    List<String> w = new ArrayList<>();

                    for(int i = 0; i < g.getProfs().collectList().block().size(); i++){
                        w.add(g.getProfs().collectList().block().get(i));
                    }

                    for(int i = 0; i < w.size()-1; i++){
                        toWrite = w.get(i) + ", ";
                        out.append(toWrite);
                    }
                    toWrite = w.get(w.size()-1);
                    out.append(toWrite);
                }
                out.append("\n\n");
                out.close();
                        
            } catch(IOException e){e.printStackTrace();}
        });







        /* wc.get()
        .uri("/student/")
        .retrieve()
        .bodyToFlux(Student.class)
        .publishOn(Schedulers.boundedElastic())
        .map(v -> {
            
            Flux<StudentFullInfo> tmp = wc.get()
            .uri("/student_teacher")
            .retrieve()
            .bodyToFlux(StudentTeacher.class)
            .publishOn(Schedulers.boundedElastic())
            .filter(s ->  s.getStudent_id() == v.getId())
            .map(s -> {

                Flux<String> c = wc.get()
                .uri("/teacher")
                .retrieve()
                .bodyToFlux(Teacher.class)
                .publishOn(Schedulers.boundedElastic())
                .filter(k ->  s.getTeacher_id() == k.getId())
                .map(k -> {
                    return k.getName();
                });

                //return "test";
                return new StudentFullInfo(v.getId(), v.getName(), v.getBirthdate(), v.getCredits(), v.getGrade(), c);
            });


            return tmp;
        })
        .subscribe(g -> {

            try{ 

                String path13 = System.getProperty("user.dir");
                path13 = path13 + "/outputs/";
                File log = new File(path13 + "ex11.txt");

                if(log.exists()==false){
                    System.out.println("We had to make a new file.");
                    log.createNewFile();
                }

                Mono<List<StudentFullInfo>> tmp = g.collectList();

                PrintWriter out = new PrintWriter(new FileWriter(log, true));
                for(int i = 0; i < tmp.block().size(); i++){
                    String toWrite = "Name: " + tmp.block().get(i).getName() + "\n";
                    out.append(toWrite);
                }
                out.close();
                        
            } catch(IOException e){e.printStackTrace();}
        });

 */


       /*  wc.get()
        .uri("/student")
        .retrieve()
        .bodyToFlux(Student.class)
        .sort((s1, s2) -> {
            return s1.getId() - s2.getId();
          })
        .subscribe(s -> {

            String path2 = System.getProperty("user.dir");
            path2 = path2 + "/outputs/ex11/";

            File f2 = new File(path2);
              if (f2.mkdir()) {
                System.out.println("Directory is created");
            }
            else {
                System.out.println("Directory cannot be created");
            }


            File log = new File(path2 + s.getName() + "'s Data.txt");

            try{
                if(log.exists()==false){
                    System.out.println("We had to make a new file.");
                    log.createNewFile();
                }
                else{
                    System.out.println("File already exists.");  
                }


                PrintWriter out = new PrintWriter(new FileWriter(log, true));
                String toWrite = "ID: " + s.getId() + "\nName: " + s.getName() + "\nBirthdate: " + s.getBirthdate() + "\nCredits: " + s.getCredits() + "\nGrade: " + s.getGrade() + "\nProfessors:\n";
                out.append(toWrite);
                out.close();

                wc.get()
                .uri("/student_teacher")
                .retrieve()
                .bodyToFlux(StudentTeacher.class)
                .filter(st ->  st.getStudent_id() == s.getId())
                .subscribe(st -> {

                    wc.get()
                    .uri("/teacher")
                    .retrieve()
                    .bodyToFlux(Teacher.class)
                    .filter(t -> t.getId() == st.getTeacher_id())
                    .sort((t1, t2) -> {
                        return t2.getId() - t1.getId();
                      })
                    .subscribe(t -> {

                        try{
                            PrintWriter out2 = new PrintWriter(new FileWriter(log, true));
                            String toWrit = "\t\t\t-" + t.getName() + "\n";
                            out2.append(toWrit);
                            out2.close();
                        } catch(IOException e){e.printStackTrace();}

                    });


                });

            
            } catch(IOException e){e.printStackTrace();}
        });

        
        // ex 7

        ArrayList<Float> grades2 = new ArrayList<>();
        
        WebClient.create("http://localhost:8080").get().uri("/student").retrieve().bodyToFlux(Student.class)
        .filter(s -> s.getCredits() == 180)
        .subscribe(s -> {  

            String path6 = System.getProperty("user.dir");
            path6 = path6 + "/outputs/";

            File log = new File(path6 + "ex7.txt");
        
            try{
            if(log.exists()==false){
                System.out.println("We had to make a new file.");
                log.createNewFile();
            }

            grades2.add(s.getGrade());

            
            float std = 0;
            float allSum = 0;
            for(float el : grades2){
                allSum += el;
            }
                    
            float avg = allSum / grades2.size();

        for(float temp: grades2) {
            std += Math.pow(temp - avg, 2);
        }
        double standardDev = Math.sqrt(std/grades2.size());


            PrintWriter out = new PrintWriter(log);
            String toWrite = "Average: " + avg + "\nStandard Deviation: " + standardDev;
            out.append(toWrite);
            out.close();    

            }catch(IOException e){
                System.out.println("Log failed!");
            }
        }); 
 */
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    static void deleteDir(File f) throws IOException {
        if (f.isDirectory()) {
          File[] content = f.listFiles();
          if (content != null) {
            for (File entry : content) {
              deleteDir(entry);
            }
          }
        }
        if (!f.delete()) {
          throw new IOException("Failed to delete file " + f);
        }
      }


}
