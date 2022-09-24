package uc.mei.is;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.ArrayList;

import java.io.File;

public class App {

    public static void main(String[] args) {

        JAXBContext jaxbContext = null;

        try {
        
            jaxbContext = org.eclipse.persistence.jaxb.JAXBContextFactory
                    .createContext(new Class[]{Students.class}, null);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Students studentList = new Students();
            //studentList.setStudent(new ArrayList<Student>());

            Student obj = new Student("Folhas", 2019214765);
            Student obj2 = new Student("Vaz", 2019281159);
            Student obj3 = new Student("B", 201817823);

            //Add the employees in list
            studentList.getStudent().add(obj);
            studentList.getStudent().add(obj2);
            studentList.getStudent().add(obj3);

            // output to a xml file
            jaxbMarshaller.marshal(studentList, new File("D:\\UNI\\Master's\\Year 1\\1st Semester\\IS\\Students.xml"));
            
            // output to console
            jaxbMarshaller.marshal(studentList, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

}