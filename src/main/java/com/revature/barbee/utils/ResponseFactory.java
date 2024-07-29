package com.revature.barbee.utils;

import java.io.OutputStream;

import com.revature.barbee.model.HTTPMIMEType;
import com.revature.barbee.model.HTTPStatus;

public class ResponseFactory {
    private final Response res;
    
    /*
     * Constructors
     */
    public ResponseFactory(OutputStream outputStream) {
        this.res = new Response(outputStream);
    }

    public ResponseFactory(Response res) {
        this.res = res;
    }

    /*
     * Factory methods
     */
    public ResponseFactory setStatus(HTTPStatus status) {
        this.res.status = status;
        return this;
    }
    
    public ResponseFactory setType(HTTPMIMEType type) {
        this.res.type = type;
        return this;
    }

    public ResponseFactory setBody(String body) {
        this.res.body = body;
        return this;
    }

    /*
     * Build
     */
    public Response build() {
        return this.res;
    }

    /*
     * Response Templates
     */
    public static ResponseFactory plainOK(Response res) {
        return new ResponseFactory(res)
            .setStatus(HTTPStatus.OK)
            .setType(HTTPMIMEType.PLAIN);
    }

    public static ResponseFactory HTMLOK(Response res) {
        return new ResponseFactory(res)
            .setStatus(HTTPStatus.OK)
            .setType(HTTPMIMEType.HTML);
    }

    public static ResponseFactory fileOK(Response res) {
        return new ResponseFactory(res)
            .setStatus(HTTPStatus.OK);
    }

    /*
     * Common Responses
     */
    public static Response fileNotFoundResponse(Response res) {
        res.status = HTTPStatus.NOT_FOUND;
        res.type = HTTPMIMEType.PLAIN;
        res.body = "Could not find file";
        return res;
    }

    public static Response badRequest(Response res, String body) {
        res.status = HTTPStatus.BAD_REQUEST;
        res.type = HTTPMIMEType.PLAIN;
        res.body = body;
        return res;
    }

    public static Response internalServerError(Response res, String body) {
        res.status = HTTPStatus.INTERNAL_SERVER_ERROR;
        res.type = HTTPMIMEType.PLAIN;
        res.body = body;
        return res;
    }

}
