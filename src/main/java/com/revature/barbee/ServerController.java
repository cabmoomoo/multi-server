package com.revature.barbee;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.revature.barbee.model.AcceptedHeader;
import com.revature.barbee.model.HTTPMIMEType;
import com.revature.barbee.model.HTTPStatus;
import com.revature.barbee.model.MultiServerError;
import com.revature.barbee.service.FileService;
import com.revature.barbee.service.HTMLService;
import com.revature.barbee.service.Service;
import com.revature.barbee.utils.Request;
import com.revature.barbee.utils.Response;
import com.revature.barbee.utils.ResponseFactory;

public class ServerController {
    private final Service service;
    private final HTMLService webService;
    private final FileService fileService;

    public ServerController() throws MultiServerError {
        this.service = new Service();
        this.webService = new HTMLService();
        this.fileService = new FileService();
    }

    public Server startAPI(int port) {
        System.out.println("    Building server");
        Server server = new Server(port);
        server.addGetEndpoint("/", this::endpointViewSemesterSchedule);
        server.addGetEndpoint("/courses", this::endpointViewCourses);
        server.addGetEndpoint("/professors", this::endpointViewProfessors);
        server.addGetEndpoint("/favicon.ico", this::endpointGetFavicon);
        server.addGetEndpoint("/test", this::endpointTest);
        server.addPostEndpoint("/pleaseshutdown", this::endpointShutdown);
        return server;
    }

    private void endpointViewSemesterSchedule(Request req, Response res) {
        String body = webService.viewCoursesProfessorStudents();
        ResponseFactory.HTMLOK(res)
            .setBody(body)
            .build()
            .send();
    }

    private void endpointViewCourses(Request req, Response res) {
        String body = webService.viewCourses();
        ResponseFactory.HTMLOK(res)
            .setBody(body)
            .build()
            .send();
    }

    private void endpointViewProfessors(Request req, Response res) {
        List<HTTPMIMEType> supported = new ArrayList<>() {{
            add(HTTPMIMEType.HTML);
            add(HTTPMIMEType.CSV);
            add(HTTPMIMEType.ANY_TEXT);
            add(HTTPMIMEType.ANY);
        }};
        Optional<HTTPMIMEType> firstSupported = AcceptedHeader.firstSupported(req.acceptedHeader, supported);
        if (firstSupported.isEmpty()) {
            new ResponseFactory(res)
                .setStatus(HTTPStatus.UNSUPPORTED_MEDIA_TYPE)
                .setType(HTTPMIMEType.PLAIN)
                .setBody("The requested media types are not supported by this endpoint.")
                .build()
                .send();
            return;
        }
        HTTPMIMEType responseType = AcceptedHeader.firstSupported(req.acceptedHeader, supported).get();
        String body;
        switch (responseType) {
            case HTTPMIMEType.CSV -> {
                body = this.service.viewProfessorsCSV();
                ResponseFactory.CSVOK(res)
                    .setBody(body)
                    .build()
                    .send();
            }
            default -> {
                body = this.webService.viewProfessorsCourses();
                ResponseFactory.HTMLOK(res)
                    .setBody(body)
                    .build()
                    .send();
            }
        }
        
    }

    private void endpointGetFavicon(Request req, Response res) {
        File faviconFile;
        try {
            faviconFile = this.fileService.pathToFile("favicon.ico");
        } catch (URISyntaxException | IllegalArgumentException ex) {
            // Mozilla notes that for an API, 404 can also mean "the endpoint is valid but the resource itself does not exist"
            // https://developer.mozilla.org/en-US/docs/Web/HTTP/Status#client_error_responses
            ResponseFactory.fileNotFoundResponse(res).send();
            return;
        }
        ResponseFactory.fileOK(res)
            .setType(HTTPMIMEType.ICON)
            .build()
            .sendFile(faviconFile);
    }

    private void endpointTest(Request req, Response res) {
        ResponseFactory.plainOK(res)
            .setBody("Hi there!")
            .build()
            .send();
    }

    private void endpointShutdown(Request req, Response res) {
        if ((req.body != null) && req.body.equalsIgnoreCase("123456")) {
            ResponseFactory.plainOK(res)
                .setBody("Okie dokie, I'll try and shut down now!")
                .build()
                .send();
            System.out.println("Shutdown request recieved from external connection.");
            try {
                Server.serverSocket.close();
            } catch (IOException ex) {
                System.out.println("Failed to close Server.serverSocket:");
                System.out.println(ex.getMessage());
            }
        } else {
            new ResponseFactory(res)
                .setStatus(HTTPStatus.UNAUTHORIZED)
                .setType(HTTPMIMEType.PLAIN)
                .setBody("Invalid password! You little goofy goober.")
                .build()
                .send();
        }
    }
    
}
