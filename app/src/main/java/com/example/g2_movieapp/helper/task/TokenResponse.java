package com.example.g2_movieapp.helper.task;

import com.google.gson.annotations.SerializedName;

public class TokenResponse {
    @SerializedName("request_token")
    private String request_token;

    public String getRequest_token() {
        return request_token;
    }

    public void setRequest_token(String request_token) {
        this.request_token = request_token;
    }
}
