package com.limelight.emulator.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by sylvain on 01/03/16.
 */
public class ServiceInfoHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        System.out.println("getting server info");
        Headers headers = httpExchange.getResponseHeaders();
        headers.add("Content-Type", "application/xml");


        /*
        state : !null ou ne fini pas par _SERVER_AVAIBLE => jeux en cours
        <currentgame></currentgame> defini l'id du jeu en cours
         */


        String response = "<root status_code=\"200\" status_message=\"msg\">" +
                "<appversion>3.0</appversion>" +
                "<PairStatus>1</PairStatus>" +
                "<state>GFE_SERVER_AVAILABLE</state>"+
                "</root>";





        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
