package com.limelight.emulator.http;


import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class GameStreamHttpServer  {

    private static final int HTTP_PORT = 47989;

    public GameStreamHttpServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(HTTP_PORT), 0);


        //server.createContext("/serverinfo", new serviceInfoHandler());
        server.setExecutor(null);
        server.start();
    }
}
