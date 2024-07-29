package com.revature.barbee.service;

import java.sql.SQLException;
import java.util.List;

import com.revature.barbee.dao.Database;
import com.revature.barbee.dao.ExternalConnections;
import com.revature.barbee.model.MultiServerError;
import com.revature.barbee.model.data.Course;
import com.revature.barbee.model.data.Professor;
import com.revature.barbee.model.data.Semester;
import com.revature.barbee.model.data.Student;
import com.revature.barbee.utils.ExampleData;

public class Service {

    private final Semester semester;
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
            List<Student> students;
            try {
                students = this.externalConnections.getAllStudents();
                System.out.println("        Students successful");
            } catch (MultiServerError e) {
                students = ExampleData.students;
                System.out.println(e.getMessage());
                System.out.println("        Init failure... falling back to ExampleData.students");
            }
            List<Professor> professors;
            try {
                professors = this.externalConnections.getAllProfessors();
                System.out.println("        Professors successful");
            } catch (MultiServerError e) {
                professors = ExampleData.professors;
                System.out.println(e.getMessage());
                System.out.println("        Init failure... falling back to ExampleData.professors");
            }
            List<Course> courses;
            try {
                courses = this.externalConnections.getAllCourses();
                System.out.println("        Courses successful");
            } catch (MultiServerError e) {
                courses = ExampleData.courses;
                System.out.println(e.getMessage());
                System.out.println("        Init failure... falling back to ExampleData.courses");
            }
            this.semester = Semester.get_instance().init(
                students,
                professors,
                courses,
                database.selectAllStudents_Courses(),
                database.selectAllCourses_Professors()
            );
        } catch (SQLException ex) {
            throw new MultiServerError(String.format("Failed to create Service class:%n") + ex.getMessage());
        }
        
    }
    
}
