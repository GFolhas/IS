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

import com.example.demo.model.Teacher;
import com.example.demo.repo.TeacherRepo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value="/teacher")
public class TeacherController {
    
    @Autowired
    TeacherRepo repo;


    @GetMapping
    public Flux<Teacher> getTeachers(){
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Teacher> getTeacherByID(@PathVariable Integer id){
        return repo.findById(id);
    }

    
    @PostMapping
    public Mono<Teacher> createTeacher(@RequestBody Teacher teacher){
        return repo.save(teacher);
    }
    

    @PutMapping("/{id}")
    public Mono<Teacher> updateTeacher(@RequestBody Teacher teacher, @PathVariable Integer id){
        return repo.findById(id)
                    .map(t -> {
                        t.setName(teacher.getName());
                        return t;
                    }).flatMap(t -> repo.save(t));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteTeacher(@PathVariable Integer id){
        return repo.deleteById(id);
    }

}

