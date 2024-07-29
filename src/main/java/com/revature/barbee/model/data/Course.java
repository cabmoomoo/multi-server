package com.revature.barbee.model.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course {
    public int id;
    public String name;

    public Course() {}

    public Course(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<Course> parseCSVString(String csv) {
        List<Course> result = new ArrayList<>();
        Map<String, Integer> columnIndex = new HashMap<>();
        String[] lines = csv.split("\n");
        String[] headers = lines[0].split(",");
        for (int i = 0; i < headers.length; i++) {
            columnIndex.put(headers[i], i);
        }
        for (int i = 1; i < lines.length; i++) {
            String[] values = lines[i].split(",");
            result.add(new Course(
                Integer.parseInt(values[columnIndex.get("id")]),
                values[columnIndex.get("Course")]
            ));
        }
        return result;
    }
}
