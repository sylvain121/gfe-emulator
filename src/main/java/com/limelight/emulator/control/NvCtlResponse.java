package com.limelight.emulator.control;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by sylvain on 15/03/16.
 */
class NvCtlResponse extends NvCtlPacket {
    public short status;

    public NvCtlResponse(InputStream in) throws IOException {
        super(in);
    }

    public NvCtlResponse(short type, short paylen) {
        super(type, paylen);
    }

    public NvCtlResponse(short type, short paylen, byte[] payload) {
        super(type, paylen, payload);
    }

    public NvCtlResponse(byte[] payload) {
        super(payload);
    }

    public void setStatusCode(short status)
    {
        this.status = status;
    }

    public short getStatusCode()
    {
        return status;
    }
}
