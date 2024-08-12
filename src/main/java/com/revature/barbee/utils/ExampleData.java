package com.revature.barbee.utils;

import java.util.ArrayList;
import java.util.List;

import com.revature.barbee.model.data.Course;
import com.revature.barbee.model.data.Professor;
import com.revature.barbee.model.data.Student;

public class ExampleData {
    public static final List<Student> students = new ArrayList<>() {{
        add(new Student(1, "Caleb", 22));
        add(new Student(2, "Andrew", 22));
        add(new Student(3, "Janet", 22));
        add(new Student(4, "Runo", 22));
        add(new Student(5, "Jimmy", 24));
        add(new Student(6, "James", 23));
        add(new Student(7, "June", 21));
    }};

    public static final List<Professor> professors = new ArrayList<>() {{
        add(new Professor(1, "Mehrab"));
        add(new Professor(2, "Frankenstein"));
        add(new Professor(3, "Doofenshmirtz"));
    }};

    public static final List<Course> courses = new ArrayList<>() {{
        add(new Course(1, "MuleSoft Development"));
        add(new Course(2, "Advanced Biology"));
        add(new Course(3, "The Venerable Self Destruct Button"));
        add(new Course(4, "Java Programming II"));
    }};
}
