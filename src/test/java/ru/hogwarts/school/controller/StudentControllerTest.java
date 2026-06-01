package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import java.util.Collection;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        studentRepository.deleteAll();
        baseUrl = "http://localhost:" + port + "/student";
    }

    @Test
    public void testCreateStudent() {
        Student student = new Student();
        student.setName("Harry");
        student.setAge(11);
        ResponseEntity<Long> response = restTemplate.postForEntity(baseUrl, student, Long.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void testReadStudent() {
        Student student = new Student();
        student.setName("Ron");
        student.setAge(11);
        Student saved = studentRepository.save(student);
        ResponseEntity<Student> response = restTemplate.getForEntity(baseUrl + "/" + saved.getId(), Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Ron");
    }

    @Test
    public void testReadStudentNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testUpdateStudent() {
        Student student = new Student();
        student.setName("Hermione");
        student.setAge(11);
        Student saved = studentRepository.save(student);
        saved.setName("Hermione Granger");
        restTemplate.put(baseUrl, saved);
        ResponseEntity<Student> response = restTemplate.getForEntity(baseUrl + "/" + saved.getId(), Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Hermione Granger");
    }

    @Test
    public void testFilterByAge() {
        Student student = new Student();
        student.setName("Draco");
        student.setAge(12);
        studentRepository.save(student);
        ResponseEntity<Collection> response = restTemplate.getForEntity(baseUrl + "/age?age=12", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }
}
