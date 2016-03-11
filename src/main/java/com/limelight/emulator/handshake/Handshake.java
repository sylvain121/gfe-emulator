package com.limelight.emulator.handshake;

import com.tinyrtsp.rtsp.message.RtspMessage;
import com.tinyrtsp.rtsp.message.RtspRequest;
import com.tinyrtsp.rtsp.message.RtspResponse;
import com.tinyrtsp.rtsp.parser.RtspStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Handshake {
	private ServerSocket serverSocket;
	
	public static final int PORT = 48010;
	
	public void start() throws IOException {
		serverSocket = new ServerSocket(PORT);
		System.out.println("Handshake connection listening on "+PORT);

		new Thread() {
			@Override
			public void run() {
				for (;;) {
					try {
						Socket s = serverSocket.accept();
						
						System.out.println("Handshake connected from "+s.getRemoteSocketAddress());
						try {
							// Wait for the client to close this connection
							InputStream sin = s.getInputStream();
							OutputStream sout = s.getOutputStream();


							RtspStream rtspStream = new RtspStream(s.getInputStream(), s.getOutputStream());
							while(true) {
								RtspRequest resp = (RtspRequest) rtspStream.read();

							}

/*
							while ( sin.read() != -1) {
								System.out.println("Handhake data" + sin.read());
								sout.write(0);
							}
*/
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
