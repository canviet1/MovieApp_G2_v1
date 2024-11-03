package com.example.g2_movieapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.example.g2_movieapp.R;
import com.example.g2_movieapp.adapter.KnownForMoviesAdapter;
import com.example.g2_movieapp.domain.CreditResponse;
import com.example.g2_movieapp.domain.Movie;
import com.example.g2_movieapp.helper.Constant;
import com.example.g2_movieapp.helper.GsonRequest;
import com.example.g2_movieapp.helper.RequestQueueSingleton;
import com.example.g2_movieapp.interfaces.TaskCallback;

import java.util.List;

public class ActorDetailActivity extends AppCompatActivity implements TaskCallback {
    private ImageView actorProfileImage;
    private TextView actorName, actorPopularity, knownForDepartment, adultContent;
    private RecyclerView knownForMoviesRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_detail);

        actorProfileImage = findViewById(R.id.actorProfileImageDetail);
        actorName = findViewById(R.id.actorNameDetail);
        actorPopularity = findViewById(R.id.actorPopularity);
        knownForDepartment = findViewById(R.id.knownForDepartment);
        adultContent = findViewById(R.id.adultContent);
        knownForMoviesRecyclerView = findViewById(R.id.knownForMoviesRecyclerView);
        knownForMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        int Id = intent.getIntExtra("actorId",23);
        String name = intent.getStringExtra("actorName");
        double popularity = intent.getDoubleExtra("actorPopularity", 0);
        String profilePath = intent.getStringExtra("actorProfilePath");
        String department = intent.getStringExtra("knownForDepartment");
        boolean isAdult = intent.getBooleanExtra("actorAdult", false);
        // ArrayList<Movie> knownForMovies = (ArrayList<Movie>) getIntent().getSerializableExtra("knownForMovies");

        actorName.setText(name);
        actorPopularity.setText(String.valueOf(popularity));
        knownForDepartment.setText(department);
        adultContent.setText(isAdult ? "Yes" : "No");
        Glide.with(this).load(Constant.API.IMAGE + profilePath).into(actorProfileImage);
        getPopularActors(Id+"");
    }
    private void getPopularActors(String Id) {
        String url = Constant.API.ACTORMOVIECREDIT+Id+"/movie_credits?api_key=5aa5ed76193d3d2a01f9f679885ec8d3";

        GsonRequest<CreditResponse> gsonRequest = new GsonRequest<>(
                this,
                Request.Method.GET,
                url,
                CreditResponse.class,
                null,
                response -> {

                    List<Movie> knownForMovies = response.getCast();
                    if (knownForMovies != null && !knownForMovies.isEmpty()) {
                        KnownForMoviesAdapter adapter = new KnownForMoviesAdapter(knownForMovies);
                        knownForMoviesRecyclerView.setAdapter(adapter);
                    }
                },
                error -> {
                    Toast.makeText(this, "Failed to load actors", Toast.LENGTH_SHORT).show();
                    Log.e("VOLLEY", "Error: " + error.getMessage());
                }
        );

        RequestQueueSingleton.getInstance(this).addToRequestQueue(gsonRequest);
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
