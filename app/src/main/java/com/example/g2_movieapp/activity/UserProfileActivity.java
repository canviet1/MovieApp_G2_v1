package com.example.g2_movieapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.example.g2_movieapp.R;
import com.example.g2_movieapp.domain.user.User;
import com.example.g2_movieapp.helper.Constant;
import com.example.g2_movieapp.helper.GsonRequest;
import com.example.g2_movieapp.helper.RequestQueueSingleton;
import com.google.android.material.button.MaterialButton;


public class UserProfileActivity extends AppCompatActivity {

    TextView profileNameTxt;
    ImageView profileImage;
    MaterialButton logout;
    Context context;
    MaterialButton watchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_ui);

        requestUserInformation();
        initView();
    }
    public void initView() {
        profileNameTxt = findViewById(R.id.username);
        profileImage = findViewById(R.id.avatar);
        logout = findViewById(R.id.logout_button);
        //logout
        logout.setOnClickListener(view -> logout());

        watchlist = findViewById(R.id.watch_list_button);
        watchlist.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, WatchListActivity.class);
            startActivity(intent);
        });
    }
    public void requestUserInformation() {
        String url = Constant.TempUserData.USER_PROFILE;
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
                    if (user.getAvatar() != null && user.getAvatar().getTmdb() != null && user.getAvatar().getTmdb().getAvatarPath() != null) {
                        String avatarUrl = Constant.API.IMAGE + user.getAvatar().getTmdb().getAvatarPath();
                        Log.i("Avatar URL", avatarUrl); // Kiểm tra URL
                        Glide.with(UserProfileActivity.this)
                                .load(avatarUrl)
                                .error(R.drawable.account)
                                .into(profileImage);
                    } else {
                        Log.i("Avatar Info", "No avatar path available");
                    }

                },
                error -> {
                    Log.e("VOLLEY", "Error: " + error.getMessage());
                    Toast.makeText(context, "Failed to load movie details", Toast.LENGTH_SHORT).show();
                });

        RequestQueueSingleton.getInstance(this).addToRequestQueue(gsonRequest);
    }

    public void logout() {
        // Gọi hàm xóa session trên TMDB trước khi đăng xuất
        deleteTmdbSession();

        // Xóa session trong ứng dụng
        SharedPreferences prefs = getSharedPreferences(Constant.App.PREFS_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("session_id");
        editor.remove("access_token");
        editor.remove("user_id");
        editor.apply();

        // Chuyển hướng về trang đăng nhập
        Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    // xóa session trên TMDB
    private void deleteTmdbSession() {
        SharedPreferences prefs = getSharedPreferences(Constant.App.PREFS_SESSION, Context.MODE_PRIVATE);
        String sessionId = prefs.getString("session_id", null);

        if (sessionId != null) {
            String url = Constant.API.DELETE_SESSION + "?session_id=" + sessionId;
            GsonRequest<Void> deleteSessionRequest = new GsonRequest<>(
                    this,
                    Request.Method.DELETE,
                    url,
                    Void.class,
                    null,
                    response -> Log.i("TMDB Logout", "Session deleted successfully"),
                    error -> Log.e("TMDB Logout Error", "Failed to delete session on TMDB: " + error.getMessage())
            );
            RequestQueueSingleton.getInstance(this).addToRequestQueue(deleteSessionRequest);
        }
    }
}
