package id.vn.minhlamdev.movieapp.domain;

public class Response {
    private boolean success;
    private int statusCode;
    private String statusMessage;

    public Response(boolean success, int statusCode, String statusMessage) {
        this.success = success;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
