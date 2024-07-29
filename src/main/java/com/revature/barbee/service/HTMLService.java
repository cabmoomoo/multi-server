package com.revature.barbee.service;

import java.util.List;

import com.revature.barbee.model.data.Course;
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
        String table = "";
        table +=
            """
                <table>
                    <tr>
                        <th>Course</th>
                        <th>Professor</th>
                        <th>Enrolled Students</th>
                    </tr>
            """;
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
            table +=
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
                );
        }
        table +=
            """
                </table>
            """;

        return HTMLConstructor.boilerplateHTML("Courses", this.genericTableStyle, table);
    }

    public String viewCoursesProfessorStudents() {
        String table = "";
        table += 
            """
                <table>
                    <tr>
                        <th>Course</th>
                        <th>Professor</th>
                        <th>Students</th>
                    </tr>
            """;
        for (int courseID : this.semester.index_course_professor.keySet()) {
            table += 
            """
                    <tr>
                        <td>%s</td>
                        <td>%s</td>
                        <td>
            """
                .formatted(
                    this.semester.courses.get(courseID).name, 
                    this.semester.professors.get(this.semester.index_course_professor.get(courseID)).name
                );
            for (int studentID : this.semester.courses_students.get(courseID)) {
                Student student = this.semester.students.get(studentID);
                table += 
            """
                            %s<br>
            """.formatted(student.name);
            }
            table +=
            """
                        </td>
                    </tr>
            """;
        }
        table += 
            """
                </table>
            """;

        return HTMLConstructor.boilerplateHTML("Semester Schedule", this.genericTableStyle, table);
    }
    
}
