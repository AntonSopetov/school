package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student readStudent(long id) {
        logger.info("Was invoked method for read student");
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is no student with id = " + id);
                    return new IllegalArgumentException("Student not found");
                });
    }

    public Student updateStudent(Student student) {
        logger.info("Was invoked method for update student");
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student");
        studentRepository.deleteById(id);
    }

    public Collection<Student> filterByAge(int age) {
        logger.info("Was invoked method for filter students by age");
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked method for find students by age between");
        return studentRepository.findByAgeBetween(min, max);
    }

    public Integer getCountOfAllStudents() {
        logger.info("Was invoked method for get count of all students");
        return studentRepository.getCountOfAllStudents();
    }

    public Double getAverageAge() {
        logger.info("Was invoked method for get average age of students");
        return studentRepository.getAverageAge();
    }

    public Collection<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");
        return studentRepository.getLastFiveStudents();
    }

    public List<String> getAllStudentsStartingWithA() {
        logger.info("Was invoked method for get all students starting with A");
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(name -> name.startsWith("A"))
                .sorted()
                .collect(Collectors.toList());
    }

    public double getAverageAgeWithStreams() {
        logger.info("Was invoked method for get average age with streams");
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
    }

    public void printStudentsParallel() {
        logger.info("Was invoked method for print students parallel");
        List<Student> students = studentRepository.findAll();

        printName(students, 0);
        printName(students, 1);

        new Thread(() -> {
            printName(students, 2);
            printName(students, 3);
        }).start();

        new Thread(() -> {
            printName(students, 4);
            printName(students, 5);
        }).start();
    }

    public void printStudentsSynchronized() {
        logger.info("Was invoked method for print students synchronized");
        List<Student> students = studentRepository.findAll();

        printNameSynchronized(students, 0);
        printNameSynchronized(students, 1);

        new Thread(() -> {
            printNameSynchronized(students, 2);
            printNameSynchronized(students, 3);
        }).start();

        new Thread(() -> {
            printNameSynchronized(students, 4);
            printNameSynchronized(students, 5);
        }).start();
    }

    private void printName(List<Student> students, int index) {
        if (index < students.size()) {
            System.out.println(students.get(index).getName());
        } else {
            System.out.println("No student at index " + index);
        }
    }

    private synchronized void printNameSynchronized(List<Student> students, int index) {
        if (index < students.size()) {
            System.out.println(Thread.currentThread().getName() + ": " + students.get(index).getName());
        } else {
            System.out.println(Thread.currentThread().getName() + ": No student at index " + index);
        }
    }
}
