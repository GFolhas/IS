package com.example.demo.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.example.demo.model.Student;

public interface StudentRepo extends ReactiveCrudRepository<Student, Integer> {
    
}
