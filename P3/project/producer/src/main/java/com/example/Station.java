package com.example;

public class Station {
    private int id;
    private String name;
    private String location;

    public Station(int id, String name, String location){
        this.id = id;
        this.name = name;
        this.location = location;
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

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    
}
