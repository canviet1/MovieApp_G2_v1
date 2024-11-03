package com.example.g2_movieapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.g2_movieapp.R;

import java.util.ArrayList;



public class FavoriteList extends AppCompatActivity {

    private Button btn_po;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);
        btn_po = findViewById(R.id.btn_popular);
        btn_po.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FavoriteList.this, PopularActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}