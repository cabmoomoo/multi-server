package com.revature.barbee;

import java.io.File;
import java.net.URISyntaxException;

import com.revature.barbee.model.HTTPContentType;
import com.revature.barbee.model.MultiServerError;
import com.revature.barbee.service.FileService;
import com.revature.barbee.service.Service;
import com.revature.barbee.utils.Request;
import com.revature.barbee.utils.Response;
import com.revature.barbee.utils.ResponseFactory;

public class ServerController {
    private final Service service;
    private final FileService fileService;

    public ServerController() throws MultiServerError {
        this.service = new Service();
        this.fileService = new FileService();
    }

    public Server startAPI(int port) {
        System.out.println("    Building server");
        Server server = new Server(port);
        server.addGetEndpoint("/", this::endpointGetEverything);
        server.addGetEndpoint("/favicon.ico", this::endpointGetFavicon);
        server.addGetEndpoint("/test", this::endpointTest);
        return server;
    }

    private void endpointGetEverything(Request req, Response res) {
        String body = service.viewCoursesProfessorStudents();
        ResponseFactory.HTMLOK(res)
            .setBody(body)
            .build()
            .send();
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
            .setType(HTTPContentType.ICON)
            .build()
            .sendFile(faviconFile);
    }

    private void endpointTest(Request req, Response res) {
        ResponseFactory.plainOK(res)
            .setBody("Hi there!")
            .build()
            .send();
    }
    
}
