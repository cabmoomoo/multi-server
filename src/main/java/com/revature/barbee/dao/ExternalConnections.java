package com.revature.barbee.dao;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.TreeMap;

import com.revature.barbee.model.MultiServerError;
import com.revature.barbee.model.data.Course;
import com.revature.barbee.model.data.Professor;
import com.revature.barbee.model.data.Student;
import com.revature.barbee.utils.ExternalResponse;

public class ExternalConnections {

    private final TreeMap<String, String> addressMap = new TreeMap<>() {{
        put("Students", "98.121.62.71:47532");
        put("Professors", "64.99.221.113:54321");
        put("Courses", "99.130.147.100:50000");
    }};

    public List<Student> getAllStudents() throws MultiServerError {
        List<Student> students;
        try {
            URL url = new URI(String.format("http://%s/getStudents", addressMap.get("Students"))).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.connect();
            ExternalResponse req = new ExternalResponse(conn.getInputStream(), conn.getContentLength());
            // if (req.status != HTTPStatus.OK) {
            //     throw new IOException(String.format("Server returned non-OK status: %s%nServer message: %s", req.status.toString(), req.body));
            // }
            students = Student.parseCSVString(req.body);
            conn.disconnect();
        } catch (URISyntaxException | IOException ex) {
            throw new MultiServerError(String.format("Communication with Students server failed:%n") + ex.getMessage());
        }
        return students;
    }

    public List<Professor> getAllProfessors() throws MultiServerError {
        List<Professor> professors;
        try {
            URL url = new URI(String.format("http://%s/table", addressMap.get("Professors"))).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.connect();
            ExternalResponse req = new ExternalResponse(conn.getInputStream(), conn.getContentLength());
            // if (req.status != HTTPStatus.OK) {
            //     throw new IOException(String.format("Server returned non-OK status: %s%nServer message: %s", req.status.toString(), req.body));
            // }
            professors = Professor.parseCSVString(req.body);
            conn.disconnect();
        } catch (URISyntaxException | IOException ex) {
            throw new MultiServerError(String.format("Communication with Professors server failed:%n") + ex.getMessage());
        }
        return professors;
    }

    public List<Course> getAllCourses() throws MultiServerError {
        List<Course> courses;
        try {
            URL url = new URI(String.format("http://%s/courses", addressMap.get("Courses"))).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.connect();
            ExternalResponse req = new ExternalResponse(conn.getInputStream(), conn.getContentLength());
            // if (req.status != HTTPStatus.OK) {
            //     throw new IOException(String.format("Server returned non-OK status: %s%nServer message: %s", req.status.toString(), req.body));
            // }
            courses = Course.parseCSVString(req.body);
            conn.disconnect();
        } catch (URISyntaxException | IOException ex) {
            throw new MultiServerError(String.format("Communication with Courses server failed:%n") + ex.getMessage());
        }
        return courses;
    }

}
