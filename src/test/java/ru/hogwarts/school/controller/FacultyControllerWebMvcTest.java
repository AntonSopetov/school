package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColour("Red");

        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor"));
    }

    @Test
    public void testReadFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Slytherin");
        faculty.setColour("Green");

        when(facultyService.readFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Slytherin"));
    }

    @Test
    public void testUpdateFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Hufflepuff");
        faculty.setColour("Yellow");

        when(facultyService.updateFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.colour").value("Yellow"));
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFindByNameOrColour() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("Ravenclaw");
        faculty.setColour("Blue");

        when(facultyService.findByNameOrColour("Ravenclaw", null))
                .thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculty/find?name=Ravenclaw"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ravenclaw"));
    }
}
