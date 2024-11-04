package com.example.g2_movieapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.g2_movieapp.BuildConfig;

import java.util.ArrayList;
import java.util.List;

public class Constant {
    private static String api_key = BuildConfig.TMDB_API_KEY;
    public static class API {
        public static final String NOW_PLAYING_MOVIE_API = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + api_key + "&language=en-US&page=1";
        public static final String POPULAR_MOVIE_API = "https://api.themoviedb.org/3/movie/popular?api_key=" + api_key + "&language=en-US&page=1";
        public static final String TOP_RATED_MOVIE_API = "https://api.themoviedb.org/3/movie/top_rated?api_key=" + api_key + "&language=en-US&page=1";
        //public static final String UPCOMMING_MOVIE_LIST = "https://api.themoviedb.org/3/movie/upcoming?api_key=" + api_key + "&language=en-US&page=1";
        public static final String IMAGE = "https://image.tmdb.org/t/p/w500/";
        public static final String SEARCH_MOVIE = "https://api.themoviedb.org/3/search/movie";
        public static final String POPULAR = "https://api.themoviedb.org/3/person/popular?api_key=" + api_key + "&language=en-US&page=1";
        public static final String ACTORMOVIECREDIT ="https://api.themoviedb.org/3/person/";
        public static String getFavoriteMoviesApi(Context context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(App.PREFS_SESSION, Context.MODE_PRIVATE);
            String Se_id = sharedPreferences.getString("session_id", "");
            return "https://api.themoviedb.org/3/account/null/favorite/movies?api_key=" + api_key + "&session_id="+Se_id;
        }

        public static String postFavoriteMovieApi(Context context) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(App.PREFS_SESSION, Context.MODE_PRIVATE);
            String Se_id = sharedPreferences.getString("session_id", "");
            return "https://api.themoviedb.org/3/account/null/favorite?api_key=" + api_key + "&session_id="+Se_id;
        }

        public static final String GET_MOVIE = "https://api.themoviedb.org/3/movie/";

        public static String GET_MOVIE_VIDEOS(int movieId) {
            return "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + api_key + "&language=en-US";
        }

        public static final String GET_RECOMMENDATIONS = "https://api.themoviedb.org/3/movie/{id}/recommendations?api_key=" + api_key + "&session_id=";
        public static final String WATCH_LIST = "https://api.themoviedb.org/3/account/null/watchlist/movies?api_key=" + api_key + "&language=en-US&page=1&session_id=";
        public static final String ADD_WATCH_LIST = "https://api.themoviedb.org/3/account/null/watchlist?api_key=" + api_key + "&session_id=";
        //        public static final String SESSION_WITH_LOGIN = "https://api.themoviedb.org/3/authentication/token/validate_with_login";
        public static final String DELETE_SESSION = "https://api.themoviedb.org/3/authentication/session?api_key=" + api_key + "";
        public static final String USER_INFO = "https://api.themoviedb.org/3/account/null?api_key=" + api_key + "&session_id=";

    }

    public static class TempUserData {
        //     public static String ACCESS_TOKEN = "";
        //     public static final String NAM_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiOGVkZjBlZjRhNjNmZjMyYTAxM2ZjMWNmMzM2ZmMzNSIsInN1YiI6IjY2NjZiY2M5OTFkMDhmZmQ4NWQzYzI2YSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.IRXwjKnKj2BUAwTvYp7TUl0ZE3Uxg7r69FN88mdhono";
        public static final String USER_PROFILE = "https://api.themoviedb.org/3/account/21576509?api_key=" + api_key + "";
        public static String USERNAME = "";
    }

    public static class App {
        public static final String PREFS_SESSION = "session_string";
    }
}
