package com.example;

public class TeacherCount {
    String name;
    Long counter;

    
    public TeacherCount(String name, Long counter){
        this.name = name;
        this.counter = counter;
    }

    public TeacherCount(){}
    
    
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCounter() {
        return this.counter;
    }

    public void setCounter(Long counter) {
        this.counter = counter;
    }
    
}
