package com.example.g2_movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.g2_movieapp.R;
import com.example.g2_movieapp.activity.DetailActivity;
import com.example.g2_movieapp.domain.MovieData;
import com.example.g2_movieapp.helper.Constant;


public class SearchMovieAdapter extends RecyclerView.Adapter<SearchMovieAdapter.ViewHolder> {

    MovieData items;
    Context context;

    public SearchMovieAdapter(MovieData items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_film_horizontal, parent, false);
        context = parent.getContext();

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTxt.setText(items.getResults().get(position).getTitle());
        holder.releaseDateTxt.setText(items.getResults().get(position).getReleaseDate() == null ?
                "" : items.getResults().get(position).getReleaseDate());

        Glide.with(holder.itemView.getContext())
                .load(Constant.API.IMAGE + items.getResults().get(position).getPosterPath())
                .into(holder.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra("id", items.getResults().get(position).getId());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.getResults().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView pic;
        private TextView titleTxt, releaseDateTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pic = itemView.findViewById(R.id.pic);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            releaseDateTxt = itemView.findViewById(R.id.releaseDateTxt);
        }
    }
}
