package uc.mei.is;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "class")
// order of the fields in XML
// @XmlType(propOrder = {"price", "name"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Students {

    @XmlElement(name = "student")
    ArrayList<Student> list = new ArrayList<>();
   
    public ArrayList<Student> getStudent() {
      return list;
    }
   
    public void setStudent(ArrayList<Student> list) {
      this.list = list;
    }

}