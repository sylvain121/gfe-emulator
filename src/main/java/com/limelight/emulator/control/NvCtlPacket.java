package com.limelight.emulator.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by sylvain on 15/03/16.
 */
class NvCtlPacket {
    public short type;
    public short paylen;
    public byte[] payload;

    private static final ByteBuffer headerBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
    private static final ByteBuffer serializationBuffer = ByteBuffer.allocate(128).order(ByteOrder.LITTLE_ENDIAN);

    public NvCtlPacket(InputStream in) throws IOException
    {
        // Use the class's static header buffer for parsing the header
        synchronized (headerBuffer) {
            int offset = 0;
            byte[] header = headerBuffer.array();
            do
            {
                int bytesRead = in.read(header, offset, header.length - offset);
                if (bytesRead < 0) {
                    break;
                }
                offset += bytesRead;
            } while (offset != header.length);

            if (offset != header.length) {
                throw new IOException("Socket closed prematurely");
            }

            headerBuffer.rewind();
            type = headerBuffer.getShort();
            paylen = headerBuffer.getShort();
        }

        if (paylen != 0)
        {
            payload = new byte[paylen];

            int offset = 0;
            do
            {
                int bytesRead = in.read(payload, offset, payload.length - offset);
                if (bytesRead < 0) {
                    break;
                }
                offset += bytesRead;
            } while (offset != payload.length);

            if (offset != payload.length) {
                throw new IOException("Socket closed prematurely");
            }
        }
    }

    public NvCtlPacket(byte[] packet)
    {
        synchronized (headerBuffer) {
            headerBuffer.rewind();

            headerBuffer.put(packet, 0, 4);
            headerBuffer.rewind();

            type = headerBuffer.getShort();
            paylen = headerBuffer.getShort();
        }

        if (paylen != 0)
        {
            payload = new byte[paylen];
            System.arraycopy(packet, 4, payload, 0, paylen);
        }
    }

    public NvCtlPacket(short type, short paylen)
    {
        this.type = type;
        this.paylen = paylen;
    }

    public NvCtlPacket(short type, short paylen, byte[] payload)
    {
        this.type = type;
        this.paylen = paylen;
        this.payload = payload;
    }

    public short getType()
    {
        return type;
    }

    public short getPaylen()
    {
        return paylen;
    }

    public void setType(short type)
    {
        this.type = type;
    }

    public void setPaylen(short paylen)
    {
        this.paylen = paylen;
    }

    public void write(OutputStream out) throws IOException
    {
        // Use the class's serialization buffer to construct the wireform to send
        synchronized (serializationBuffer) {
            serializationBuffer.rewind();
            serializationBuffer.putShort(type);
            serializationBuffer.putShort(paylen);
            serializationBuffer.put(payload);

            out.write(serializationBuffer.array(), 0, serializationBuffer.position());
        }
    }
}
