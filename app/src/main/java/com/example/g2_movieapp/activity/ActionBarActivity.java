package com.example.g2_movieapp.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.g2_movieapp.R;


public class ActionBarActivity extends AppCompatActivity {


    public void setupActionBar(final Activity activity) {
        ImageView fvList = activity.findViewById(R.id.fvList);
        ImageView trend = activity.findViewById(R.id.home);
        ImageView watchlist = activity.findViewById(R.id.watchlist);
        ImageView search = activity.findViewById(R.id.search);
	// favourite
        fvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, FavoriteList.class);
                activity.startActivity(intent);
            }
        });
        // trend
        trend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
            }
        });
        // watched list
        watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, WatchListActivity.class);
                activity.startActivity(intent);
            }
        });
        //search
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SearchActivity.class);
                activity.startActivity(intent);
            }
        });
    }

}