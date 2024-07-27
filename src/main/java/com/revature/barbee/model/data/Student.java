package com.revature.barbee.model.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student {
    public int id;
    public String name;
    public int age;

    public Student() {}
    
    public Student(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public static List<Student> parseCSVString(String csv) {
        List<Student> result = new ArrayList<>();
        // List<String> columnNames = new ArrayList<>();
        Map<String, Integer> columnIndex = new HashMap<>();
        String[] lines = csv.split(System.lineSeparator());
        String[] headers = lines[0].split(",");
        for (int i = 0; i < headers.length; i++) {
            columnIndex.put(headers[i], i);
        }
        for (int i = 1; i < lines.length; i++) {
            String[] values = lines[i].split(",");
            result.add(new Student(
                Integer.parseInt(values[columnIndex.get("id")]),
                values[columnIndex.get("name")], 
                Integer.parseInt(values[columnIndex.get("age")])
            ));
        }
        return result;
    }
}
