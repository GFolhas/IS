package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Student;
import com.example.demo.repo.StudentRepo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value="/student")
public class StudentController {
    
    @Autowired
    StudentRepo repo;


    @GetMapping
    public Flux<Student> getStudents(){
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Student> getStudentByID(@PathVariable Integer id){
        return repo.findById(id);
    }
    
    @PostMapping
    public Mono<Student> createStudent(@RequestBody Student student){
        return repo.save(student);
    }

    @PutMapping("/{id}")
    public Mono<Student> updateStudent(@RequestBody Student student, @PathVariable Integer id){
        return repo.findById(id)
                    .map(s -> {
                        s.setName(student.getName());
                        s.setBirthdate(student.getBirthdate());
                        s.setCredits(student.getCredits());
                        s.setGrade(student.getGrade());
                        return s;
                    }).flatMap(t -> repo.save(t));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteStudent(@PathVariable Integer id){
        return repo.deleteById(id);
    }

}
