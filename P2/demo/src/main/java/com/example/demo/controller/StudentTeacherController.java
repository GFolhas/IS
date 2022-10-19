package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.StudentTeacher;
import com.example.demo.repo.StudentTeacherRepo;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/relationship")
public class StudentTeacherController {
    
    @Autowired
    StudentTeacherRepo repo;


    @PostMapping
    public Mono<StudentTeacher> createRelationship(@RequestBody StudentTeacher studentteacher){
        return repo.save(studentteacher);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteRelationship(@PathVariable Integer id){
        return repo.deleteById(id);
    }


}
