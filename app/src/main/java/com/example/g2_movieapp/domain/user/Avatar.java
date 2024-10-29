package com.example.g2_movieapp.domain.user;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Avatar {

    @SerializedName("gravatar")
    @Expose
    private Gravatar gravatar;
    @SerializedName("tmdb")
    @Expose
    private Tmdb tmdb;

    public Gravatar getGravatar() {
        return gravatar;
    }

    public void setGravatar(Gravatar gravatar) {
        this.gravatar = gravatar;
    }

    public Tmdb getTmdb() {
        return tmdb;
    }

    public void setTmdb(Tmdb tmdb) {
        this.tmdb = tmdb;
    }

}
