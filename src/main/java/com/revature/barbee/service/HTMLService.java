package com.revature.barbee.service;

import java.util.ArrayList;
import java.util.List;

import com.revature.barbee.model.data.Course;
import com.revature.barbee.model.data.Professor;
import com.revature.barbee.model.data.Semester;
import com.revature.barbee.model.data.Student;
import com.revature.barbee.utils.HTMLConstructor;

/*
 * This class gets the lovely, extremely tall job of generating HTML
 * in response to user queries
 */
public class HTMLService {
    private final Semester semester;

    private final String genericTableStyle = 
    """
        table, th, td {border: 1px solid black;}
    """;

    public HTMLService() {
        this.semester = Semester.get_instance();
    }
    
    public String viewCourses() {
        StringBuilder table = new StringBuilder();
        table.append(
            """
                <table>
                    <tr>
                        <th>Course</th>
                        <th>Professor</th>
                        <th>Enrolled Students</th>
                    </tr>
            """);
        for (Course course : this.semester.courses.values()) {
            Integer professorID = this.semester.index_course_professor.get(course.id);
            String professorName;
            if (professorID == null) {
                professorName = "No professor";
            } else {
                professorName = this.semester.professors.get(professorID).name;
            }
            List<Integer> studentIDs = this.semester.courses_students.get(course.id);
            int studentCount;
            if (studentIDs == null) {
                studentCount = 0;
            } else {
                studentCount = studentIDs.size();
            }
            table.append(
            """
                    <tr>
                        <td>%s</td>
                        <td>%s</td>
                        <td>%d</td>
                    </tr>
            """.formatted(
                    course.name,
                    professorName,
                    studentCount
                ));
        }
        table.append(
            """
                </table>
            """);

        return HTMLConstructor.boilerplateHTML("Courses", this.genericTableStyle, table).toString();
    }

    public String viewCoursesProfessorStudents() {
        StringBuilder table = new StringBuilder();
        table.append( 
            """
                <table>
                    <tr>
                        <th>Course</th>
                        <th>Professor</th>
                        <th>Students</th>
                    </tr>
            """);
        for (int courseID : this.semester.index_course_professor.keySet()) {
            table.append( 
            """
                    <tr>
                        <td>%s</td>
                        <td>%s</td>
                        <td>
            """
                .formatted(
                    this.semester.courses.get(courseID).name, 
                    this.semester.professors.get(this.semester.index_course_professor.get(courseID)).name
                ));
            for (int studentID : this.semester.courses_students.get(courseID)) {
                Student student = this.semester.students.get(studentID);
                table.append( 
            """
                            %s<br>
            """.formatted(student.name));
            }
            table.append( 
            """
                        </td>
                    </tr>
            """);
        }
        table.append( 
            """
                </table>
            """);

        return HTMLConstructor.boilerplateHTML("Semester Schedule", this.genericTableStyle, table).toString();
    }

    public String viewProfessorsCourses() {
        StringBuilder table = new StringBuilder();
        table.append(
            """
                <table>
                    <tr>
                        <th>Professor</th>
                        <th>Courses</th>
                    </tr>
            """
        );
        for (Professor professor : this.semester.professors.values()) {
            List<String> courseNameList = new ArrayList<>();
            List<Integer> courseIDs = this.semester.professors_courses.get(professor.id);
            if (courseIDs == null) {
                courseNameList.add("None");
            } else {
                for (Integer courseID : this.semester.professors_courses.get(professor.id)) {
                    Course course = this.semester.courses.get(courseID);
                    if (course == null) {
                        continue;
                    }
                    courseNameList.add(course.name);
                }
            }
            StringBuilder courseNames = new StringBuilder();
            for (String courseName : courseNameList) {
                courseNames.append("%s<br>".formatted(courseName));
            }
            table.append(
            """
                    <tr>
                        <td>%s</td>
                        <td>%s</td>
                    </tr>
            """.formatted(professor.name, courseNames)
            );
        }
        table.append( 
            """
                </table>
            """);
        return HTMLConstructor.boilerplateHTML("Professors", this.genericTableStyle, table).toString();
    }
    
}
