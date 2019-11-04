package com.example.fmach.codesappaqp;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import io.paperdb.Paper;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView restaurant, discoteca, turismo;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        Toast.makeText(this, "Bienvenido Admin", Toast.LENGTH_SHORT).show();
        logout = (Button)findViewById(R.id.logout_button);
        restaurant = (ImageView) findViewById(R.id.restaurantes);
        discoteca = (ImageView) findViewById(R.id.discotecas);
        turismo = (ImageView) findViewById(R.id.turismo);

        restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "restaurant");
                startActivity(intent);
            }
        });
        discoteca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "discoteca");
                startActivity(intent);
            }
        });
        turismo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "turismo");
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this,MainActivity.class);
                Paper.book().destroy();
                startActivity(intent);
            }
        });

    }
}
