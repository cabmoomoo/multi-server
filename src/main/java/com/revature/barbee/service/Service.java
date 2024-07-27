package com.revature.barbee.service;

import java.sql.SQLException;

import com.revature.barbee.dao.Database;
import com.revature.barbee.dao.ExternalConnections;
import com.revature.barbee.model.MultiServerError;
import com.revature.barbee.model.data.Semester;
import com.revature.barbee.model.data.Student;
import com.revature.barbee.utils.ExampleData;

public class Service {

    private final Semester semseter;
    private final Database database;
    private final ExternalConnections externalConnections;

    public Service() throws MultiServerError {
        try {
            System.out.println("    Connecting to local database");
            this.database = new Database();
            this.externalConnections = new ExternalConnections();
            // TODO: Using temp data
            // Semester constructor should be provided with the results of externalConnections methods
            System.out.println("    Connecting to external databases");
            this.semseter = new Semester(
                ExampleData.students, // this.externalConnections.getAllStudents(),
                ExampleData.professors, 
                ExampleData.courses,
                database.selectAllStudents_Courses(),
                database.selectAllCourses_Professors()
            );
        } catch (SQLException ex) {
            throw new MultiServerError(String.format("Failed to create Service class:%n") + ex.getMessage());
        }
        
    }
    
    public String viewCoursesProfessorStudents() {
        String result = "";

        // Starting HTML boilerplate
        result += 
            """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="utf-8">
                <title>""";
        result += "Semester Schedule"; // Page title
        result += 
            """
            </title>
            <style>
                table, th, td {border: 1px solid black;}
            </style>
            </head>
            <body>
            """;
        // Build a table
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
        for (int courseID : this.semseter.courses.keySet()) {
            table += 
            """
                    <tr>
                        <td>%s</td>
                        <td>%s</td>
                        <td>
            """
                .formatted(
                    this.semseter.courses.get(courseID).name, 
                    this.semseter.professors.get(this.semseter.index_course_professor.get(courseID)).name
                );
            for (int studentID : this.semseter.courses_students.get(courseID)) {
                Student student = this.semseter.students.get(studentID);
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
        result += table;
        
        // Ending HTML boilerplate
        result += 
            """
            </body>
            </html>
            """;

        return result;
    }
    
}
