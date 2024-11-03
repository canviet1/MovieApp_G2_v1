package com.example.g2_movieapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.g2_movieapp.R;
import com.example.g2_movieapp.domain.Movie;
import com.example.g2_movieapp.helper.Constant;

import java.util.List;

public class KnownForMoviesAdapter extends RecyclerView.Adapter<KnownForMoviesAdapter.MovieViewHolder> {
    private List<Movie> movies;

    public KnownForMoviesAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_known_for_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.movieTitleTextView.setText(movie.getTitle());
        holder.movieOverviewTextView.setText(movie.getOverview());
        Glide.with(holder.itemView.getContext())
                .load(Constant.API.IMAGE + movie.getPosterPath())
                .into(holder.moviePosterImageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePosterImageView;
        TextView movieTitleTextView, movieOverviewTextView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePosterImageView = itemView.findViewById(R.id.moviePosterImageView);
            movieTitleTextView = itemView.findViewById(R.id.movieTitleTextView);
            movieOverviewTextView = itemView.findViewById(R.id.movieOverviewTextView);
        }
    }
}
