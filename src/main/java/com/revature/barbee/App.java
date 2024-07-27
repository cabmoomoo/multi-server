package com.revature.barbee;

import com.revature.barbee.model.MultiServerError;

public class App {
    public static void main(String[] args) throws MultiServerError {
        System.out.println("Starting up...");
        ServerController serverController = new ServerController();
        int port = 53264;

        Server server = serverController.startAPI(port);
        server.start();
    }
}
