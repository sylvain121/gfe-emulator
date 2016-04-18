package com.limelight.emulator.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Control {

	private static final int IDX_START_A = 0;
	private static final int IDX_REQUEST_IDR_FRAME = 0;
	private static final int IDX_START_B = 1;
	private static final int IDX_INVALIDATE_REF_FRAMES = 2;
	private static final int IDX_LOSS_STATS = 3;

	private static final short packetTypesGen3[] = {
			0x140b, // Start A
			0x1410, // Start B
			0x1404, // Invalidate reference frames
			0x140c, // Loss Stats
			0x1417, // Frame Stats (unused)
	};

	private static final short payloadLengthsGen3[] = {
			0, // Start A
			0, // Start B
			24, // Invalidate reference frames
			32, // Loss Stats
			64, // Frame Stats
	};

	private static final byte[] precontructedPayloadsGen3[] = {
			new byte[]{0}, // Start A
			new byte[]{0}, // Start B
			null, // Invalidate reference frames
			null, // Loss Stats
			null, // Frame Stats
	};



	private ServerSocket serverSocket;
	
	public static final int PORT = 47995;
	
	public void start() throws IOException {
		serverSocket = new ServerSocket(PORT);
		System.out.println("Control connection listening on "+PORT);
		new Thread() {
			@Override
			public void run() {
				for (;;) {
					try {
						Socket s = serverSocket.accept();
						
						System.out.println("Control connected from "+s.getRemoteSocketAddress());
						try {
							// Wait for the client to close this connection
							InputStream sin = s.getInputStream();
							OutputStream sout = s.getOutputStream();

							while (true) {

								NvCtlPacket packet = new NvCtlPacket(sin);
								if( packet.getType() == packetTypesGen3[IDX_START_A]){
									NvCtlPacket response = new NvCtlPacket(packetTypesGen3[IDX_START_A], payloadLengthsGen3[IDX_START_A], precontructedPayloadsGen3[IDX_START_A]);
									response.write(sout);
								} else if( packet.getType() == packetTypesGen3[IDX_START_B]){
									NvCtlPacket response = new NvCtlPacket((short) 0x0000, payloadLengthsGen3[IDX_START_B], precontructedPayloadsGen3[IDX_START_B]);
									response.write(sout);
								} else if(packet.getType() == packetTypesGen3[IDX_LOSS_STATS]){
									// TODO get LOSS_STATS
								} else {
									System.out.println("Control.Class, Unhandled packet type : "+packet.getType());
								}

							}
						} catch (IOException e) {
							// Client died; continue
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
}
