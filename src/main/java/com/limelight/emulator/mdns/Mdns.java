package com.limelight.emulator.mdns;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;

/**
 * Created by sylvain on 01/03/16.
 */
public class Mdns {
    public static final String SERVICE_TYPE = "_nvstream._tcp.local.";
    public static final String COMPUTER_NAME = "OPEN Gamestream";

    public Mdns() throws IOException {
        JmDNS jmdns = JmDNS.create();
        ServiceInfo gamestreamService = ServiceInfo.create(SERVICE_TYPE, COMPUTER_NAME, 6666, "gamestream");
        jmdns.registerService(gamestreamService);

    }
}
