package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry");
        student.setAge(11);

        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void testReadStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Ron");
        student.setAge(11);

        when(studentService.readStudent(anyLong())).thenReturn(student);

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ron"));
    }

    @Test
    public void testReadStudentNotFound() throws Exception {
        when(studentService.readStudent(999L)).thenReturn(null);

        mockMvc.perform(get("/student/999"));
    }

    @Test
    public void testUpdateStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Hermione");
        student.setAge(11);

        when(studentService.updateStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Hermione"));
    }

    @Test
    public void testFilterByAge() throws Exception {
        Student student = new Student();
        student.setName("Draco");
        student.setAge(12);

        when(studentService.filterByAge(12)).thenReturn(Collections.singletonList(student));

        mockMvc.perform(get("/student/age?age=12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Draco"));
    }
}