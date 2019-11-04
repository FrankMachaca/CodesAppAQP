package com.example.fmach.codesappaqp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.rey.material.widget.EditText;


public class SearchPlacesActivity extends AppCompatActivity {

    private Button SearchBtn;
    private EditText inpuText;
    private RecyclerView searchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_places);

        inpuText = findViewById(R.id.search_places);
        SearchBtn = findViewById(R.id.search_bt);

        //searchList.setLayoutManager(new LinearLayoutManager(SearchPlacesActivity.this));
    }
}
