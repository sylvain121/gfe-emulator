package com.limelight.emulator.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by sylvain on 01/03/16.
 */
public class appListHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        ///applist?uniqueid=c20303f58f801b99&uuid=227d2357-60d9-43c6-927e-6daec7939282

        String response = "<root>" +
                "<app AppTitle=\"Desktop\" ID=\"1\" IsRunning=\"false\"></app>" +
                "</root>";



        Headers headers = httpExchange.getResponseHeaders();
        headers.add("Content-Type", "application/xml");

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
