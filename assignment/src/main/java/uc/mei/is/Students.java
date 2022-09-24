package uc.mei.is;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "class")
@XmlAccessorType(XmlAccessType.FIELD)
public class Students {

    @XmlElement(name = "student")
    // List<String> students;
    ArrayList<Student> students;

    public Students() { }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student){
      this.students.add(student);
    }

}