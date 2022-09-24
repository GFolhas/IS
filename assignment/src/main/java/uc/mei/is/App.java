package uc.mei.is;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.ArrayList;
import java.util.List;

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

            Teachers teacherList = new Teachers();
            teacherList.setTeacher(new ArrayList<Teacher>());

            Students sl1 = new Students();
            Students sl2 = new Students();
            Students sl3 = new Students();

            sl1.setStudents(new ArrayList<Student>());
            sl2.setStudents(new ArrayList<Student>());
            sl3.setStudents(new ArrayList<Student>());

            
            Teacher t1 = new Teacher(1, "Ernesto Costa", LocalDate.parse("1958-05-10"), 910475233, "Pinhal de Marrocos", sl1);
            Teacher t2 = new Teacher(2, "Bernardette Ribeiro", LocalDate.parse("1954-12-25"), 913478991, "Bairro Norton de Matos", sl2);
            Teacher t3 = new Teacher(3, "Paulo de Carvalho", LocalDate.parse("1969-11-09"), 936212683, "Rua Fonte da Talha", sl3);
            
            Student s1 = new Student(1, "Goncalo Folhas", 925798577, "M", LocalDate.parse("2001-09-30"), LocalDate.parse("2022-09-01"), "Rua Vale da Estrada", t1.getName());
            Student s2 = new Student(2, "Joao Vaz", 910976234, "M", LocalDate.parse("2001-04-12"), LocalDate.parse("2022-09-01"), "Rua de Condeixa", t1.getName());
            
            Student s3 = new Student(3, "Leonor Paulo", 928765299, "F", LocalDate.parse("2000-12-18"), LocalDate.parse("2022-09-04"), "Vale da Murta", t2.getName());
            
            Student s4 = new Student(4, "Bianca Ramalho", 934208007, "F", LocalDate.parse("2002-06-02"), LocalDate.parse("2021-07-24"), "Rua Marco da Silva", t3.getName());
            Student s5 = new Student(5, "Francisco Carreira", 910772194, "M", LocalDate.parse("2001-02-28"), LocalDate.parse("2022-07-21"), "Rua do Brazil", t3.getName());
            Student s6 = new Student(6, "David Leitao", 910976234, "M", LocalDate.parse("2000-01-16"), LocalDate.parse("2021-07-20"), "Rua Aguas Livres", t3.getName());
            




            // Adding Students to their respective classes
            sl1.addStudent(s1);
            sl1.addStudent(s2);

            sl2.addStudent(s3);

            sl3.addStudent(s4);
            sl3.addStudent(s5);
            sl3.addStudent(s6);

            t1.setStudents(sl1);
            t2.setStudents(sl2);
            t3.setStudents(sl3);


            // Setting teachers classes
            teacherList.getTeacher().add(t1);
            teacherList.getTeacher().add(t2);
            teacherList.getTeacher().add(t3);

            String path = System.getProperty("user.dir") + "\\School.xml";

            // output to a xml file
            System.out.println("File created at \\" + path);
            jaxbMarshaller.marshal(teacherList, new File(path));
            
            // output to console
            //jaxbMarshaller.marshal(teacherList, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

}