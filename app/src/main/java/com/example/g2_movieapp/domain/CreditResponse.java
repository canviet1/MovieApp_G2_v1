package com.example.g2_movieapp.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreditResponse {
    @SerializedName("cast")
    private List<Movie> cast;

    public List<Movie> getCast() {
        return cast;
    }

    public void setCast(List<Movie> cast) {
        this.cast = cast;
    }
}
