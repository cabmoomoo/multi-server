package com.revature.barbee;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadHandler {
    public final ExecutorService threadPool;

    // Singleton pattern
    private static ThreadHandler threadHandlerInstance = null;
    private ThreadHandler() {
        this.threadPool = Executors.newCachedThreadPool();
        ShutdownHandler.getInstance().addHandle(this::shutdown);
    }
    public static ThreadHandler getInstance() {
        if (ThreadHandler.threadHandlerInstance != null) {
            return ThreadHandler.threadHandlerInstance;
        }
        ThreadHandler.threadHandlerInstance = new ThreadHandler();
        return ThreadHandler.threadHandlerInstance;
    }

    public void shutdown() {
        System.out.println("    Closing thread pool...");
        try {
            this.threadPool.close();
            System.out.println("    Thread pool closed.");
        } catch (Exception e) { // Seems this only throws the unchecked SecurityException, so this should never be an issue.
            System.out.println("Thread pool did not close!");
            System.out.println("Error: " + e.getMessage());
        }
    }
}
