package com.revature.barbee.service;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.revature.barbee.ThreadHandler;
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
        ExecutorService threadPool = ThreadHandler.getInstance().threadPool;
        try {
            System.out.println("    Connecting to local database");
            this.database = new Database();
            this.externalConnections = new ExternalConnections();
            // TODO: Using temp data
            // Semester constructor should be provided with the results of externalConnections methods
            System.out.println("    Connecting to external databases");
            List<Student> students;
            List<Professor> professors;
            List<Course> courses;
            Future<List<Student>> studentThread = threadPool.submit(() -> {return this.externalConnections.getAllStudents();});
            Future<List<Professor>> professorThread = threadPool.submit(() -> {return this.externalConnections.getAllProfessors();});
            Future<List<Course>> courseThread = threadPool.submit(() -> {return this.externalConnections.getAllCourses();});
            try {
                students = studentThread.get();
                System.out.println("        Students successful");
            } catch (InterruptedException | ExecutionException e) {
                students = ExampleData.students;
                System.out.println("Error:" + e.getMessage());
                System.out.println("        Init failure... falling back to ExampleData.students");
            }
            try {
                professors = professorThread.get();
                System.out.println("        Professors successful");
            } catch (InterruptedException | ExecutionException e) {
                professors = ExampleData.professors;
                System.out.println("Error:" + e.getMessage());
                System.out.println("        Init failure... falling back to ExampleData.professors");
            }
            try {
                courses = courseThread.get();
                System.out.println("        Courses successful");
            } catch (InterruptedException | ExecutionException e) {
                courses = ExampleData.courses;
                System.out.println("Error:" + e.getMessage());
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

    public String viewProfessorsCSV() {
        StringBuilder result = new StringBuilder();
        result.append(
            """
            id,name
            """
        );
        for (Professor professor : this.semester.professors.values()) {
            result.append(
            """
            %d,%s
            """.formatted(professor.id, professor.name)
            );
        }
        return result.toString();
    }
    
}
