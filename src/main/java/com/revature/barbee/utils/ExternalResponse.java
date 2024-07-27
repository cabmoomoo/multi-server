package com.revature.barbee.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.revature.barbee.model.HTTPStatus;

public class ExternalResponse {
    public HTTPStatus status;
    public Map<String, String> headers = new HashMap<>();
    public String body;

    public ExternalResponse(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = reader.readLine();
        String[] statusLine = line.split(" ");
        this.status = HTTPStatus.valueOfCode(Integer.parseInt(statusLine[1]));

        while ((line = reader.readLine()).length() > 0) {
            if (line.contains(":")) {
                String[] tokens = line.split(": ");
                this.headers.put(tokens[0], tokens[1]);
            }
        }

        if (this.headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(this.headers.get("Content-Length"));
            char[] bodyArray = new char[contentLength];
            reader.read(bodyArray);
            this.body = new String(bodyArray);
        }
    }
}
