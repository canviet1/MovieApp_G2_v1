package com.example.g2_movieapp.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.example.g2_movieapp.domain.Movie;
import com.example.g2_movieapp.domain.MovieData;
import com.example.g2_movieapp.interfaces.TaskCallback;

public class Util {
    public static void saveFavoriteList(Context context, TaskCallback taskCallback) {
        GsonRequest<MovieData> gsonRequest = new GsonRequest(
                context,
                Request.Method.GET,
                Constant.API.getFavoriteMoviesApi(context),
                MovieData.class,
                response -> {
                    StringBuilder favoriteMovie = new StringBuilder();
                    MovieData movieData = (MovieData) response;
                    for (Movie movie :
                            movieData.getResults()) {
                        favoriteMovie.append(movie.getId() + ",");
                    }

                    SharedPreferences prefs = context.getSharedPreferences(Constant.App.PREFS_SESSION, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("favorite_movie_list", favoriteMovie.toString());
                    editor.apply();

                    taskCallback.onLoginSuccess();

//                    Intent intent = new Intent(fromCtx, clazz);
//                    startActivity(intent);
//                    taskCallback.onSuccess();

                }, error -> {
            Log.i("request api error: ", error.getMessage());
        });

        RequestQueueSingleton.getInstance(context).addToRequestQueue(gsonRequest);
    }
}
