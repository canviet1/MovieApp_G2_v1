package com.example.g2_movieapp.activity;
import android.content.Context;

import com.example.g2_movieapp.BuildConfig;
import com.example.g2_movieapp.helper.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast; // Thêm import Toast

import androidx.appcompat.app.AppCompatActivity;



import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.g2_movieapp.R;
import com.example.g2_movieapp.helper.task.SessionRequest;
import com.example.g2_movieapp.helper.task.SessionResponse;
import com.example.g2_movieapp.helper.task.TokenResponse;
import com.example.g2_movieapp.interfaces.TMDBApi;

public class LoginActivity extends AppCompatActivity {

    private EditText userEdt, passEdt;
    private TextView registerBtn;
    private Button loginBtn;
    private ProgressBar loadingBar;
    private WebView webView;
    private String apiKey = BuildConfig.TMDB_API_KEY;
    private TMDBApi tmdbApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Kiểm tra session ID khi khởi chạy
        SharedPreferences prefs = getSharedPreferences(Constant.App.PREFS_SESSION, Context.MODE_PRIVATE);
        String sessionId = prefs.getString("session_id", null);

        if (sessionId != null) {
            // Nếu session ID tồn tại, chuyển tới MainActivity
            navigateToMainActivity();
            return;
        }

        // Nếu không có session ID, hiển thị giao diện đăng nhập
        setContentView(R.layout.activity_login);
        userEdt = findViewById(R.id.editUsername);
        passEdt = findViewById(R.id.editPassword);
        loginBtn = findViewById(R.id.btnLogin);
        registerBtn = findViewById(R.id.textRegisterNow);
        loadingBar = findViewById(R.id.loadingLogin);
        webView = findViewById(R.id.webView); // Thêm WebView vào layout

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        tmdbApi = retrofit.create(TMDBApi.class);

        loginBtn.setOnClickListener(v -> {
            String username = userEdt.getText().toString();
            String password = passEdt.getText().toString();
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập username và password!", Toast.LENGTH_SHORT).show();
            } else {
                getNewToken();
            }
        });
        registerBtn.setOnClickListener(v -> openRegistrationPage());
    }
    private void openRegistrationPage() {
        // Hiển thị trang đăng ký TMDB trong WebView
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);

        // Cài đặt WebView để xử lý các liên kết
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false; // Để WebView tự xử lý URL
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WebView", "Page loaded: " + url);
            }
        });

        // Tải trang đăng ký của TMDB
        webView.loadUrl("https://www.themoviedb.org/signup");
    }

    private void getNewToken() {
        loadingBar.setVisibility(View.VISIBLE);
        tmdbApi.getNewToken(apiKey).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                loadingBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getRequest_token();
                    // Store token in SharedPreferences if needed
                    saveToken(token);  // Method to save the token
                    loadAuthenticationPage(token);
                } else {
                    Toast.makeText(LoginActivity.this, "Không thể lấy token, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                loadingBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveToken(String token) {
        SharedPreferences prefs = getSharedPreferences(Constant.App.PREFS_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("request_token", token); // Or "access_token" if applicable
        editor.apply();
    }

    private String approvedToken = null;

    private void loadAuthenticationPage(String requestToken) {
        String url = "https://www.themoviedb.org/authenticate/" + requestToken +"?redirect_to=myapp://callback"; // Thêm tham số redirect_to

        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Chỉ log và không xử lý ở đây
                Log.d("WebView", "Redirecting to: " + url); // Ghi log URL để kiểm tra
                return false; // Để WebView tải URL
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Kiểm tra nếu URL có chứa myapp://callback
                if (url.contains("myapp://callback")) {
                    Uri uri = Uri.parse(url);
                    approvedToken = uri.getQueryParameter("request_token");
                    if (approvedToken != null) {
                        // Sau khi xác thực thành công, lưu token và ẩn WebView
                        webView.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Xác thực thành công, nhấn Back để hoàn tất.", Toast.LENGTH_SHORT).show();
                        createSession(approvedToken);
                    }
                }
            }
        });
        webView.loadUrl(url); // Tải URL
    }

    private void createSession(String requestToken) {
        loadingBar.setVisibility(View.VISIBLE);
        SessionRequest sessionRequest = new SessionRequest(requestToken);
        tmdbApi.createSession(apiKey, sessionRequest).enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(Call<SessionResponse> call, Response<SessionResponse> response) {
                loadingBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    String sessionId = response.body().getSessionId();

                    // Lưu sessionID vào SharedPreferences
                    SharedPreferences prefs = getSharedPreferences(Constant.App.PREFS_SESSION, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("session_id", sessionId);
                    editor.apply();

                    // Chuyển sang MainActivity
                    runOnUiThread(() -> navigateToMainActivity());
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, "Không thể tạo session, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onFailure(Call<SessionResponse> call, Throwable t) {
                loadingBar.setVisibility(View.GONE);
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}


