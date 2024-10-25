package id.vn.minhlamdev.movieapp.domain;

import com.google.gson.annotations.SerializedName;

public class RequestToken {
    @SerializedName("success")
    private boolean success;

    @SerializedName("request_token")
    private String guestSessionId;

    @SerializedName("expires_at")
    private String expiresAt;

    // Getters and setters
    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getGuestSessionId() {
        return guestSessionId;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    // Setters
    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setGuestSessionId(String guestSessionId) {
        this.guestSessionId = guestSessionId;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }
}
