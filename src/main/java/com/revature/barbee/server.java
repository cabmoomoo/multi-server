package com.revature.barbee;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import com.revature.barbee.model.HTTPRequestMethod;
import com.revature.barbee.utils.Request;
import com.revature.barbee.utils.Response;
import com.revature.barbee.utils.ResponseFactory;
import com.revature.barbee.utils.Servlet;

public class Server {
    private final int port;
    static ServerSocket serverSocket;
    private final Map<HTTPRequestMethod, Map<String, Servlet>> endpointMap = new HashMap<>();
    private final ExecutorService threadPool = ThreadHandler.getInstance().threadPool;

    /*
     * Init the server
     */
    public Server(int port) {
        this.port = port;
        ShutdownHandler.getInstance().addHandle(this::shutdown);
        // Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown()));
    }

    /*
     * Declare Enpoint methods
     */
    public void addEndpoint(HTTPRequestMethod method, String path, Servlet servlet) {
        Map<String, Servlet> methodMap = this.endpointMap.get(method);
        if (methodMap == null) {
            this.endpointMap.put(method, new HashMap<>());
            methodMap = this.endpointMap.get(method);
        }
        methodMap.put(path, servlet);
    }

    public void addGetEndpoint(String path, Servlet servlet) {
        Map<String, Servlet> methodMap = this.endpointMap.get(HTTPRequestMethod.GET);
        if (methodMap == null) {
            this.endpointMap.put(HTTPRequestMethod.GET, new HashMap<>());
            methodMap = this.endpointMap.get(HTTPRequestMethod.GET);
        }
        methodMap.put(path, servlet);
    }

    public void addPostEndpoint(String path, Servlet servlet) {
        Map<String, Servlet> methodMap = this.endpointMap.get(HTTPRequestMethod.POST);
        if (methodMap == null) {
            this.endpointMap.put(HTTPRequestMethod.POST, new HashMap<>());
            methodMap = this.endpointMap.get(HTTPRequestMethod.POST);
        }
        methodMap.put(path, servlet);
    }

    /*
     * Manage the server
     */
    public void shutdown() {
        System.out.println("    Closing server socket...");
        try {
            Server.serverSocket.close();
            System.out.println("    Server socket closed.");
        } catch (IOException e) {
            System.out.println("Server socket did not close!");
            System.out.println("Error: " + e.getMessage());
        }
    }

    /*
     * Running the server
     */
    public void start() {
        try {
            if (Server.serverSocket == null) {
                Server.serverSocket = new ServerSocket(this.port);
            }
            System.out.println("Starting server on port: " + this.port);
            while (Server.serverSocket.isBound()) {
                Socket client = serverSocket.accept();
                this.threadPool.execute(() -> handle(client));
            }
        } catch (SocketException ex) {
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void handle(Socket client) {
        Request req;
        Response res;
        try {
            req = new Request(client.getInputStream());
            res = new Response(client.getOutputStream());
        } catch (IOException e) {
            System.out.println("IOException encountered. Terminating thread.");
            System.out.println(e.getMessage());
            return;
        }
        Servlet servlet;
        try {
            Map<String, Servlet> methodMap = this.endpointMap.get(req.method);
            servlet = methodMap.get(req.path);
            if (servlet == null) {
                throw new NullPointerException("Method exists, but path does not");
            }
        } catch (NullPointerException e) {
            String body = "No servlet exists for the following request:\nMethod: " + req.method.toString() + "; Path: " + req.path;
            System.out.println(body);
            System.out.println(e.getMessage());
            ResponseFactory.badRequest(res, body)
                .send();
            return;
        }
        servlet.service(req, res);
    }

}
