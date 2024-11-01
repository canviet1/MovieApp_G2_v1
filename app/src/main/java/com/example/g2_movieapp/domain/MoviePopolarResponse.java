package com.example.g2_movieapp.domain;

import com.google.gson.annotations.SerializedName;
import com.example.g2_movieapp.domain.credit.Actor;
import java.util.List;

public class MoviePopolarResponse {
    @SerializedName("results")
    private List<Actor> results;

    public List<Actor> getResults() {
        return results;
    }

    public void setResults(List<Actor> results) {
        this.results = results;
    }
}
