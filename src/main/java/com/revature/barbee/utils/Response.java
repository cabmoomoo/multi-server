package com.revature.barbee.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;

import com.revature.barbee.model.HTTPMIMEType;
import com.revature.barbee.model.HTTPStatus;

public class Response {
    private final PrintWriter out;
    private final OutputStream outputStream;
    HTTPStatus status;
    HTTPMIMEType type;
    String body = "";
    
    public Response(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.out = new PrintWriter(outputStream, true);
    }

    public void send() {
        out.println("HTTP/1.1 " + this.status.toString());
        out.println("Content-Length: " + this.body.length());
        out.println("Content-Type: " + this.type.toString());
        // Connection: close is fine for HTTP/1.1 (which is all we need for now), but is prohibited in higher versions
        // https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Connection
        out.println("Connection: close"); 
        out.println();
        out.println(this.body);
    }

    public void send(String body, HTTPStatus status, HTTPMIMEType type) {
        out.println("HTTP/1.1 " + status.toString());
        out.println("Content-Length: " + body.length());
        out.println("Content-Type: " + type.toString());
        out.println("Connection: close");
        out.println();
        out.println(body);
    }

    public void sendFile(File file) {
        out.println("HTTP/1.1 " + this.status.toString());
        out.println("Content-Type: " + this.type.toString());
        out.println("Content-Length: " + file.length());
        out.println("Connection: close");
        out.println();
        try {
            Files.copy(file.toPath(), this.outputStream);
        } catch (IOException ex) {
            System.out.println("We've encountered an IOException while sending a file... but we've already told the reciever to expect the file. Uh-oh.");
        }
    }
    
}
