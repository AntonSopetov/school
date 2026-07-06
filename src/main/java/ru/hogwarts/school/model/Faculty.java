package ru.hogwarts.school.model;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String colour;

    @OneToMany(mappedBy = "faculty")
    private Collection<Student> students;

    public Faculty() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getColour() { return colour; }
    public void setColour(String colour) { this.colour = colour; }
    public Collection<Student> getStudents() { return students; }
}