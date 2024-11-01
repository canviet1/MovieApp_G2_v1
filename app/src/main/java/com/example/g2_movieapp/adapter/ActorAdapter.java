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
import com.example.g2_movieapp.domain.MovieCreditsResponse;
import com.example.g2_movieapp.helper.Constant;

import java.util.List;


public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.ActorViewHolder> {
    private List<MovieCreditsResponse.Cast> actors;

    public ActorAdapter(List<MovieCreditsResponse.Cast> actors) {
        this.actors = actors;
    }

    @NonNull
    @Override
    public ActorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actor, parent, false);
        return new ActorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorViewHolder holder, int position) {
        MovieCreditsResponse.Cast actor = actors.get(position);
        holder.actorName.setText(actor.getName());
        holder.actorCharacter.setText(actor.getCharacter());
        Glide.with(holder.itemView.getContext())
                .load(Constant.API.IMAGE + actor.getProfile_path())
                .into(holder.actorProfileImage);
    }

    @Override
    public int getItemCount() {
        return actors.size();
    }

    public static class ActorViewHolder extends RecyclerView.ViewHolder {
        ImageView actorProfileImage;
        TextView actorName, actorCharacter;

        public ActorViewHolder(@NonNull View itemView) {
            super(itemView);
            actorProfileImage = itemView.findViewById(R.id.actorProfileImage);
            actorName = itemView.findViewById(R.id.actorName);
            actorCharacter = itemView.findViewById(R.id.actorCharacter);
        }
    }
}
