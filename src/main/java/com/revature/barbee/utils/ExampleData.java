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
    }};

    public static final List<Professor> professors = new ArrayList<>() {{
        add(new Professor(1, "Mehrab"));
        add(new Professor(2, "Frankenstein"));
    }};

    public static final List<Course> courses = new ArrayList<>() {{
        add(new Course(1, "Advanced Biology"));
        add(new Course(2, "MuleSoft Development"));
    }};
}
