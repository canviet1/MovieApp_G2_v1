package com.example.g2_movieapp.adapter;

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
import com.example.g2_movieapp.activity.ActorDetailActivity;
import com.example.g2_movieapp.domain.credit.Actor;
import com.example.g2_movieapp.helper.Constant;

import java.util.ArrayList;
import java.util.List;

public class ActorDetailPopularAdapter extends RecyclerView.Adapter<ActorPopularAdapter.ActorViewHolder>{
    private List<Actor> actors;

    public ActorDetailPopularAdapter(List<Actor> actors) {
        this.actors = actors;
    }

    @NonNull
    @Override
    public ActorPopularAdapter.ActorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.actor_item, parent, false);
        return new ActorPopularAdapter.ActorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActorPopularAdapter.ActorViewHolder holder, int position) {
        Actor actor = actors.get(position);
        holder.actorName.setText(actor.getName());
        holder.actorCharacter.setText(actor.getCharacter());

        Glide.with(holder.itemView.getContext())
                .load(Constant.API.IMAGE + actor.getProfilePath())
                .into(holder.actorProfileImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ActorDetailActivity.class);

            intent.putExtra("actorName", actor.getName());
            intent.putExtra("actorPopularity", actor.getPopularity());
            intent.putExtra("actorProfilePath", actor.getProfilePath());
            intent.putExtra("knownForDepartment", actor.getKnownForDepartment());
            intent.putExtra("actorAdult", actor.getAdult());
            intent.putExtra("actorId", actor.getId());
            intent.putExtra("knownForMovies", new ArrayList<>(actor.getKnownFor()));
            holder.itemView.getContext().startActivity(intent);
        });
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

