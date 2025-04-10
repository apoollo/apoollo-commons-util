package com.apoollo.commons.util.model;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisableSSLValidation {

    private static final Logger LOGGER = LoggerFactory.getLogger(DisableSSLValidation.class);

    class DisableValidationTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

    }

    /**
     * A {@link HostnameVerifier} that does not validate any hostnames.
     */
    class TrustAllHostnames implements HostnameVerifier {

        @Override
        public boolean verify(String s, SSLSession sslSession) {
            return true;
        }

    }

    public void apply(Consumer3<SSLSocketFactory, X509TrustManager, HostnameVerifier> consumer3) {
        try {
            X509TrustManager disabledTrustManager = new DisableValidationTrustManager();
            TrustManager[] trustManagers = new TrustManager[1];
            trustManagers[0] = disabledTrustManager;
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagers, new java.security.SecureRandom());
            SSLSocketFactory disabledSSLSocketFactory = sslContext.getSocketFactory();
            consumer3.accept(disabledSSLSocketFactory, disabledTrustManager, new TrustAllHostnames());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            LOGGER.warn("Error setting SSLSocketFactory in OKHttpClient", e);
        }
    }
}