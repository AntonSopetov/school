package ru.hogwarts.school.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import java.util.Collection;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        facultyRepository.deleteAll();
        baseUrl = "http://localhost:" + port + "/faculty";
    }

    @Test
    public void testCreateFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColour("Red");
        ResponseEntity<Faculty> response = restTemplate.postForEntity(baseUrl, faculty, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void testReadFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Slytherin");
        faculty.setColour("Green");
        Faculty saved = facultyRepository.save(faculty);
        ResponseEntity<Faculty> response = restTemplate.getForEntity(baseUrl + "/" + saved.getId(), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Slytherin");
    }

    @Test
    public void testUpdateFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Hufflepuff");
        faculty.setColour("Yellow");
        Faculty saved = facultyRepository.save(faculty);
        saved.setColour("Gold");
        restTemplate.put(baseUrl, saved);
        ResponseEntity<Faculty> response = restTemplate.getForEntity(baseUrl + "/" + saved.getId(), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getColour()).isEqualTo("Gold");
    }

    @Test
    public void testDeleteFaculty() {
        Faculty faculty = new Faculty();
        faculty.setName("Ravenclaw");
        faculty.setColour("Blue");
        Faculty saved = facultyRepository.save(faculty);
        restTemplate.delete(baseUrl + "/" + saved.getId());
        ResponseEntity<Faculty> response = restTemplate.getForEntity(baseUrl + "/" + saved.getId(), Faculty.class);
        assertThat(response.getBody()).isNull();
    }

    @Test
    public void testFindByNameOrColour() {
        Faculty faculty = new Faculty();
        faculty.setName("Gryffindor");
        faculty.setColour("Red");
        facultyRepository.save(faculty);
        ResponseEntity<Collection> response = restTemplate.getForEntity(baseUrl + "/find?name=Gryffindor", Collection.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }
}
