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
public class Student {

    public Student(){}

    public Student(int id, String name, long phone, String gender, LocalDate birthDate, LocalDate registrationDate, String address, String professor){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.address = address;
        this.professor = professor;
    }

    
    //@XmlValue
    @XmlElement(name = "name")
    String name; 
    @XmlElement(name = "phone")
    long phone;
    @XmlElement(name = "gender")
    String gender;
    @XmlElement(name = "birthDate")
    LocalDate birthDate;
    @XmlElement(name = "registrationDate")
    LocalDate registrationDate;
    @XmlElement(name = "address")
    String address;
    @XmlElement(name = "professor")
    String professor;


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

    public long getPhone() {
        return this.phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getRegistrationDate() {
        return this.registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfessor() {
        return this.professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }


}