package com.example.g2_movieapp.interfaces;


import okhttp3.OkHttpClient;

public interface CsrfTokenCallback {
    void onCsrfTokenReceived(String csrfToken, OkHttpClient client);
}
