package com.example;

import reactor.core.publisher.Flux;

public class StudentFullInfo {
    private int id;
    private String name;
    private String birthdate;
    private int credits;
    private float grade;
    private Flux<String> profs;

    public StudentFullInfo(){}

    public StudentFullInfo(int id, String name, String birthdate, int credits, float grade,Flux<String> profs){
        this.id = id;
        this.name = name;
        this.birthdate = birthdate;
        this.credits = credits;
        this.grade = grade;
        this.profs = profs;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return this.birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public int getCredits() {
        return this.credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public float getGrade() {
        return this.grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public Flux<String> getProfs() {
        return this.profs;
    }

    public void setProfs(Flux<String> profs) {
        this.profs = profs;
    }


    
}
