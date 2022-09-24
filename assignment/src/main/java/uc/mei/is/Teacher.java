package uc.mei.is;

import java.util.ArrayList;
import java.util.Date;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlValue;

@XmlRootElement
// order of the fields in XML
// @XmlType(propOrder = {"price", "name"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Teacher {

    public Teacher(){}

    public Teacher(int id, String name, Date birthDate,long phone, String address, ArrayList<Students> students){
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
        this.address = address;
        this.students = students;
    }

    
    //@XmlElement(name = "name")
    @XmlValue
    String name;
    Date birthDate;
    long phone;
    String address;
    ArrayList<Students> students;

    
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
    
    public Date getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(Date birthDate) {
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

    public ArrayList<Students> getStudents() {
        return this.students;
    }

    public void setStudents(ArrayList<Students> students) {
        this.students = students;
    }

}