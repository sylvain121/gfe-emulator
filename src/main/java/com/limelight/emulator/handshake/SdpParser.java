package com.limelight.emulator.handshake;

import com.tinyrtsp.rtsp.message.RtspRequest;
import com.tinyrtsp.rtsp.message.RtspResponse;

import java.util.HashMap;

public class SdpParser {


    private int sessionId = 0;

    public RtspResponse parse(RtspRequest request) {
        if(request != null) {
            String type = this.getRequestType(request);
            if(type.equals("OPTIONS")) {
                return getAvaiblesOptions();
            } else if( type.equals("DESCRIBE")) {
                return getAvaiblesData();
            } else if(type.equals("SETUP")) {
                return setupStream(request);
            } else if(type.equals("PLAY")) {
                return startStream(request);
            } else if(type.equals("ANNOUNCE")){
                return getAvaiblesAnnouces(request);
            }
        }


        return null;
    }

    private RtspResponse getAvaiblesAnnouces(RtspRequest request) {
        //TODO
        System.out.print("ANNOUCE-------------------");
        System.out.print(request.getPayload());
        return new RtspResponse("RTSP/1.0", 200, "OK", 0, null, "");
    }

    private RtspResponse startStream(RtspRequest request) {
        int s = Integer.parseInt(request.getOption("Session"));
        if(s == this.sessionId){
            String target = request.getTarget();
            if( target.contains("video")){
                startVideoStream();
            } else if ( target.contains("audio")){
                startAudioStream();
            }
        }
        return new RtspResponse("RTSP/1.0", 200, "OK", 0, null, "");
    }

    private void startAudioStream() {
        //TODO
        System.out.println("start audio stream");
    }

    private void startVideoStream() {
        //TODO
        System.out.println("start video stream");
    }

    private RtspResponse setupStream(RtspRequest request) {
        String sessionString = request.getOption("Session");
        if(sessionString == null ){
            generateNewSessionId();
       }
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("Session", this.sessionId+"");
        return new RtspResponse("RTSP/1.0", 200, "OK", 0, options, "");
    }

    private void generateNewSessionId() {
        this.sessionId = 123456;
    }

    private RtspResponse getAvaiblesData() {
        /*
        "sprop-parameter-sets=AAAAAU" for h265
         */
        return new RtspResponse("RTSP/1.0", 200, "OK", 0, null, "sprop-parameter-sets=XXXXX");
    }

    private RtspResponse getAvaiblesOptions() {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("Public", "DESCRIBE, SETUP, ANNOUCES, PLAY");
        return new RtspResponse("RTSP/1.0", 200, "OK", 0, options, "");
    }

    private String getRequestType(RtspRequest request) {
        System.out.println(request.getCommand());
         return request.getCommand();
    }
}
