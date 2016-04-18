package com.limelight.emulator.av;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class VideoHeader {
	public static final int PACKET_SIZE = 1024;
	public static final int HEADER_SIZE = 16;
	
	public static final int FLAG_EOF = 0x2;
	public static final int FLAG_SOF = 0x4;
	public static final int FLAG_CONTAINS_PIC_DATA = 0x1;
	
	private byte[] fullPacket;
	
	public VideoHeader(int frameIndex, int packetIndex, int totalPackets, int flags, int streamPacketIndex, byte[] payload) {
		fullPacket = new byte[PACKET_SIZE];
		ByteBuffer bb = ByteBuffer.wrap(fullPacket).order(ByteOrder.LITTLE_ENDIAN);
		streamPacketIndex = streamPacketIndex << 8;

		bb.rewind();
		bb.putInt(streamPacketIndex);
		bb.putInt(frameIndex);
		bb.putInt(flags);

		//bb.putInt(packetIndex);
		//bb.putInt(totalPackets);
		//bb.putInt(payload.length);

		
		System.arraycopy(payload, 0, fullPacket, HEADER_SIZE, payload.length);
	}
	
	public VideoHeader(byte[] payload) {
		fullPacket = new byte[HEADER_SIZE+payload.length];
		ByteBuffer bb = ByteBuffer.wrap(fullPacket).order(ByteOrder.LITTLE_ENDIAN);
		
		bb.rewind();
		bb.putInt(1);
		bb.putInt(0);
		bb.putInt(1);
		bb.putInt(FLAG_SOF | FLAG_EOF);
		bb.putInt(payload.length);
		bb.putInt(1);
		
		System.arraycopy(payload, 0, fullPacket, HEADER_SIZE, payload.length);
	}
	
	public byte[] toBytes() {
		return fullPacket;
	}
}
