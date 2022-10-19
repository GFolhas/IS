package com.example.demo.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.example.demo.model.StudentTeacher;

public interface StudentTeacherRepo extends ReactiveCrudRepository<StudentTeacher, Integer> {
    
}
