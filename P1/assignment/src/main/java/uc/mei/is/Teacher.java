package uc.mei.is;

import java.time.LocalDate;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
// order of the fields in XML
// @XmlType(propOrder = {"price", "name"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Teacher {

    public Teacher(){}

    public Teacher(int id, String name, LocalDate birthDate,long phone, String address, Students students){
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
        this.address = address;
        this.students = students;
    }

    
    //@XmlElement(name = "name")
    @XmlElement(name = "name")
    String name;
    @XmlElement(name = "birthdate")
    LocalDate birthDate;
    @XmlElement(name = "phone")
    long phone;
    @XmlElement(name = "address")
    String address;
    @XmlElement(name = "students")
    Students students;
    
    @XmlAttribute()
    int id;
    
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public long getPhone() {
        return this.phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Students getStudents() {
        return this.students;
    }

    /* public void addStudents(Student student){
        this.students.add(student);
    } */

    public void setStudents(Students students) {
        this.students = students;
    }

}