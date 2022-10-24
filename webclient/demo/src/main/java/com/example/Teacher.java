package com.example;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("teacher")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teacher {
    
    @Id
    private int id;
    @Column
    private String name;
}
