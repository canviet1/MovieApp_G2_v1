package com.example.g2_movieapp.interfaces;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TMDBApi {
    @GET("authentication/token/new") // Phương thức để lấy token mới
    Call<TokenResponse> getNewToken(@Query("api_key") String apiKey);

    @POST("authentication/token/validate_with_login") // Phương thức để xác thực token
    Call<ValidateTokenResponse> validateToken(@Query("api_key") String apiKey, @Body LoginRequest loginRequest);

    @POST("authentication/session/new") // Phương thức để tạo session
    Call<SessionResponse> createSession(@Query("api_key") String apiKey, @Body SessionRequest sessionRequest);

}
