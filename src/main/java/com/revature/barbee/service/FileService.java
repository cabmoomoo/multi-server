package com.revature.barbee.service;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class FileService {
    /*
     * This is technically a violation of the service/data layer conventions I've been following,
     * but I don't see much point in changing it when we'll only do one thing we will do to files
     * is serve them - a single task without any variation in the method. The only thing that will
     * change is the Content-Type response header, and that's handled by the Controller.
     */
    public File pathToFile(String path) throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException("Could not locate file: " + path);
        }
        return new File(resource.toURI());
    }
}
