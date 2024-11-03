package com.example.g2_movieapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.g2_movieapp.R;
import com.example.g2_movieapp.adapter.ActorDetailPopularAdapter;
import com.example.g2_movieapp.domain.MoviePopolarResponse;
import com.example.g2_movieapp.domain.credit.Actor;
import com.example.g2_movieapp.helper.Constant;
import com.example.g2_movieapp.helper.GsonRequest;
import com.example.g2_movieapp.helper.RequestQueueSingleton;
import com.example.g2_movieapp.interfaces.TaskCallback;

import java.util.List;

public class PopularActivity extends AppCompatActivity implements TaskCallback {
    private RecyclerView.Adapter adapterActorList;
    private RecyclerView recyclerView;
    private ProgressBar loading;
    private EditText etSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_popular);
        getPopularActors();
        recyclerView = findViewById(R.id.rvPopular);
        int spanCount = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
        loading = findViewById(R.id.loadingFav);
        etSearch = findViewById(R.id.editTextText);
        ActionBarActivity actionbar = new ActionBarActivity();
        actionbar.setupActionBar(this);

    }

    private void getPopularActors() {
        String url = Constant.API.POPULAR;

        GsonRequest<MoviePopolarResponse> gsonRequest = new GsonRequest<>(
                this,
                Request.Method.GET,
                url,
                MoviePopolarResponse.class,
                null,
                response -> {
                    // Lấy danh sách Actor từ phản hồi
                    List<Actor> actorList = response.getResults();
                    if (actorList != null && !actorList.isEmpty()) {
                        ActorDetailPopularAdapter actorAdapter = new ActorDetailPopularAdapter(actorList);
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