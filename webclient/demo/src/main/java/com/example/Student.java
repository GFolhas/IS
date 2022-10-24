package com.example;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("student")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    
    @Id
    private int id;
    @Column
    private String name;
    @Column
    private String birthdate;
    @Column
    private int credits;
    @Column
    private float grade;
}
