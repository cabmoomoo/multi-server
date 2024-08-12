package com.revature.barbee.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.revature.barbee.model.AcceptedHeader;
import com.revature.barbee.model.HTTPMIMEType;
import com.revature.barbee.model.HTTPRequestMethod;

public class Request {
    public HTTPRequestMethod method;
    public String path;
    public Map<String, String> headerMap = new HashMap<>();
    public List<AcceptedHeader> acceptedHeader = new ArrayList<>();
    public String body;
    
    public Request(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = reader.readLine();
        String[] requestLine = line.split(" ");
        this.method = HTTPRequestMethod.valueOf(requestLine[0]);
        this.path = requestLine[1];

        while ((line = reader.readLine()).length() > 0) {
            if (line.contains(":")) {
                String[] tokens = line.split(": ");
                this.headerMap.put(tokens[0], tokens[1]);
            }
        }

        if (this.headerMap.containsKey("Accepted")) {
            String[] acceptedTokens = this.headerMap.get("Accepted").split(",");
            for (String token : acceptedTokens) {
                if (!token.contains(";")) {
                    HTTPMIMEType type = HTTPMIMEType.get(token);
                    if (type == null) {
                        continue;
                    }
                    this.acceptedHeader.add(new AcceptedHeader(type));
                    continue;
                }
                String[] splitToken = token.split(";");
                HTTPMIMEType type = HTTPMIMEType.get(splitToken[0]);
                if (type == null) {
                    continue;
                }
                double qualityValue;
                try {
                    qualityValue = Double.parseDouble(splitToken[1]);
                } catch (NumberFormatException ex) {
                    qualityValue = 1.0d;
                }
                this.acceptedHeader.add(new AcceptedHeader(type, qualityValue));
            }
        } else {
            this.acceptedHeader.add(new AcceptedHeader(HTTPMIMEType.ANY));
        }
        
        if (this.headerMap.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(this.headerMap.get("Content-Length"));
            char[] bodyArray = new char[contentLength];
            reader.read(bodyArray);
            this.body = new String(bodyArray);
        }
    }
}
