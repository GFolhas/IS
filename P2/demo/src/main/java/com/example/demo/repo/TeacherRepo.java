package com.example.demo.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.example.demo.model.Teacher;

public interface TeacherRepo extends ReactiveCrudRepository<Teacher, Integer> {
    
}
