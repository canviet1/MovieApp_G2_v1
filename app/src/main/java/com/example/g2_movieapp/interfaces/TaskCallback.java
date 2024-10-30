package com.example.g2_movieapp.interfaces;


public interface TaskCallback {
    void onSuccess(String... params) ;
    void onLoginSuccess() ;
    void onFailure(String errorMess) ;
}
