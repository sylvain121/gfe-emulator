package com.limelight.emulator.http;


import com.sun.net.httpserver.*;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.*;
import java.security.cert.CertificateException;

public class GameStreamHttpsServer {

    private static final int HTTPS_PORT = 47984;

    public GameStreamHttpsServer() throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        HttpsServer server = HttpsServer.create(new InetSocketAddress(HTTPS_PORT), 0);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        char[] password = "password".toCharArray();
        KeyStore ks = KeyStore.getInstance("JKS");
        FileInputStream fis = new FileInputStream("/home/sylvain/src/gfe-emulator/keystore.jks");
        ks.load(fis, password);

        // setup the key manager factory
        KeyManagerFactory kmf = KeyManagerFactory.getInstance ( "SunX509" );
        kmf.init ( ks, password );

        // setup the trust manager factory
        TrustManagerFactory tmf = TrustManagerFactory.getInstance ( "SunX509" );
        tmf.init ( ks );

        // setup the HTTPS context and parameters
        sslContext.init ( kmf.getKeyManagers (), tmf.getTrustManagers (), null );
        server.setHttpsConfigurator ( new HttpsConfigurator( sslContext )
        {
            public void configure ( HttpsParameters params )
            {
                try
                {
                    // initialise the SSL context
                    SSLContext c = SSLContext.getDefault ();
                    SSLEngine engine = c.createSSLEngine ();
                    params.setNeedClientAuth ( false );
                    params.setCipherSuites ( engine.getEnabledCipherSuites () );
                    params.setProtocols ( engine.getEnabledProtocols () );

                    // get the default parameters
                    SSLParameters defaultSSLParameters = c.getDefaultSSLParameters ();
                    params.setSSLParameters ( defaultSSLParameters );
                }
                catch ( Exception ex )
                {
                    System.out.println( ex );
                    System.out.println( "Failed to create HTTPS port" );
                }
            }
        } );

        server.createContext("/serverinfo", new serviceInfoHandler());
        server.createContext("/applist", new appListHandler());
        server.createContext("/", new loggerHandler());
        server.setExecutor(null);
        server.start();
    }


}
