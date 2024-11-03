package com.example.g2_movieapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.example.g2_movieapp.R;
import com.example.g2_movieapp.adapter.MovieListAdapter;
import com.example.g2_movieapp.domain.Movie;
import com.example.g2_movieapp.domain.MovieData;
import com.example.g2_movieapp.helper.Constant;
import com.example.g2_movieapp.helper.GsonRequest;
import com.example.g2_movieapp.helper.RequestQueueSingleton;
import com.example.g2_movieapp.interfaces.TaskCallback;

import java.util.ArrayList;



public class FavoriteList extends AppCompatActivity implements TaskCallback {
    private MovieListAdapter adapterFavoriteList;
    private RecyclerView recyclerViewFavoriteList;
    private ProgressBar loading;
    private EditText etSearch;
    private MovieData favoriteMovies;
    private Button btn_po;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
        initView();
        sendRequest();
        event();
        btn_po = findViewById(R.id.btn_popular);
        btn_po.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FavoriteList.this, PopularActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void sendRequest() {
        loading.setVisibility(View.VISIBLE);

        GsonRequest<MovieData> gsonRequest = new GsonRequest<>(this, Request.Method.GET, Constant.API.getFavoriteMoviesApi(this), MovieData.class,
                response -> {
                    Log.i("request api: ", "get api success");
                    loading.setVisibility(View.GONE);
                    favoriteMovies = response;
                    adapterFavoriteList = new MovieListAdapter(favoriteMovies, R.layout.viewholder_film, this);
                    recyclerViewFavoriteList.setAdapter(adapterFavoriteList);
                }, error -> {
            Log.i("request api error: ", error.getMessage());
            loading.setVisibility(View.GONE);
        });

        RequestQueueSingleton.getInstance(this).addToRequestQueue(gsonRequest);
    }

    private void event() {
        etSearch.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                String searchString = etSearch.getText().toString();
                filterFavorites(searchString);
            }
            return false;
        });
    }

    private void filterFavorites(String query) {
        if (favoriteMovies == null || favoriteMovies.getResults() == null) return;

        MovieData filteredMovies = new MovieData();
        filteredMovies.setResults(new ArrayList<>());

        for (Movie movie : favoriteMovies.getResults()) {
            if (movie.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredMovies.getResults().add(movie);
            }
        }

        adapterFavoriteList.updateData(filteredMovies);
    }

    private void initView() {
        recyclerViewFavoriteList = findViewById(R.id.rvFav);
        recyclerViewFavoriteList.setLayoutManager(new GridLayoutManager(this, 3));

        loading = findViewById(R.id.loadingFav);
        etSearch = findViewById(R.id.editTextText);

        ActionBarActivity actionbar = new ActionBarActivity();
        actionbar.setupActionBar(this);
    }

    @Override
    public void onSuccess(String... params) {
        // Implementation if needed
    }

    @Override
    public void onLoginSuccess() {
        sendRequest();
    }

    @Override
    public void onFailure(String errorMess) {
        // Implementation if needed
    }
}