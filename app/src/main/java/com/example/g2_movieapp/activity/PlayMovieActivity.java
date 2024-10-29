package com.example.g2_movieapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.g2_movieapp.R;
import com.example.g2_movieapp.adapter.MovieListAdapter;
import com.example.g2_movieapp.domain.Movie;
import com.example.g2_movieapp.domain.MovieData;
import com.example.g2_movieapp.domain.Response;
import com.example.g2_movieapp.domain.TrailerResponse;
import com.example.g2_movieapp.helper.Constant;
import com.example.g2_movieapp.helper.GsonRequest;
import com.example.g2_movieapp.helper.RequestQueueSingleton;
import com.example.g2_movieapp.interfaces.OnHeartClickListener;

import org.json.JSONException;
import org.json.JSONObject;


public class PlayMovieActivity extends AppCompatActivity implements OnHeartClickListener {
    private WebView webView;
    private RecyclerView.Adapter adapter;
    private ImageView backImg, ivAddfavorite;
    private Context context;
    private int idMovie;
    private ProgressBar loading;
    private RecyclerView recyclerView;
    private FrameLayout mFullscreenContainer;
    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private int mOriginalOrientation;
    private int mOriginalSystemUiVisibility;
    private TextView movieName;
    private TextView description;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);
        context = this;
        webView = findViewById(R.id.playerView);
        idMovie = getIntent().getIntExtra("id", 0);
        initView();
        sendRequest();
        sendRequest1();
        sendRequest2();

        ivAddfavorite = findViewById(R.id.addfavorite);
        ivAddfavorite.setOnClickListener(v -> onHeartClick(idMovie, true));

        backImg = findViewById(R.id.backImg);
        backImg.setOnClickListener(v -> finish());
    }

    private void initView() {
        recyclerView = findViewById(R.id.rvRecommendationMovies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        movieName = findViewById(R.id.movieName);
        description = findViewById(R.id.description);
        loading = findViewById(R.id.loadingRecommendation);
    }
    private void sendRequest2() {
        loading.setVisibility(View.VISIBLE);

        String url = Constant.API.GET_MOVIE + idMovie+"?api_key=5aa5ed76193d3d2a01f9f679885ec8d3&language=en-US&page=1";
        GsonRequest<Movie> gsonRequest = new GsonRequest<>(this, Request.Method.GET,url,Movie.class,null,
                response -> {
                    loading.setVisibility(View.GONE);
                    Movie movie = response;

                    movieName.setText(movie.getTitle());
                    description.setText(movie.getOverview());
                },
                error -> {
                    loading.setVisibility(View.GONE);
                    Log.e("VOLLEY", "Error: " + error.getMessage());
                    Toast.makeText(context, "Failed to load movie details", Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueueSingleton.getInstance(this).addToRequestQueue(gsonRequest);
    }
    private void sendRequest1() {
        loading.setVisibility(View.VISIBLE);
        SharedPreferences prefs = getSharedPreferences(Constant.App.PREFS_SESSION, Context.MODE_PRIVATE);
        String Se_id = prefs.getString("session_id","");
        String url = Constant.API.GET_RECOMMENDATIONS.replace("{id}", String.valueOf(idMovie))+Se_id;

        GsonRequest<MovieData> gsonRequest = new GsonRequest(this, Request.Method.GET, url, MovieData.class,
                response -> {
                    Log.i("request api: ", "get api success");
                    loading.setVisibility(View.GONE);
                    adapter = new MovieListAdapter((MovieData) response, R.layout.viewholder_film, null);
                    recyclerView.setAdapter(adapter);
                }, error -> {
            Log.i("request api error: ", error.getMessage());
            loading.setVisibility(View.GONE);
        });

        RequestQueueSingleton.getInstance(this).addToRequestQueue(gsonRequest);
    }

    private void sendRequest() {
        String url = Constant.API.GET_MOVIE_VIDEOS(idMovie);
        GsonRequest<TrailerResponse> gsonRequest = new GsonRequest<>(
                this,
                Request.Method.GET,
                url,
                TrailerResponse.class,
                null,
                response -> {
                    TrailerResponse.Trailer selectTrailer = response.getResults().get(0);
                    playMovie(selectTrailer.getKey());
                },
                error -> {
                    Log.e("VOLLEY", "Error: " + error.getMessage());
                    Toast.makeText(this, "Failed to load movie video", Toast.LENGTH_SHORT).show();
                }
        );
        RequestQueueSingleton.getInstance(this).addToRequestQueue(gsonRequest);
    }

    private void playMovie(String movieKey) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                if (mCustomView != null) {
                    callback.onCustomViewHidden();
                    return;
                }
                mCustomView = view;
                mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
                mOriginalOrientation = getRequestedOrientation();
                mCustomViewCallback = callback;

                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

                FrameLayout decor = (FrameLayout) getWindow().getDecorView();
                mFullscreenContainer = new FrameLayout(PlayMovieActivity.this);
                mFullscreenContainer.setBackgroundColor(Color.BLACK);
                mFullscreenContainer.addView(mCustomView, new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                decor.addView(mFullscreenContainer, new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));


            }


            @Override
            public void onHideCustomView() {
                if (mCustomView == null)
                    return;

                FrameLayout decor = (FrameLayout) getWindow().getDecorView();
                decor.removeView(mFullscreenContainer);
                mFullscreenContainer = null;
                mCustomView = null;
                getWindow().getDecorView().setSystemUiVisibility(mOriginalSystemUiVisibility);
                setRequestedOrientation(mOriginalOrientation);
                mCustomViewCallback.onCustomViewHidden();
                mCustomViewCallback = null;

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });

        String html = getHtml(movieKey);
        webView.loadData(html, "text/html", "utf-8");
    }

    private String getHtml(String videoId) {
        return "<html><body>" +
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videoId + "\" frameborder=\"0\" allowfullscreen></iframe>" +
                "</body></html>";
    }

    @Override
    public void onHeartClick(int idMovie, boolean fav) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("media_id", idMovie);
            jsonBody.put("media_type", "movie");
            jsonBody.put("favorite", fav);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        final String requestBody = jsonBody.toString();
        GsonRequest<Response> gsonRequest = new GsonRequest<>(
                this,
                Request.Method.POST,
                Constant.API.postFavoriteMovieApi(this),
                Response.class,
                requestBody,
                response -> {
                    Log.i("VOLLEY", "Success");
                    if (fav) {
                        Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.i("VOLLEY", "Failure");
                    if (fav) {
                        Toast.makeText(context, "Failed to add to favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueueSingleton.getInstance(context).addToRequestQueue(gsonRequest);
    }
}
