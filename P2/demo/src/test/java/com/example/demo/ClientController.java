package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.example.demo.model.Student;
import com.example.demo.model.Teacher;
import com.example.demo.repo.StudentRepo;
import com.example.demo.repo.TeacherRepo;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@SpringBootTest
@AutoConfigureWebTestClient
@Slf4j
@ActiveProfiles(profiles = "client")
public class ClientController {

  @Autowired
  private WebTestClient webTestClient;

  @Autowired
  private TeacherRepo teacherRepo;

  @Autowired
  private StudentRepo studentRepo;

  @Value("${spring.r2dbc.url}")
  private String dbUrl;

  @BeforeEach
  public void setup() {
    initializeDatabase();
    insertTeacherData();
    insertStudentData();
  }

  private void initializeDatabase() {
    ConnectionFactory connectionFactory = ConnectionFactories.get(dbUrl);
    R2dbcEntityTemplate template = new R2dbcEntityTemplate(connectionFactory);
    String queryT = "CREATE TABLE IF NOT EXISTS teacher (id SERIAL PRIMARY KEY, name TEXT NOT NULL);";
    String queryS = "CREATE TABLE IF NOT EXISTS student (id SERIAL PRIMARY KEY, name TEXT NOT NULL);"; // change this
    
    template.getDatabaseClient().sql(queryT).fetch().rowsUpdated().block();
    //template.getDatabaseClient().sql(queryS).fetch().rowsUpdated().block();
  }

  private void insertTeacherData() {
    Teacher t1 = new Teacher(1, "João Vaz");
    Teacher t2 = new Teacher(2, "Gonçalo Folhas");
    Teacher t3 = new Teacher(3, "Rodrigo Machado");
    Teacher t4 = new Teacher(4, "Francisco Carreira");
    Flux<Teacher> teacherFlux = Flux.just(
        t1, t2, t3, t4
    );
    teacherRepo.deleteAll()
        .thenMany(teacherFlux)
        .flatMap(teacherRepo::save)
        .doOnNext(t -> log.info("inserted {}", t))
        .blockLast();
  }


  private void insertStudentData() {

    Student s1 = new Student(1, "Carlos Jordão", "12-04-2000", 156, 13);
    Student s2 = new Student(2, "Guilherme Junqueira", "17-10-2009", 150, 12);
    Student s3 = new Student(3, "David Leitão", "28-08-2001", 150, 12);
    Student s4 = new Student(4, "Rui Bernardo", "02-01-2001", 156, 13);

    Flux<Student> studentFlux = Flux.just(
        s1, s2, s3, s4
    );
    studentRepo.deleteAll()
        .thenMany(studentFlux)
        .flatMap(studentRepo::save)
        .doOnNext(s -> log.info("inserted {}", s))
        .blockLast();

  }



  //TODO: change both @test because it still isnt done


  @Test
  public void getAllTeachers() {
    webTestClient.get()
        .uri("/teacher")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$")
        .isArray()
        .jsonPath("$[0].name")
        .isEqualTo("João Vaz")
        .jsonPath("$[1].name")
        .isEqualTo("Gonçalo Folhas")
        .jsonPath("$[2].name")
        .isEqualTo("Rodrigo Machado")
        .jsonPath("$[3].name")
        .isEqualTo("Francisco Carreira");
  }

  @Test
  public void getOneTeacher() {
    webTestClient.get()
        .uri("/teacher/1")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.name")
        .isEqualTo("João Vaz")
        .jsonPath("$.id")
        .isNumber();
  }


  @Test
  public void getAllStudents() {
    webTestClient.get()
        .uri("/student")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$")
        .isArray()
        .jsonPath("$[0].name")
        .isEqualTo("Carlos Jordão")
        .jsonPath("$[1].name")
        .isEqualTo("Guilherme Junqueira")
        .jsonPath("$[2].name")
        .isEqualTo("David Leitão")
        .jsonPath("$[3].name")
        .isEqualTo("Rui Bernardo");
  }

  @Test
  public void getOneStudent() {
    webTestClient.get()
        .uri("/student/1")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody()
        .jsonPath("$.name")
        .isEqualTo("Carlos Jordão")
        .jsonPath("$.id")
        .isNumber();
  }

}
