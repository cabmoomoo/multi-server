package com.revature.barbee.model;

import java.util.Map;
import java.util.TreeMap;

public enum HTTPStatus {
    OK(200),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    HTTPStatus(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code + " " + this.name();
    }

    // Index/cache reverse construction of enum from code
    private static final Map<Integer, HTTPStatus> BY_CODE = new TreeMap<>();
    static { // static blocks are only ran once, when the class is loaded the first time
        for (HTTPStatus s: values()) {
            BY_CODE.put(s.code, s);
        }
    }

    /**
     * Construct an HTTPStatus in reverse, using the numerical code to create the enum
     * 
     * @param code Integer HTTP status code
     * @return Enum variant matching code, or null if unknown
     */
    public static HTTPStatus valueOfCode(int code) {
        return BY_CODE.get(code);
    }
}
