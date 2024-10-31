package com.example.g2_movieapp.activity;// In your MainActivity.java

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;



import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.example.g2_movieapp.R;
import com.example.g2_movieapp.adapter.MovieListAdapter;
import com.example.g2_movieapp.adapter.SliderAdapter;
import com.example.g2_movieapp.domain.MovieData;
import com.example.g2_movieapp.domain.user.User;
import com.example.g2_movieapp.helper.Constant;
import com.example.g2_movieapp.helper.GsonRequest;
import com.example.g2_movieapp.helper.RequestQueueSingleton;
import com.example.g2_movieapp.interfaces.TaskCallback;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements TaskCallback {
    private RecyclerView.Adapter adapterNewMovies, adapterUpCommingMovies;
    private RecyclerView recyclerViewNewMovies, recyclerViewUpCommingMovies;
    private ProgressBar loading1, loading2, loading3;
    private ViewPager2 viewPager2;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    TextView profileNameTxt, profileEmailTxt;
    ImageView profileImage;
    MaterialButton logout, watchlist, favoriteList;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_ui);

        initView();
        event();
        callApi();

        requestUserInformation();
        requestNowPlayingMovies();

        ActionBarActivity actionBar = new ActionBarActivity();
        actionBar.setupActionBar(this);
    }

    private void callApi() {
        requestPopularMovies();
        requestTopRatedMovies();
    }

    private void event() {
        logout.setOnClickListener(view -> logout());

        watchlist.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, WatchListActivity.class);
            MainActivity.this.startActivity(intent);
        });

        favoriteList.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, FavoriteList.class);
            MainActivity.this.startActivity(intent);
        });
    }

    private void requestPopularMovies() {
        loading1.setVisibility(View.VISIBLE);
        GsonRequest<MovieData> gsonRequest = new GsonRequest(this, Request.Method.GET, Constant.API.POPULAR_MOVIE_API, MovieData.class,
                response -> {
                    loading1.setVisibility(View.GONE);
                    TaskCallback taskCallback = this;
                    adapterNewMovies = new MovieListAdapter((MovieData) response, R.layout.viewholder_film, taskCallback);
                    recyclerViewNewMovies.setAdapter(adapterNewMovies);
                }, error -> loading1.setVisibility(View.GONE));

        RequestQueueSingleton.getInstance(this).addToRequestQueue(gsonRequest);
    }

    private void requestTopRatedMovies() {
        loading2.setVisibility(View.VISIBLE);
        GsonRequest<MovieData> gsonRequest = new GsonRequest(this, Request.Method.GET, Constant.API.TOP_RATED_MOVIE_API, MovieData.class,
                response -> {
                    Log.i("request api: ", "get api success");
                    loading2.setVisibility(View.GONE);
                    TaskCallback taskCallback = this;
                    adapterUpCommingMovies = new MovieListAdapter((MovieData) response, R.layout.viewholder_film, taskCallback);
                    recyclerViewUpCommingMovies.setAdapter(adapterUpCommingMovies);
                }, error -> {
            Log.i("request api error: ", error.getMessage());
            loading2.setVisibility(View.GONE);
        });

        RequestQueueSingleton.getInstance(this).addToRequestQueue(gsonRequest);
    }

    private void requestNowPlayingMovies() {
        loading3.setVisibility(View.VISIBLE);
        GsonRequest<MovieData> gsonRequest = new GsonRequest(this, Request.Method.GET, Constant.API.NOW_PLAYING_MOVIE_API, MovieData.class,
                response -> {
                    Log.i("request api: ", "get api success");
                    loading3.setVisibility(View.GONE);
                    viewPager2.setAdapter(new SliderAdapter((MovieData) response, viewPager2));
                }, error -> {
            Log.i("request api error: ", error.getMessage());
            loading3.setVisibility(View.GONE);
        });

        RequestQueueSingleton.getInstance(this).addToRequestQueue(gsonRequest);
    }

    private void initView() {
        recyclerViewNewMovies = findViewById(R.id.rvNewMovies);
        recyclerViewNewMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewUpCommingMovies = findViewById(R.id.rvCommingMovies);
        recyclerViewUpCommingMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        viewPager2 = findViewById(R.id.viewPager2SlidingMovies);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        loading1 = findViewById(R.id.loadingNewMovies);
        loading2 = findViewById(R.id.loadingCommingMovies);
        loading3 = findViewById(R.id.loadingSlidingMovies);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Set up the toolbar
        setSupportActionBar(toolbar);

        // Set up the drawer toggle
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.open,
                R.string.close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // NavigationView item click listener
        navigationView.setNavigationItemSelectedListener(item -> {
            // Handle navigation view item clicks here.
            // Update UI or navigate to different activities based on item clicked
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        View headerView = navigationView.getHeaderView(0);
        profileNameTxt = headerView.findViewById(R.id.username);
        profileImage = headerView.findViewById(R.id.avatar);
        profileEmailTxt = headerView.findViewById(R.id.name);
        logout = headerView.findViewById(R.id.logout_button);
        watchlist = headerView.findViewById(R.id.watch_list_button);
        favoriteList = headerView.findViewById(R.id.favorite_list_button);
    }

    public void logout() {
        SharedPreferences prefs = getSharedPreferences(Constant.App.PREFS_SESSION, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("session_id");
        editor.remove("access_token");
        editor.remove("user_id");
        editor.remove("username");
        editor.apply();

        Constant.TempUserData.USERNAME = "";

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void requestUserInformation() {
        //String url = Constant.TempUserData.USER_PROFILE;
        SharedPreferences prefs = getSharedPreferences(Constant.App.PREFS_SESSION, Context.MODE_PRIVATE);
        String Se_id = prefs.getString("session_id","");
        String url = Constant.API.USER_INFO+Se_id;
        GsonRequest<User> gsonRequest = new GsonRequest<>(
                this,
                Request.Method.GET,
                url,
                User.class,
                null,
                response -> {
                    User user = response;
                    Log.i("request api: ", "get api success");

                    profileNameTxt.setText(user.getUsername());
                    profileEmailTxt.setText(user.getName());
                    if (user.getAvatar().getTmdb().getAvatarPath() != null) {
                        Glide.with(MainActivity.this)
                                .load(Constant.API.IMAGE + response.getAvatar().getTmdb().getAvatarPath())
                                .into(profileImage);
                    } else {
                        String gravatarHash = user.getAvatar().getGravatar().getHash();
                        if (gravatarHash != null && !gravatarHash.isEmpty()) {
                            Glide.with(MainActivity.this)
                                    .load("https://gravatar.com/avatar/" + gravatarHash + "?f=y&d=identicon")
                                    .into(profileImage);
                        }
                    }

                    if (!Constant.TempUserData.USERNAME.isEmpty())
                        return;

                    Constant.TempUserData.USERNAME = user.getUsername();

                    FancyToast.makeText(MainActivity.this, "Xin chào " + Constant.TempUserData.USERNAME,
                            FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                },
                error -> {
                    Log.e("VOLLEY", "Error: " + error.getMessage());
                    FancyToast.makeText(this, "Failed to load movie detail!", FancyToast.LENGTH_LONG, FancyToast.ERROR, true);
                });

        RequestQueueSingleton.getInstance(this).addToRequestQueue(gsonRequest);
    }

    @Override
    public void onSuccess(String... params) {

    }

    @Override
    public void onLoginSuccess() {
        callApi();
    }

    @Override
    public void onFailure(String errorMess) {

    }

}
