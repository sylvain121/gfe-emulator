package com.limelight.emulator;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import com.limelight.emulator.av.Video;
import com.limelight.emulator.av.VideoPacketizer;
import com.limelight.emulator.control.Control;
import com.limelight.emulator.dataparser.H264Parser;
import com.limelight.emulator.handshake.Handshake;
import com.limelight.emulator.http.GameStreamHttpServer;
import com.limelight.emulator.http.GameStreamHttpsServer;
import com.limelight.emulator.input.Input;
import com.limelight.emulator.mdns.Mdns;

public class Emulator {
	public static void main(String[] args) throws IOException {

		H264Parser parser = new H264Parser();
		parser.loadFile(new File("/home/sylvain/src/gfe-emulator/test.h264"));
		
		Handshake h = new Handshake();
		Control c = new Control();
		Input i = new Input();
		Video v = new Video(new VideoPacketizer(parser));
		try {
			GameStreamHttpsServer http = new GameStreamHttpsServer();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		new Mdns();
		
		h.start();
		c.start();
		i.start();
		v.start();
	}
}
