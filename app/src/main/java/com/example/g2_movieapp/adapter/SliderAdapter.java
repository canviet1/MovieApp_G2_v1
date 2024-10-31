package com.example.g2_movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.g2_movieapp.R;
import com.example.g2_movieapp.activity.DetailActivity;
import com.example.g2_movieapp.domain.MovieData;
import com.example.g2_movieapp.domain.detail.MovieDetail;
import com.example.g2_movieapp.helper.Constant;
import com.example.g2_movieapp.helper.GsonRequest;
import com.example.g2_movieapp.helper.RequestQueueSingleton;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private MovieData sliderItems;
    private ViewPager2 viewPager2;
    private Context context;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.getResults().addAll(sliderItems.getResults());
            notifyDataSetChanged();
        }
    };

    public SliderAdapter(MovieData sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderAdapter.SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_dashboard_slider, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapter.SliderViewHolder holder, int position) {
        getDetailMovie(sliderItems.getResults().get(position).getId(), holder);

        if (position == sliderItems.getResults().size() - 2) {
            viewPager2.post(runnable);
        }
    }

    private void getDetailMovie(int id, @NonNull SliderAdapter.SliderViewHolder holder) {
        String url = Constant.API.GET_MOVIE + id +"?api_key=5aa5ed76193d3d2a01f9f679885ec8d3";
        GsonRequest<MovieDetail> gsonRequest = new GsonRequest<>(context, Request.Method.GET, url, MovieDetail.class, null,
                response -> {
                    MovieDetail movieDetail = response;
                    holder.setImage(movieDetail);
                    holder.itemView.setOnClickListener(v -> {
                        Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
                        intent.putExtra("id", movieDetail.getId());
                        holder.itemView.getContext().startActivity(intent);
                    });
                },
                error -> {
                    Toast.makeText(context, "Failed to load movie details", Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueueSingleton.getInstance(context).addToRequestQueue(gsonRequest);
    }

    @Override
    public int getItemCount() {
        return sliderItems.getResults().size();
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView name, genre, voteCount, year, time;
        void setImage(MovieDetail movie) {
            RequestOptions requestOptions = new RequestOptions()
                    .transform(new CenterCrop(), new RoundedCorners(60));

            Glide.with(context)
                    .load(Constant.API.IMAGE + movie.getPosterPath())
                    .apply(requestOptions)
                    .into(imageView);

            name.setText(movie.getTitle());
            genre.setText(movie.getGenres().get(0).getName());
            year.setText(movie.getReleaseDate().split("-")[0]);
            voteCount.setText(movie.getVoteCount().toString());
            time.setText(movie.getRuntime().toString());
        }

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageSlider);
            name = itemView.findViewById(R.id.name);
            genre = itemView.findViewById(R.id.genre);
            voteCount = itemView.findViewById(R.id.voteCount);
            year = itemView.findViewById(R.id.year);
            time = itemView.findViewById(R.id.time);
        }
    }
}
