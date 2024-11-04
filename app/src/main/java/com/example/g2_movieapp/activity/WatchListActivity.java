package com.example.g2_movieapp.activity;

// Import các thư viện cần thiết
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

// Định nghĩa lớp WatchListActivity kế thừa từ AppCompatActivity và triển khai giao diện TaskCallback
public class WatchListActivity extends AppCompatActivity implements TaskCallback {

    // Khai báo các thành phần giao diện và dữ liệu
    private MovieListAdapter adapterWatchList;
    private RecyclerView recyclerViewWatchList;
    private ProgressBar loading;
    private EditText etSearch;
    private MovieData watchListMovies;

    // Phương thức onCreate được gọi khi Activity được khởi tạo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_list);

        initView(); // Khởi tạo các thành phần giao diện
        sendRequest(); // Gửi yêu cầu API để lấy danh sách phim
        event(); // Đăng ký sự kiện
    }

    // Phương thức sendRequest gửi yêu cầu API để lấy danh sách phim yêu thích của người dùng
    private void sendRequest() {
        loading.setVisibility(View.VISIBLE); // Hiển thị thanh tiến trình
        SharedPreferences prefs = getSharedPreferences(Constant.App.PREFS_SESSION, Context.MODE_PRIVATE);
        String Se_id = prefs.getString("session_id",""); // Lấy session ID từ SharedPreferences
        // Khởi tạo yêu cầu API sử dụng GsonRequest để lấy dữ liệu phim từ API
        GsonRequest<MovieData> gsonRequest = new GsonRequest(this, Request.Method.GET, Constant.API.WATCH_LIST+Se_id, MovieData.class,
                response -> {
                    Log.i("request api: ", "get api success"); // Log khi thành công
                    loading.setVisibility(View.GONE); // Ẩn thanh tiến trình
                    watchListMovies = (MovieData) response; // Lưu trữ dữ liệu phim
                    TaskCallback taskCallback = this;
                    // Khởi tạo adapter và thiết lập cho RecyclerView
                    adapterWatchList = new MovieListAdapter(watchListMovies, R.layout.viewholder_film, taskCallback);
                    recyclerViewWatchList.setAdapter(adapterWatchList);
                }, error -> {
            Log.i("request api error: ", error.getMessage()); // Log khi có lỗi
            loading.setVisibility(View.GONE); // Ẩn thanh tiến trình khi có lỗi
        });

        RequestQueueSingleton.getInstance(this).addToRequestQueue(gsonRequest); // Thêm yêu cầu vào hàng đợi
    }

    // Phương thức event xử lý sự kiện tìm kiếm khi nhấn Enter trong ô tìm kiếm
    private void event() {
        etSearch.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                String searchString = etSearch.getText().toString(); // Lấy chuỗi tìm kiếm từ EditText
                filterWatchList(searchString); // Gọi hàm lọc danh sách phim
            }
            return false;
        });
    }

    // Phương thức filterWatchList lọc danh sách phim theo từ khóa tìm kiếm
    private void filterWatchList(String query) {
        if (watchListMovies == null || watchListMovies.getResults() == null) return;

        MovieData filteredMovies = new MovieData(); // Tạo danh sách phim mới chứa kết quả tìm kiếm
        filteredMovies.setResults(new ArrayList<>()); // Khởi tạo danh sách rỗng cho kết quả

        // Duyệt qua từng phim trong danh sách và kiểm tra nếu tiêu đề phim chứa từ khóa
        for (Movie movie : watchListMovies.getResults()) {
            if (movie.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredMovies.getResults().add(movie); // Thêm phim vào danh sách kết quả nếu có chứa từ khóa
            }
        }

        adapterWatchList.updateData(filteredMovies); // Cập nhật dữ liệu trong adapter
    }

    // Phương thức initView khởi tạo các thành phần giao diện
    private void initView() {
        recyclerViewWatchList = findViewById(R.id.rvWatchlist); // Ánh xạ RecyclerView
        recyclerViewWatchList.setLayoutManager(new GridLayoutManager(this, 3)); // Thiết lập lưới hiển thị với 3 cột

        loading = findViewById(R.id.loadingWatchlist); // Ánh xạ thanh tiến trình
        etSearch = findViewById(R.id.editTextSearch); // Ánh xạ ô tìm kiếm

        ActionBarActivity actionbar = new ActionBarActivity();
        actionbar.setupActionBar(this); // Thiết lập thanh công cụ
    }

    // Các phương thức được triển khai từ TaskCallback
    @Override
    public void onSuccess(String... params) {
        // Phương thức được gọi khi một tác vụ thành công (hiện tại chưa có logic thực hiện)
    }

    @Override
    public void onLoginSuccess() {
        sendRequest(); // Gửi yêu cầu khi đăng nhập thành công
    }

    @Override
    public void onFailure(String errorMess) {
        // Phương thức được gọi khi một tác vụ thất bại (hiện tại chưa có logic thực hiện)
    }
}
