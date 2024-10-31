package com.example.g2_movieapp.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;

import com.example.g2_movieapp.R;
import com.example.g2_movieapp.adapter.SearchMovieAdapter;
import com.example.g2_movieapp.domain.MovieData;
import com.example.g2_movieapp.helper.Constant;
import com.example.g2_movieapp.helper.GsonRequest;
import com.example.g2_movieapp.helper.RequestQueueSingleton;


public class SearchActivity extends AppCompatActivity {
    private EditText etSearch;

    private RecyclerView.Adapter adapterSearchMovie;
    private RecyclerView recyclerViewSearchMovie;
    private ProgressBar loadingSearchMovie;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        init();
        event();
    }

    private void sendRequest(String searchString) {
        loadingSearchMovie.setVisibility(View.VISIBLE);
        String queryUrl = Constant.API.SEARCH_MOVIE + "?query=" + searchString+"&api_key=5aa5ed76193d3d2a01f9f679885ec8d3&language=en-US";

        GsonRequest<MovieData> gsonRequest = new GsonRequest(this, Request.Method.GET, queryUrl, MovieData.class,
                response -> {
                    loadingSearchMovie.setVisibility(View.GONE);
                    adapterSearchMovie = new SearchMovieAdapter((MovieData) response);
                    recyclerViewSearchMovie.setAdapter(adapterSearchMovie);
                }, error -> {
            loadingSearchMovie.setVisibility(View.GONE);
        });

        RequestQueueSingleton.getInstance(this).addToRequestQueue(gsonRequest);
    }

    private void event() {
        etSearch.setOnKeyListener((v, keyCode, event) -> {

            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                String searchString = etSearch.getText().toString();
                sendRequest(searchString);
            }

            return true;
        });
    }

    private void init() {
        etSearch = findViewById(R.id.etSearch);

        recyclerViewSearchMovie = findViewById(R.id.rvSearchedMovies);
        recyclerViewSearchMovie.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        loadingSearchMovie = findViewById(R.id.loadingSearchMovie);

        ActionBarActivity actionBar = new ActionBarActivity();
        actionBar.setupActionBar(this);
    }
}

