package com.revature.barbee.model.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Semester {
    public Map<Integer, Course> courses = new HashMap<>();
    public Map<Integer, Professor> professors = new HashMap<>();
    public Map<Integer, Student> students = new HashMap<>();
    public Map<Integer, List<Integer>> courses_students = new HashMap<>();
    public Map<Integer, List<Integer>> professors_courses = new HashMap<>();

    public Map<Integer, List<Integer>> index_students_courses = new HashMap<>();
    public Map<Integer, Integer> index_course_professor = new HashMap<>();

    private static Semester semester_instance = null;
    private Semester() {}
    public static Semester get_instance() {
        if (semester_instance == null) {
            semester_instance = new Semester();
        }
        return semester_instance;
    }

    public Semester init(
        List<Student> students, 
        List<Professor> professors, 
        List<Course> courses,
        List<Entry<Integer,Integer>> courses_students,
        List<Entry<Integer,Integer>> professors_courses
    ) {
        System.out.println("    Initializing semester");
        for (Student student : students) {
            this.students.put(student.id, student);
        }
        for (Professor professor : professors) {
            this.professors.put(professor.id, professor);
        }
        for (Course course : courses) {
            this.courses.put(course.id, course);
        }
        this.buildCourses_StudentsMap(courses_students);
        this.buildProfessors_CoursesMap(professors_courses);
        this.indexStudentsToCourses();
        this.indexCoursesToProfessors();
        return this;
    }

    public final void buildCourses_StudentsMap(List<Entry<Integer,Integer>> student_course) {
        // student_course can be considered a list of tuples, which looks like this:
        // (student_id, course_id)
        for (Entry<Integer, Integer> tuple : student_course) {
            int studentID = tuple.getKey();
            int courseID = tuple.getValue();
            if (this.courses_students.containsKey(courseID)) {
                this.courses_students.get(courseID).add(studentID);
            } else {
                this.courses_students.put(courseID, new ArrayList<>() {{ add(studentID); }} );
            }
        }
    }

    public final void buildProfessors_CoursesMap(List<Entry<Integer,Integer>> course_professor) {
        // (course_id, professor_id)
        for (Entry<Integer,Integer> tuple : course_professor) {
            int courseID = tuple.getKey();
            int professorID = tuple.getValue();
            if (this.professors_courses.containsKey(professorID)) {
                this.professors_courses.get(professorID).add(courseID);
            } else {
                this.professors_courses.put(professorID, new ArrayList<>() {{ add(courseID); }} );
            }
        }
    }

    public final void indexStudentsToCourses() {
        Set<Integer> courseIDSet = this.courses_students.keySet();
        for (int courseID : courseIDSet) {
            List<Integer> studentIDList = this.courses_students.get(courseID);
            for (int studentID : studentIDList) {
                if (this.index_students_courses.containsKey(studentID)) {
                    this.index_students_courses.get(studentID).add(courseID);
                } else {
                    this.index_students_courses.put(studentID, new ArrayList<>() {{ add(courseID); }} );
                }
            }
        }
    }

    public final void indexCoursesToProfessors() {
        Set<Integer> professorIDSet = this.professors_courses.keySet();
        for (int professorID : professorIDSet) {
            List<Integer> courseIDList = this.professors_courses.get(professorID);
            for (int courseID : courseIDList) {
                this.index_course_professor.put(courseID, professorID);
            }
        }
    }

}
