package com.pz.zrobseliste.utils;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;


public class CustomHttpBuilder {

    public static OkHttpClient.Builder SSL() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        X509TrustManager TRUST_ALL_CERTS = new X509TrustManager() {
            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }
        };

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[] { TRUST_ALL_CERTS }, new java.security.SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        builder.sslSocketFactory(sslContext.getSocketFactory(), TRUST_ALL_CERTS);
        builder.hostnameVerifier((hostname, session) -> true);

        return builder;
    }
}