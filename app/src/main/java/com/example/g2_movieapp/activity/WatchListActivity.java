package com.example.g2_movieapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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


public class WatchListActivity extends AppCompatActivity implements TaskCallback {
    private MovieListAdapter adapterWatchList;
    private RecyclerView recyclerViewWatchList;
    private ProgressBar loading;
    private EditText etSearch;
    private MovieData watchListMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);

        initView();
        sendRequest();
        event();
    }

    private void sendRequest() {
        loading.setVisibility(View.VISIBLE);
        SharedPreferences prefs = getSharedPreferences(Constant.App.PREFS_SESSION, Context.MODE_PRIVATE);
        String Se_id = prefs.getString("session_id","");
        GsonRequest<MovieData> gsonRequest = new GsonRequest(this, Request.Method.GET, Constant.API.WATCH_LIST+Se_id, MovieData.class,
                response -> {
                    Log.i("request api: ", "get api success");
                    loading.setVisibility(View.GONE);
                    TaskCallback taskCallback = this;
                    adapterWatchList = new MovieListAdapter((MovieData) response, R.layout.viewholder_film, taskCallback);
                    recyclerViewWatchList.setAdapter(adapterWatchList);
                }, error -> {
            Log.i("request api error: ", error.getMessage());
            loading.setVisibility(View.GONE);
        });

        RequestQueueSingleton.getInstance(this).addToRequestQueue(gsonRequest);
    }

    private void event() {
        etSearch.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                String searchString = etSearch.getText().toString();
                filterWatchList(searchString);
            }
            return false;
        });
    }

    private void filterWatchList(String query) {
        if (watchListMovies == null || watchListMovies.getResults() == null) return;

        MovieData filteredMovies = new MovieData();
        filteredMovies.setResults(new ArrayList<>());

        for (Movie movie : watchListMovies.getResults()) {
            if (movie.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredMovies.getResults().add(movie);
            }
        }

        adapterWatchList.updateData(filteredMovies);
    }

    private void initView() {
        recyclerViewWatchList = findViewById(R.id.rvWatchlist);
        recyclerViewWatchList.setLayoutManager(new GridLayoutManager(this, 3));

        loading = findViewById(R.id.loadingWatchlist);
        etSearch = findViewById(R.id.editTextSearch);

        ActionBarActivity actionbar = new ActionBarActivity();
        actionbar.setupActionBar(this);
    }

    @Override
    public void onSuccess(String... params) {

    }

    @Override
    public void onLoginSuccess() {
        sendRequest();
    }

    @Override
    public void onFailure(String errorMess) {

    }
}
