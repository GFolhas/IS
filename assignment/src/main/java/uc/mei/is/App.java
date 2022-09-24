package uc.mei.is;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.time.LocalDate;

public class App {

    public static void main(String[] args) {

        JAXBContext jaxbContext = null;

        try {
        
            jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
                    .createContext(new Class[]{Teachers.class}, null);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Random nt = new Random();
            int numberOfTeachers = nt.nextInt(40-1) + 1;
            Teachers teacherList = new Teachers();
            teacherList.setTeacher(new ArrayList<Teacher>());


            String fnames[] = new String[] {"Michael", "Christopher","Jessica","Matthew","Ashley","Jennifer","Joshua","Amanda","Daniel","David","James","Robert","John","Joseph","Andrew","Ryan","Brandon","Jason","Justin","Sarah","William","Jonathan","Stephanie","Brian","Nicole","Nicholas","Anthony","Heather","Eric",
                "Elizabeth","Adam","Megan","Melissa","Kevin","Steven","Thomas","Timothy","Christina","Kyle","Rachel","Laura","Lauren","Amber","Brittany","Danielle","Richard","Kimberly","Jeffrey","Amy","Crystal","Michelle","Tiffany","Jeremy","Benjamin","Mark","Emily","Aaron","Charles","Rebecca","Jacob","Stephen","Patrick"};

            String lnames[] = new String[] {"Hill","Puckett","Song","Hamilton","Bender","Wagner","McLaughlin","McNamara","Raynor","Moon","Woodard","Desai","Wallace","Lawrence","Griffin","Dougherty","Powers","May","Steele","Teague","Vick","Gallagher","Solomon","Walsh","Monroe","Connolly","Hawkins","Middleton","Goldstein",
                "Watts","Johnston","Weeks","Wilkerson","Barton","Walton","Hall","Ross","Woods","Mangum","Joseph","Rosenthal","Bowden","Underwood","Jones","Baker","Merritt","Cross","Cooper","Holmes","Sharpe","Morgan","Hoyle","Allen","Rich","Grant","Proctor","Diaz","Graham","Watkins","Hinton","Marsh","Hewitt","Branch","O'Brien",
                "Case","Christensen","Parks","Hardin","Lucas","Eason","Davidson","Whitehead","Rose"};



            for(int i = 0; i < numberOfTeachers; i++){
                Students sl = new Students();
                sl.setStudents(new ArrayList<Student>());
                Teacher t = createTeacher(fnames, lnames, i, sl);
                teacherList.getTeacher().add(t);

                Random stud = new Random();
                int numberOfStudents = stud.nextInt(15-0) + 0;
                
                for(int j = 0; j < numberOfStudents; j++){
                    Student s = createStudent(fnames, lnames, j, t.getName());
                    sl.addStudent(s);
                }
            }


            /* Students sl2 = new Students();
            Students sl3 = new Students(); */

           /*  sl2.setStudents(new ArrayList<Student>());
            sl3.setStudents(new ArrayList<Student>()); */

            
            /* Teacher t1 = new Teacher(1, "Ernesto Costa", LocalDate.parse("1958-05-10"), 910475233, "Pinhal de Marrocos", sl1);
            Teacher t2 = new Teacher(2, "Bernardette Ribeiro", LocalDate.parse("1954-12-25"), 913478991, "Bairro Norton de Matos", sl2);
            Teacher t3 = new Teacher(3, "Paulo de Carvalho", LocalDate.parse("1969-11-09"), 936212683, "Rua Fonte da Talha", sl3);
            
            Student s1 = new Student(1, "Goncalo Folhas", 925798577, "M", LocalDate.parse("2001-09-30"), LocalDate.parse("2022-09-01"), "Rua Vale da Estrada", t1.getName());
            Student s2 = new Student(2, "Joao Vaz", 910976234, "M", LocalDate.parse("2001-04-12"), LocalDate.parse("2022-09-01"), "Rua de Condeixa", t1.getName());
            
            Student s3 = new Student(3, "Leonor Paulo", 928765299, "F", LocalDate.parse("2000-12-18"), LocalDate.parse("2022-09-04"), "Vale da Murta", t2.getName());
            
            Student s4 = new Student(4, "Bianca Ramalho", 934208007, "F", LocalDate.parse("2002-06-02"), LocalDate.parse("2021-07-24"), "Rua Marco da Silva", t3.getName());
            Student s5 = new Student(5, "Francisco Carreira", 910772194, "M", LocalDate.parse("2001-02-28"), LocalDate.parse("2022-07-21"), "Rua do Brazil", t3.getName());
            Student s6 = new Student(6, "David Leitao", 910976234, "M", LocalDate.parse("2000-01-16"), LocalDate.parse("2021-07-20"), "Rua Aguas Livres", t3.getName());
             */




            // Adding Students to their respective classes
            /* sl1.addStudent(s1);
            sl1.addStudent(s2);

            sl2.addStudent(s3);

            sl3.addStudent(s4);
            sl3.addStudent(s5);
            sl3.addStudent(s6);

            t1.setStudents(sl1);
            t2.setStudents(sl2);
            t3.setStudents(sl3); */


            // Setting teachers classes
            /* teacherList.getTeacher().add(t1);
            teacherList.getTeacher().add(t2);
            teacherList.getTeacher().add(t3); */

            String path = System.getProperty("user.dir") + "\\AutoSchool.xml";

            // output to a xml file
            System.out.println("File created at \\" + path);
            jaxbMarshaller.marshal(teacherList, new File(path));
            
            // output to console
            jaxbMarshaller.marshal(teacherList, System.out);

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


}