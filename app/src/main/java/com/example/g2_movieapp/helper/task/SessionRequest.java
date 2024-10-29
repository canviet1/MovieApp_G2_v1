package com.example.g2_movieapp.helper.task;

import com.google.gson.annotations.SerializedName;

public class SessionRequest {
    @SerializedName("request_token")
    private String requestToken;

    public SessionRequest(String requestToken) {
        this.requestToken = requestToken;
    }

}
