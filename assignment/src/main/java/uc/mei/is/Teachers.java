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
public class Teachers {

    @XmlElement(name = "teacher")
    ArrayList<Teacher> list = new ArrayList<>();
   
    public ArrayList<Teacher> getTeacher() {
      return list;
    }
   
    public void setTeacher(ArrayList<Teacher> list) {
      this.list = list;
    }

}