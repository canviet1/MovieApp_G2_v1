package com.example.g2_movieapp.helper.task;

import com.google.gson.annotations.SerializedName;

public class SessionResponse {
    @SerializedName("session_id")
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
