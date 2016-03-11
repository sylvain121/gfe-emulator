package com.limelight.emulator.http;

import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.OkHttpClient;
import org.junit.Test;

import javax.net.ssl.*;
import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by sylvain on 07/03/16.
 */
public class GameStreamHttpsServerTest {

    private GameStreamHttpsServer http;
    private OkHttpClient httpClient;
    private OkHttpClient httpClientWithReadTimeout;
    private TrustManager[] trustAllCerts;
    private KeyManager[] ourKeyman;

    public static final int CONNECTION_TIMEOUT = 3000;
    public static final int READ_TIMEOUT = 5000;

    @org.junit.Before
    public void setUp() throws Exception {
        http = new GameStreamHttpsServer();
        httpClient = new OkHttpClient();
    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @Test
    public void appListTest() {

    }

    private void initializeHttpState(final LimelightCryptoProvider cryptoProvider) {
        trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                }};

        ourKeyman = new KeyManager[] {
                new X509KeyManager() {
                    public String chooseClientAlias(String[] keyTypes,
                                                    Principal[] issuers, Socket socket) { return "Limelight-RSA"; }
                    public String chooseServerAlias(String keyType, Principal[] issuers,
                                                    Socket socket) { return null; }
                    public X509Certificate[] getCertificateChain(String alias) {
                        return new X509Certificate[] {cryptoProvider.getClientCertificate()};
                    }
                    public String[] getClientAliases(String keyType, Principal[] issuers) { return null; }
                    public PrivateKey getPrivateKey(String alias) {
                        return cryptoProvider.getClientPrivateKey();
                    }
                    public String[] getServerAliases(String keyType, Principal[] issuers) { return null; }
                }
        };

        // Ignore differences between given hostname and certificate hostname
        HostnameVerifier hv = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) { return true; }
        };

        httpClient.setConnectionPool(new ConnectionPool(0, 0));
        httpClient.setHostnameVerifier(hv);
        httpClient.setConnectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);

        httpClientWithReadTimeout = httpClient.clone();
        httpClientWithReadTimeout.setReadTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public interface LimelightCryptoProvider {
        public X509Certificate getClientCertificate();
        public RSAPrivateKey getClientPrivateKey();
        public byte[] getPemEncodedClientCertificate();
        public String encodeBase64String(byte[] data);
    }
}