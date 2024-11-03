package com.example.g2_movieapp.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.example.g2_movieapp.R;
import com.example.g2_movieapp.adapter.ActorAdapter;
import com.example.g2_movieapp.domain.Movie;
import com.example.g2_movieapp.domain.MovieCreditsResponse;
import com.example.g2_movieapp.domain.Response;
import com.example.g2_movieapp.helper.Constant;
import com.example.g2_movieapp.helper.GsonRequest;
import com.example.g2_movieapp.helper.RequestQueueSingleton;
import com.example.g2_movieapp.helper.Util;
import com.example.g2_movieapp.interfaces.OnHeartClickListener;
import com.example.g2_movieapp.interfaces.TaskCallback;
import com.google.android.material.imageview.ShapeableImageView;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;



public class DetailActivity extends AppCompatActivity implements OnHeartClickListener, TaskCallback {
    private RecyclerView.Adapter adapterActorList;
    private RecyclerView recyclerView;
    private ProgressBar loading;
    private TextView titleTxt, movieRateTxt, movieTimeTxt, movieDateTxt, movieSummaryInfo;
    private NestedScrollView scrollView;
    private int idMovie;
    private ShapeableImageView pic1;
    private ImageView pic2, backImg, ivAddfavorite, playbutton;
    private Context context;
    private TaskCallback taskCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        context = this;
        idMovie = getIntent().getIntExtra("id", 0);
        initView();
        sendRequest();
        getMovieActors();
        playbutton = findViewById(R.id.playbutton);
        playbutton.setOnClickListener(v -> {
            AddWatchList(idMovie, true);
            Intent intent = new Intent(DetailActivity.this, PlayMovieActivity.class);
            intent.putExtra("id", idMovie);
            startActivity(intent);
        });
        ivAddfavorite = findViewById(R.id.addfavorite);
        ivAddfavorite.setOnClickListener(v -> {
            onHeartClick(idMovie, true);
        });

        // Initialize the TaskCallback
        this.taskCallback = this;
    }

    private void sendRequest() {
        loading.setVisibility(View.VISIBLE);

        String url = Constant.API.GET_MOVIE + idMovie+"?api_key=5aa5ed76193d3d2a01f9f679885ec8d3";
        GsonRequest<Movie> gsonRequest = new GsonRequest<>(this, Request.Method.GET, url, Movie.class, null,
                response -> {
                    loading.setVisibility(View.GONE);
                    Movie movie = response;

                    Glide.with(this)
                            .load(Constant.API.IMAGE + movie.getPosterPath())
                            .into(pic1);
                    titleTxt.setText(movie.getTitle());
                    movieRateTxt.setText(String.valueOf(movie.getVoteAverage()));
                    movieTimeTxt.setText(String.valueOf(movie.getRunTime()));
                    movieDateTxt.setText(movie.getReleaseDate());
                    movieSummaryInfo.setText(movie.getOverview());
                },
                error -> {
                    loading.setVisibility(View.GONE);
                    Log.e("VOLLEY", "Error: " + error.getMessage());
                    Toast.makeText(context, "Failed to load movie details", Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueueSingleton.getInstance(this).addToRequestQueue(gsonRequest);
    }

    private void getMovieActors() {
        String url = Constant.API.GET_MOVIE + idMovie + "/credits?api_key=5aa5ed76193d3d2a01f9f679885ec8d3";

        GsonRequest<MovieCreditsResponse> gsonRequest = new GsonRequest<>(
                this,
                Request.Method.GET,
                url,
                MovieCreditsResponse.class,
                null,
                response -> {
                    List<MovieCreditsResponse.Cast> castList = response.getCast();
                    if (castList != null && !castList.isEmpty()) {
                        ActorAdapter actorAdapter = new ActorAdapter(castList);
                        recyclerView.setAdapter(actorAdapter);
                    }
                },
                error -> {
                    Toast.makeText(this, "Failed to load actors", Toast.LENGTH_SHORT).show();
                    Log.e("VOLLEY", "Error: " + error.getMessage());
                }
        );

        RequestQueueSingleton.getInstance(this).addToRequestQueue(gsonRequest);
    }

    private void initView() {
        titleTxt = findViewById(R.id.movieNameTxt);
        loading = findViewById(R.id.detailLoading);
        scrollView = findViewById(R.id.scrollView2);
        pic1 = findViewById(R.id.posterNormalImg);
        pic2 = findViewById(R.id.posterBigImg);
        movieRateTxt = findViewById(R.id.movieRateTxt);
        movieTimeTxt = findViewById(R.id.movieTimeTxt);
        movieDateTxt = findViewById(R.id.movieDateTxt);
        movieSummaryInfo = findViewById(R.id.movieSummaryInfo);
        backImg = findViewById(R.id.backImg);
        recyclerView = findViewById(R.id.movieActorInfo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        backImg.setOnClickListener(v -> finish());
    }

    public void AddWatchList(int idMovie, boolean watchlist) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("media_id", idMovie);
            jsonBody.put("media_type", "movie");
            jsonBody.put("watchlist", watchlist);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        SharedPreferences prefs = getSharedPreferences(Constant.App.PREFS_SESSION, Context.MODE_PRIVATE);
        String Se_id = prefs.getString("session_id","");
        final String requestBody = jsonBody.toString();
        GsonRequest<Response> gsonRequest = new GsonRequest<>(
                this,
                Request.Method.POST,
                Constant.API.ADD_WATCH_LIST+Se_id,
                Response.class,
                requestBody,
                response -> {
                    Log.i("VOLLEY", "Success");
                    if (watchlist) {
                        Toast.makeText(context, "Added to watch list", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Removed from watch list", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.i("VOLLEY", "Failure");
                    if (watchlist) {
                        Toast.makeText(context, "Failed to add to watch list", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to remove from watch list", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueueSingleton.getInstance(context).addToRequestQueue(gsonRequest);
    }

    @Override
    public void onHeartClick(int movieId, boolean fav) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("media_id", movieId);
            jsonBody.put("media_type", "movie");
            jsonBody.put("favorite", fav);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        final String requestBody = jsonBody.toString();

        GsonRequest<Response> gsonRequest = new GsonRequest<>(
                context,
                Request.Method.POST,
                Constant.API.postFavoriteMovieApi(context),
                Response.class,
                requestBody,
                response -> {
                    Log.i("VOLLEY", "thành công");
                    if (fav) {
                        FancyToast.makeText(context, "Added to favorites", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                    } else {
                        FancyToast.makeText(context, "Removed from favorites", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                    }
                    if (taskCallback != null) {
                        Util.saveFavoriteList(context, taskCallback);
                    }
                }, error -> {
            Log.i("VOLLEY", "thất bại");
            if (fav) {
                Toast.makeText(context, "Failed to add to favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueueSingleton.getInstance(context).addToRequestQueue(gsonRequest);
    }

    @Override
    public void onSuccess(String... params) {

    }

    @Override
    public void onLoginSuccess() {

    }

    @Override
    public void onFailure(String errorMess) {

    }
}
