package com.example;

import java.util.ArrayList;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.util.JSONPObject;

public class Station {
    @JsonProperty("name")
    private String name;
    @JsonProperty("location")
    private String location;
    @JsonProperty("payload")
    private JSONObject payload;
    @JsonProperty("schema")
    private JSONObject schema;
    
    public Station(){}
    
    public Station(String name, String location, JSONObject payload){
        this.name = name;
        this.location = location;
        this.payload = payload;
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

    public JSONObject getPayload() {
        return this.payload;
    }

    public void setPayload(JSONObject payload) {
        this.payload = payload;
    }
    

    public JSONObject getSchema() {
        return this.schema;
    }

    public void setSchema(JSONObject schema) {
        this.schema = schema;
    }
}
