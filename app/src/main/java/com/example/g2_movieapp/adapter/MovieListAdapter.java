package com.example.g2_movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.example.g2_movieapp.R;
import com.example.g2_movieapp.activity.DetailActivity;
import com.example.g2_movieapp.domain.Movie;
import com.example.g2_movieapp.domain.MovieData;
import com.example.g2_movieapp.domain.Response;
import com.example.g2_movieapp.helper.Constant;
import com.example.g2_movieapp.helper.GsonRequest;
import com.example.g2_movieapp.helper.RequestQueueSingleton;
import com.example.g2_movieapp.helper.Util;
import com.example.g2_movieapp.interfaces.OnHeartClickListener;
import com.example.g2_movieapp.interfaces.TaskCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.json.JSONException;
import org.json.JSONObject;


public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> implements OnHeartClickListener {

    private MovieData items;
    private Context context;
    private int layoutResourceId;
    private String favoriteMovieString;
    private TaskCallback taskCallback;

    public MovieListAdapter(MovieData items, int layoutResourceId, @Nullable TaskCallback taskCallback) {
        this.items = items;
        this.layoutResourceId = layoutResourceId;
        this.taskCallback = taskCallback;
    }
    public void updateData(MovieData newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    public MovieListAdapter(MovieData items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(layoutResourceId, parent, false);
        context = parent.getContext();
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = items.getResults().get(position);
        String movieId = movie.getId().toString();

        holder.idTxt.setText(movieId); //setText
        holder.titleTxt.setText(movie.getTitle());
        holder.scoreTxt.setText(movie.getVoteAverage() == null ? "" : movie.getVoteAverage().toString());

        Glide.with(holder.itemView.getContext())
                .load(Constant.API.IMAGE + movie.getPosterPath())
                .into(holder.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra("id", movie.getId());
            holder.itemView.getContext().startActivity(intent);
        });

        SharedPreferences prefs = context.getSharedPreferences(Constant.App.PREFS_SESSION, Context.MODE_PRIVATE);
        favoriteMovieString = prefs.getString("favorite_movie_list", "");

        boolean isFav = false;
        String[] favoriteMovieIds = favoriteMovieString.split(",");
        for (String id :
                favoriteMovieIds) {
            if (id.equals(movieId)) {
                isFav = true;
            }
        }

        holder.ivAddfavorite.setOnClickListener(v -> {
            TextView favoriteStatus = holder.favoriteStatus;
            boolean fav = favoriteStatus.getText().toString().equals("true");
            favoriteStatus.setText(fav ? "false" : "true");
            onHeartClick(movie.getId(), !fav);
        });

        if (isFav) {
            // Get the color from resources
            @ColorInt int color = ContextCompat.getColor(context, R.color.favorite_color);
            holder.ivAddfavorite.setColorFilter(color);
            holder.favoriteStatus.setText("true");
        } else {
            @ColorInt int color = ContextCompat.getColor(context, R.color.white);
            holder.ivAddfavorite.setColorFilter(color);
            holder.favoriteStatus.setText("false");
        }
    }

    @Override
    public int getItemCount() {
        return items.getResults().size();
    }

//    private void updateFavoriteStatus(boolean isFav) {
//
//    }

    @Override
    public void onHeartClick(int movieId, boolean fav) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("media_id", movieId);
            jsonBody.put("media_type", "movie");
            jsonBody.put("favorite", fav);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        final String requestBody = jsonBody.toString();

        GsonRequest<Response> gsonRequest = new GsonRequest<>(
                context,
                Request.Method.POST,
                Constant.API.postFavoriteMovieApi(context),
                Response.class,
                requestBody,
                response -> {
                    Log.i("VOLLEY", "thành công");
                    if (fav) {
                        FancyToast.makeText(context, "Added to favorites", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                    } else {
                        FancyToast.makeText(context, "Removed from favorites", FancyToast.LENGTH_LONG, FancyToast.INFO, true).show();
                    }

                    if (taskCallback != null) {
                        Util.saveFavoriteList(context, taskCallback);
                    }

                }, error -> {
            Log.i("VOLLEY", "thất bại");
            if (fav) {
                Toast.makeText(context, "Failed to add to favorites", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to remove from favorites", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueueSingleton.getInstance(context).addToRequestQueue(gsonRequest);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, scoreTxt, idTxt, favoriteStatus;
        ImageView pic;
        ImageView ivAddfavorite, ivRemovefavorite;
//        OnHeartClickListener onHeartClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            scoreTxt = itemView.findViewById(R.id.scoreTxt);
            pic = itemView.findViewById(R.id.pic);
            idTxt = itemView.findViewById(R.id.idTxt);
            ivAddfavorite = itemView.findViewById(R.id.addfavorite);
            ivRemovefavorite = itemView.findViewById(R.id.removefavorite);
            favoriteStatus = itemView.findViewById(R.id.favoriteStatus);

//            this.onHeartClickListener = onHeartClickListener;
//
//            ivAddfavorite.setOnClickListener(v -> {
//                onHeartClickListener.onHeartClick(getAdapterPosition(), true);
//            });

//            if (ivRemovefavorite != null) {
//                ivRemovefavorite.setOnClickListener(v -> {
//                    onHeartClickListener.onHeartClick(getAdapterPosition(), false);
//                });
//            }
        }
    }
}

