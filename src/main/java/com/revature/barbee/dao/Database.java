package com.revature.barbee.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class Database {
    private final Connection connection;

    public Database() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:h2:mem:schedule;INIT=runscript from 'init.sql'", "sa", "");
    }

    public void close() throws SQLException {
        this.connection.close();
    }

    public List<Entry<Integer, Integer>> selectAllCourses_Professors() throws SQLException {
        List<Entry<Integer, Integer>> result = new ArrayList<>();
        String query = "SELECT * FROM course_professor;";
        Statement stmt = this.connection.createStatement();
        stmt.execute(query);
        ResultSet rs = stmt.getResultSet();
        while (rs.next()) {
            result.add(new SimpleEntry<>(rs.getInt(1), rs.getInt(2)));
        }
        return result;
    }

    public void insertCourse_Professor(int courseID, int professorID) throws SQLException {
        String query = "INSERT INTO course_professor VALUES (?, ?);";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, courseID);
        stmt.setInt(2, professorID);
        stmt.execute();
    }

    public List<Entry<Integer, Integer>> selectAllStudents_Courses() throws SQLException {
        List<Entry<Integer, Integer>> result = new ArrayList<>();
        String query = "SELECT * FROM student_course;";
        Statement stmt = this.connection.createStatement();
        stmt.execute(query);
        ResultSet rs = stmt.getResultSet();
        while (rs.next()) {
            result.add(new SimpleEntry<>(rs.getInt(1), rs.getInt(2)));
        }
        return result;
    }

    public void insertStudent_Course(int studentID, int courseID) throws SQLException {
        String query = "INSERT INTO student_course VALUES (?, ?)";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, studentID);
        stmt.setInt(2, courseID);
        stmt.execute();
    }

    public void deleteStudent(int studentID) throws SQLException {
        String query = "DELETE FROM student_course WHERE studentID = ?;";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, studentID);
        stmt.execute();
    }

    public void deleteProfessor(int professorID) throws SQLException {
        String query = "DELETE FROM course_professor WHERE professorID = ?";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, professorID);
        stmt.execute();
    }

    public void deleteCourse(int courseID) throws SQLException {
        String query = "DELETE FROM student_course WHERE courseID = ?; DELETE FROM course_professor WHERE courseID = ?";
        PreparedStatement stmt = this.connection.prepareStatement(query);
        stmt.setInt(1, courseID);
        stmt.setInt(2, courseID);
        stmt.execute();
    }
}
