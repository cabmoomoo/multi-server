package com.revature.barbee;

import java.util.ArrayList;
import java.util.List;

public class ShutdownHandler {
    private final List<Runnable> shutdownFunctions = new ArrayList<>();

    private static ShutdownHandler instance = null;
    private ShutdownHandler() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> this.shutdown()));
    }
    public static ShutdownHandler getInstance() {
        if (ShutdownHandler.instance != null) {
            return ShutdownHandler.instance;
        }
        ShutdownHandler.instance = new ShutdownHandler();
        return ShutdownHandler.instance;
    }

    public void addHandle(Runnable handle) {
        this.shutdownFunctions.add(handle);
    }

    private void shutdown() {
        System.out.println("Shutting down...");
        for (Runnable handle : this.shutdownFunctions) {
            handle.run();
        }
        System.out.println("Server resources closed successfully.");
    }
}
