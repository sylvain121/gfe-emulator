package com.limelight.emulator.http;

import fi.iki.elonen.NanoHTTPD;



import javax.xml.ws.Response;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * Created by sylvain121 on 01/03/16.
 */

public class GameStreamHttpServer extends NanoHTTPD {

    private static final int HTTP_PORT = 47989;
    private static final int HTTPS_PORT = 47984;

    public GameStreamHttpServer() throws IOException {
        super(HTTPS_PORT);
    }

    @Override
    public Response serve(IHTTPSession session) {

        String uri = session.getUri();
        System.out.println(uri);
        String data = "";
        if(uri.equals("/serverinfo")) {
            data = this.returnServerInfo(session.getQueryParameterString());
        }
        return newFixedLengthResponse(Response.Status.OK, "application/xml", data);

    }

    private String returnServerInfo(String queryParameterString) {
        return "<root><appversion>2.2.2</appversion><PairStatus>1</PairStatus></root>";
    }
}
