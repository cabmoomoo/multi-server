package com.revature.barbee.dao;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.TreeMap;

import com.revature.barbee.model.HTTPStatus;
import com.revature.barbee.model.MultiServerError;
import com.revature.barbee.model.data.Student;
import com.revature.barbee.utils.ExternalResponse;

public class ExternalConnections {

    private final TreeMap<String, String> addressMap = new TreeMap<>() {{
        put("Students", "");
        put("Professors", "64.99.221.113:54321");
        put("Courses", "");
    }};

    public List<Student> getAllStudents() throws MultiServerError {
        List<Student> students;
        try {
            URL url = new URI(String.format("http://%s", addressMap.get("Students"))).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            ExternalResponse req = new ExternalResponse(conn.getInputStream());
            if (req.status != HTTPStatus.OK) {
                throw new IOException(String.format("Server returned non-OK status: %s%nServer message: %s", req.status.toString(), req.body));
            }
            students = Student.parseCSVString(req.body);
            conn.disconnect();
        } catch (URISyntaxException | IOException ex) {
            throw new MultiServerError(String.format("Communication with Students server failed:%n") + ex.getMessage());
        }
        return students;
    }

}
