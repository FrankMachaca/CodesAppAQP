package com.example.fmach.codesappaqp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fmach.codesappaqp.Model.Places;
import com.example.fmach.codesappaqp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.FloatingActionButton;


import com.squareup.picasso.Picasso;

public class PlaceDetailsActivity extends AppCompatActivity {

    private FloatingActionButton addToCartBtn;

    private ImageView placeImage;
    private TextView placeAddress, placeDescription, placeName;
    private String placeID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);

        placeID = getIntent().getStringExtra("pid");

        placeImage =  findViewById(R.id.place_image_details);
        placeAddress = (TextView) findViewById(R.id.place_address_details);
        placeDescription = (TextView) findViewById(R.id.place_description_details);
        placeName = (TextView) findViewById(R.id.place_name_details);


        getPlaceDetails(placeID);
    }

    private void getPlaceDetails(String placeID) {
        DatabaseReference placeRef = FirebaseDatabase.getInstance().getReference().child("Places");

        placeRef.child(placeID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Places places = dataSnapshot.getValue(Places.class);

                    placeName.setText(places.getPname());
                    placeAddress.setText("Ubicanos en: " + places.getAddress());
                    placeDescription.setText(places.getDescription());

                    Picasso.get().load(places.getImage()).into(placeImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
