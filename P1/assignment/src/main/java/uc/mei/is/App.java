package uc.mei.is;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.time.LocalDate;

public class App {

    public static void main(String[] args) throws IOException {

        JAXBContext jaxbContext = null;

        try {
        
            jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
                    .createContext(new Class[]{Teachers.class}, null);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            //Random nt = new Random();

            //TODO: CHANGE NUMBER OF TEACHERS PER EXECUTION
            int numberOfTeachers = 100;
            //int numberOfTeachers = nt.nextInt(800-799) + 799;
            Teachers teacherList = new Teachers();
            teacherList.setTeacher(new ArrayList<Teacher>());


            String fnames[] = new String[] {"Michael", "Christopher","Jessica","Matthew","Ashley","Jennifer","Joshua","Amanda","Daniel","David","James","Robert","John","Joseph","Andrew","Ryan","Brandon","Jason","Justin","Sarah","William","Jonathan","Stephanie","Brian","Nicole","Nicholas","Anthony","Heather","Eric",
                "Elizabeth","Adam","Megan","Melissa","Kevin","Steven","Thomas","Timothy","Christina","Kyle","Rachel","Laura","Lauren","Amber","Brittany","Danielle","Richard","Kimberly","Jeffrey","Amy","Crystal","Michelle","Tiffany","Jeremy","Benjamin","Mark","Emily","Aaron","Charles","Rebecca","Jacob","Stephen","Patrick"};

            String lnames[] = new String[] {"Hill","Puckett","Song","Hamilton","Bender","Wagner","McLaughlin","McNamara","Raynor","Moon","Woodard","Desai","Wallace","Lawrence","Griffin","Dougherty","Powers","May","Steele","Teague","Vick","Gallagher","Solomon","Walsh","Monroe","Connolly","Hawkins","Middleton","Goldstein",
                "Watts","Johnston","Weeks","Wilkerson","Barton","Walton","Hall","Ross","Woods","Mangum","Joseph","Rosenthal","Bowden","Underwood","Jones","Baker","Merritt","Cross","Cooper","Holmes","Sharpe","Morgan","Hoyle","Allen","Rich","Grant","Proctor","Diaz","Graham","Watkins","Hinton","Marsh","Hewitt","Branch","O'Brien",
                "Case","Christensen","Parks","Hardin","Lucas","Eason","Davidson","Whitehead","Rose"};


            int totalStudents = 0;

            long start = System.nanoTime();
            School.Teachers.Builder tbuild = new School.Teachers().newBuilder();
            long finish =   System.nanoTime();
            long structTime = finish - start;

            for(int i = 0; i < numberOfTeachers; i++){
                Students sl = new Students();
                sl.setStudents(new ArrayList<Student>());
                Teacher t = createTeacher(fnames, lnames, i, sl);
                teacherList.getTeacher().add(t);
                        
                
                //Random stud = new Random();
                
                //TODO: CHANGE NUMBER OF STUDENTS PER EXECUTION
                int numberOfStudents = 15;
                //int numberOfStudents = stud.nextInt(25-0) + 0;
                totalStudents += numberOfStudents;
                
                // create a school.students.builder obj
                School.Students.Builder schoolStudents = new School.Students().newBuilder();
                
                for(int j = 0; j < numberOfStudents; j++){
                    Student s = createStudent(fnames, lnames, j, t.getName());
                    start = System.nanoTime();
                    School.Student s1 = createStudentPB(s.getId(), s.getName(), s.getPhone(), s.getGender(), s.getBirthDate(), s.getRegistrationDate(), s.getAddress(), s.getProfessor());
                    finish = System.nanoTime();
                    structTime = structTime + (finish - start);
                    sl.addStudent(s);
                    
                    // add school.student obj to school.students obj
                    schoolStudents.addStudents(s1);
                }
                start = System.nanoTime();
                School.Students sll = schoolStudents.build(); 
                School.Teacher t1 = createTeacherPB(t.getId(), t.getName(), t.getPhone(), t.getBirthDate(), t.getAddress(), sll);
                finish = System.nanoTime();
                structTime = structTime + (finish - start);
            
                tbuild.addTeachers(t1);
            }

            start = System.nanoTime();
            School.Teachers allTeachers = tbuild.build();
            finish = System.nanoTime();
            structTime = structTime + (finish - start);
            double st = (double) structTime / 1_000_000_000;
            DecimalFormat df = new DecimalFormat("0.0000");


            String path = System.getProperty("user.dir") + "\\files\\SchoolXML_" + String.valueOf(numberOfTeachers) + "_" + String.valueOf(totalStudents) + ".xml";
            System.out.println("\n\tGENERAL INFO\n");
            System.out.println(String.valueOf(numberOfTeachers) + " Teachers and " + String.valueOf(totalStudents) + " Students created");
            System.out.println("Time Elapsed Creating PB Structures: " + String.valueOf(df.format(st)) + " seconds");
            System.out.println("\n\tXML INFO\n");

            // set start timer
            start = System.nanoTime();

            // output to a xml file
            jaxbMarshaller.marshal(teacherList, new File(path));
            finish = System.nanoTime();
            long timeElapsed = finish - start;
            double ets = (double) timeElapsed / 1_000_000_000;
            double xmlTime = ets;
            
            System.out.println("File created at \\" + path);
            System.out.println("XML Elapsed Time: " + String.valueOf(df.format(ets)) + " seconds");
            
            long fileSize = Files.size(Paths.get(path));
            System.out.println("XML File Size: " + String.valueOf(fileSize) + " bytes\n\n");

            System.out.println("\n\tXML + GZIP INFO\n");

            // output to a xml compressed with gzip file
            start = System.nanoTime();
            String gzPath = gzip(path, numberOfTeachers, totalStudents);
            finish = System.nanoTime();
            timeElapsed = finish - start;
            ets = (double) timeElapsed / 1_000_000_000;

            System.out.println("File created at \\" + gzPath);
            System.out.println("XML + GZIP Elapsed Time: " + String.valueOf(df.format(ets + xmlTime)) + " seconds");
            System.out.println("GZIP Encoding Time: " + String.valueOf(df.format(ets)) + " seconds");
            
            fileSize = Files.size(Paths.get(gzPath));
            System.out.println("GZIP File Size: " + String.valueOf(fileSize) + " bytes\n");

            
            
            String binFilePath = System.getProperty("user.dir") + "\\files\\protoOutput_" + String.valueOf(numberOfTeachers) + "_" + String.valueOf(totalStudents) + ".bin";
            FileOutputStream fos = new FileOutputStream(binFilePath);
            
            start = System.nanoTime();
            allTeachers.writeTo(fos);
            finish = System.nanoTime();
            timeElapsed = finish - start;
            ets = (double) timeElapsed / 1_000_000_000;

            System.out.println("\n\tGOOGLE PROTOCOL BUFFERS INFO\n");

            System.out.println("File created at \\" + binFilePath);
            System.out.println("Binary File Elapsed Time: " + String.valueOf(df.format(ets)) + " seconds");
            fileSize = Files.size(Paths.get(binFilePath));
            System.out.println("Binary File Size: " + String.valueOf(fileSize) + " bytes\n");
            
            
            // unmarshalling

            File file = new File(System.getProperty("user.dir") + "\\files\\SchoolXML_" + String.valueOf(numberOfTeachers) + "_" + String.valueOf(totalStudents) + ".xml");    
            JAXBContext jaxbContext2 = JAXBContext.newInstance(Students.class);    
         
            Unmarshaller jaxbUnmarshaller = jaxbContext2.createUnmarshaller();    
            start = System.nanoTime();
            Students unmarshalledTeacher=(Students) jaxbUnmarshaller.unmarshal(file);
            finish = System.nanoTime();
            timeElapsed = finish - start;
            ets = (double) timeElapsed / 1_000_000_000;
            double xmlUnmarshall = ets;
            System.out.println("\n\tXML UNMARSHALL INFO\n");
            System.out.println("XML Unmarshalling Elapsed Time: " + String.valueOf(df.format(ets)) + " seconds");
            

           
            start = System.nanoTime();
            String ungzp = ungzip(System.getProperty("user.dir") + "\\files\\SchoolGZIP_" + String.valueOf(numberOfTeachers) + "_" + String.valueOf(totalStudents) + ".xml.gz", numberOfTeachers, totalStudents);
            finish = System.nanoTime();
            timeElapsed = finish - start;
            ets = (double) timeElapsed / 1_000_000_000;
            System.out.println("\n\tXML + GZIP UNMARSHALL INFO\n");
            System.out.println("XML + GZIP Elapsed Time: " + String.valueOf(df.format(xmlUnmarshall + ets)) + " seconds");
            System.out.println("GZIP Decoding Elapsed Time: " + String.valueOf(df.format(ets)) + " seconds");


            // decode the proto bitch
            FileInputStream fis = new FileInputStream(binFilePath);
            start = System.nanoTime();
            allTeachers.parseFrom(fis);
            finish = System.nanoTime();
            timeElapsed = finish - start;
            ets = (double) timeElapsed / 1_000_000_000;

            System.out.println("\n\tGOOGLE PROTOCOL BUFFERS DESERIALIZE INFO\n");

            System.out.println("ProtoBuff Deserialization Elapsed Time: " + String.valueOf(df.format(ets)) + " seconds");
            
            // output to console
            //jaxbMarshaller.marshal(teacherList, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    public static Teacher createTeacher(String[] firstName, String[] lastName, int id, Students s){
        String name = randomName(firstName, lastName);
        long phone = randomPhone();
        LocalDate bd = randomBirthDate(false);
        String addr = randomAddress();
        Teacher t = new Teacher(id, name, bd, phone, addr, s);
        return t;
    }

    public static School.Teacher createTeacherPB(int id, String name, long phone, LocalDate bd, String addr, School.Students sl){
        
        School.Teacher t1 = School.Teacher.newBuilder()
        .setId(id)
        .setName(name)
        .setPhone(phone)
        .setBirthDate(String.valueOf(bd))
        .setAddress(addr)
        .setStudents(sl)
        .build();

        return t1;

    }


    public static Student createStudent(String[] firstName, String[] lastName, int id, String professor){
        
        String gender = "M";
        Random g = new Random();
        int val = g.nextInt(2-0) + 0;
        if(val == 0){ gender = "F"; }

        String name = randomName(firstName, lastName);
        long phone = randomPhone();
        LocalDate bd = randomBirthDate(true);
        LocalDate rd = randomRegistrationDate();
        String addr = randomAddress();
        Student s = new Student(id, name, phone, gender, bd, rd, addr, professor);
        return s;

    }

    public static School.Student createStudentPB(int id, String name, long phone, String gender, LocalDate bd, LocalDate rd, String addr, String professor){
        
        School.Student s1 = School.Student.newBuilder()
        .setId(id)
        .setName(name)
        .setPhone(phone)
        .setGender(gender)
        .setBirthDate(String.valueOf(bd))
        .setRegistrationDate(String.valueOf(rd))
        .setAddress(addr)
        .setProfessor(professor)
        .build();

        return s1;

    }




    public static String randomName(String[] firstName, String[] lastName){
        int n1 = firstName.length;
        int n2 = lastName.length;
        
        Random r = new Random();
        int position1 = r.nextInt(n1-0) + 0;
        int position2 = r.nextInt(n2-0) + 0;

        String name = firstName[position1];
        String surname = lastName[position2];

        return name + " " + surname;
    }

    public static LocalDate randomBirthDate(Boolean isStudent){
        Random r = new Random();
        int year;
        int month = r.nextInt(13-1) + 1;
        int day = r.nextInt(29-1) + 1;

        if(isStudent){ year = r.nextInt(2002-1995) + 1995;}
        else{ year = r.nextInt(1985-1945) + 1945;}
        String y = String.valueOf(year);
        String m = "";
        String d = "";
        
        if(month > 9){ m = String.valueOf(month); }
        else{ m = "0" + String.valueOf(month); }

        if(day > 9){ d = String.valueOf(day); }
        else{ d = "0" + String.valueOf(day); }

        String date = y + "-" + m + "-" + d;

        return LocalDate.parse(date);
    }


    public static LocalDate randomRegistrationDate(){
        Random r = new Random();
        
        int year = r.nextInt(2022-2015) + 2015;
        int month = r.nextInt(13-1) + 1;
        int day = r.nextInt(29-1) + 1;

        String y = String.valueOf(year);
        String m = "";
        String d = "";
        
        if(month > 9){ m = String.valueOf(month); }
        else{ m = "0" + String.valueOf(month); }

        if(day > 9){ d = String.valueOf(day); }
        else{ d = "0" + String.valueOf(day); }
        String date = y + "-" + m + "-" + d;

        return LocalDate.parse(date);
    }

    public static long randomPhone(){
        Random r = new Random();
        return r.nextInt(969999999 - 900000000) + 900000000;
    }

    public static String randomAddress(){

        String[] f = new String[] {"Winston", "Trafalgar", "Love", "St.Moon", "Pickadilly", "West", "South Gate", "Liverpool", "Ramsdale", "O'Dylan", "Mayweather", "Vinland", "Hightower", "Rock-a-feller", "BraveBird", "Queen's"};
        String[] l = new String[] {"Street", "Avenue", "Boulevard", "Square"};

        Random r = new Random();
        int door = r.nextInt(700-1) + 1;

        int p1 = r.nextInt(f.length - 0) + 0;
        int p2 = r.nextInt(l.length - 0) + 0;
        

        return f[p1] + " " + l[p2] + ", " + door;
    }


    public static String gzip(String xml, int nt, int ns) {
        try {
            String outputFile = System.getProperty("user.dir") + "\\files\\SchoolGZIP_" + String.valueOf(nt) + "_" + String.valueOf(ns) + ".xml.gz";
            FileInputStream fis = new FileInputStream(xml);
            FileOutputStream fos = new FileOutputStream(outputFile);
            GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
            byte[] buffer = new byte[1024];
            int len;
            while((len=fis.read(buffer)) != -1){
                gzipOS.write(buffer, 0, len);
            }
            //close resources
            gzipOS.close();
            fos.close();
            fis.close();
            return outputFile;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "error";   
    }

    public static String ungzip(String gzipFile, int nt, int ns) {
        try {

            byte[] buffer = new byte[1024];

            FileInputStream fileIn = new FileInputStream(gzipFile);
            GZIPInputStream gZIPInputStream = new GZIPInputStream(fileIn);

            String outputFile = System.getProperty("user.dir") + "\\files\\SchoolUNGZIP_" + String.valueOf(nt) + "_" + String.valueOf(ns) + ".xml";
            
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
 
            int bytes_read;
            while ((bytes_read = gZIPInputStream.read(buffer)) > 0) {
 
                fileOutputStream.write(buffer, 0, bytes_read);
            }
 
            gZIPInputStream.close();
            fileOutputStream.close();

            return outputFile;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "error";   
    }


}