package com.example.fmach.codesappaqp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {
    private String CategoryName, Description, Address, Pname, saveCurrentDate, saveCurrentTime;
    private Button AddNewPlaceButton;
    private ImageView InputPlaceImage;
    private EditText InputPlaceName, InputPlaceDescription, InputPlaceAddress;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String placeRandomKey, downloadImageUrl;
    private StorageReference PlaceImageRef;
    private DatabaseReference PlacesRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        CategoryName = getIntent().getExtras().get("category").toString();
        PlaceImageRef = FirebaseStorage.getInstance().getReference().child("Place Images");
        PlacesRef = FirebaseDatabase.getInstance().getReference().child("Places");

        //Toast.makeText(this, CategoryName, Toast.LENGTH_SHORT).show();


        AddNewPlaceButton = (Button) findViewById(R.id.add_new_place);
        InputPlaceImage = (ImageView) findViewById(R.id.select_place_image);
        InputPlaceName = (EditText) findViewById(R.id.place_name);
        InputPlaceDescription = (EditText) findViewById(R.id.place_description);
        InputPlaceAddress = (EditText) findViewById(R.id.place_address);
        loadingBar = new ProgressDialog(this);



        TextView categoryTextView = (TextView) findViewById(R.id.category_name);
        categoryTextView.setText(CategoryName.toUpperCase());

        InputPlaceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });
        
        AddNewPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidatePlaceData();
            }
        });
    }

    private void ValidatePlaceData() {
        Description = InputPlaceDescription.getText().toString();
        Address = InputPlaceAddress.getText().toString();
        Pname = InputPlaceName.getText().toString();
        
        if (ImageUri == null){
            Toast.makeText(this, "Por favor elija una imagen", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description)){
            Toast.makeText(this, "Por favor escriba una descripción del lugar", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Address)){
            Toast.makeText(this, "Por favor Escriba la dirección", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname)){
            Toast.makeText(this, "Por favor Escriba el nombre del lugar", Toast.LENGTH_SHORT).show();
        }
        else {
            StoragePlaceInformation();
        }

    }

    private void StoragePlaceInformation() {

        loadingBar.setTitle("Agregando nuevo lugar");
        loadingBar.setMessage("Espere mientra se esta agregando");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss ");
        saveCurrentTime = currentTime.format(calendar.getTime());
        long currentTimeCreation = System.currentTimeMillis();
        placeRandomKey = ""+currentTimeCreation;

        final StorageReference filePath = PlaceImageRef.child((ImageUri.getLastPathSegment() + placeRandomKey + "jpg"));

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error:" + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "Imagen subida con exito", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminAddNewProductActivity.this, "extracion de imagen exitosa", Toast.LENGTH_SHORT).show();

                            SavePlaceInfoToDatabase();
                        }
                    }
                });
            }
        });

    }

    private void SavePlaceInfoToDatabase() {
        HashMap<String,Object> placeMap = new HashMap<>();
        placeMap.put("pid", placeRandomKey);
        placeMap.put("date", saveCurrentDate);
        placeMap.put("time", saveCurrentTime);
        placeMap.put("description", Description);
        placeMap.put("image", downloadImageUrl);
        placeMap.put("category", CategoryName);
        placeMap.put("address", Address);
        placeMap.put("pname", Pname);

        PlacesRef.child(placeRandomKey).updateChildren(placeMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                    startActivity(intent);

                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewProductActivity.this, "Lugar fue agregado ...", Toast.LENGTH_SHORT).show();
                }
                else{
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(AdminAddNewProductActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == GalleryPick && resultCode == RESULT_OK && data!=null){
            ImageUri = data.getData();
            InputPlaceImage.setImageURI(ImageUri);
        }
    }
}
