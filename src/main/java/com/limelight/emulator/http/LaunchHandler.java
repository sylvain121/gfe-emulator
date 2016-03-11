package com.limelight.emulator.http;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by sylvain on 11/03/16.
 */
public class LaunchHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        /*

        NvHTTP.java:551

        /launch?
        uniqueid=c20303f58f801b99&uuid=1bed8d85-2d9d-4b5e-bd70-f77bb89be958
        &appid=100
        &mode=1280x720x60
        &additionalStates=1
        &sops=1
        &rikey=D9B51BA363F7A8A0801F5AE081FC878F
        &rikeyid=125667060
        &localAudioPlayMode=0
        &surroundAudioInfo=196610
         */


        System.out.println("launching game id : ");
        String response =
                "<root status_code=\"200\" status_message=\"msg\">" +
                "<gamesession>123456789</gamesession>"+
                "</root>";



        Headers headers = httpExchange.getResponseHeaders();
        headers.add("Content-Type", "application/xml");

        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
