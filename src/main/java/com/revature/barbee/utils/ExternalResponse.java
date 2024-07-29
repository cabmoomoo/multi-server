package com.revature.barbee.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExternalResponse {
    // public HTTPStatus status;
    // public Map<String, String> headers = new HashMap<>();
    public String body;

    public ExternalResponse(InputStream in, int contentLength) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        char[] bodyArray = new char[contentLength];
        reader.read(bodyArray);
        this.body = new String(bodyArray);
    }
}
