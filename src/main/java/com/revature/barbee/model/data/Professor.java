package com.revature.barbee.model.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Professor {
    public int id;
    public String name;

    public Professor() {}

    public Professor(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<Professor> parseCSVString(String csv) {
        List<Professor> result = new ArrayList<>();
        Map<String, Integer> columnIndex = new HashMap<>();
        String[] lines = csv.split("\n");
        String[] headers = lines[0].split(",");
        for (int i = 0; i < headers.length; i++) {
            columnIndex.put(headers[i], i);
        }
        for (int i = 1; i < lines.length; i++) {
            String[] values = lines[i].split(",");
            result.add(new Professor(
                Integer.parseInt(values[columnIndex.get("ID")]),
                values[columnIndex.get("NAME")]
            ));
        }
        return result;
    }
}
