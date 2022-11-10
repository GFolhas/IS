package com.example;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Table("student_teacher")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentTeacher {
    
    @Id
    private int student_id;
    @Column
    private int teacher_id;

}
