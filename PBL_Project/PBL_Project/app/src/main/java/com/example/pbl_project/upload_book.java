package com.example.pbl_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import com.squareup.picasso.Picasso;

public class upload_book extends AppCompatActivity {
    private static final int pick_image_req = 1;
    private Button btnchooseimage;
    private Button btnupload;
    private TextView showuploads;
    private EditText bookname;
    private  EditText price;
    private  EditText owner;
    private ImageView image;
    private ProgressBar progressBar;
    private Uri imageuri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_book);
        btnchooseimage = findViewById(R.id.button_choose_image);
        btnupload = findViewById(R.id.button_upload);
        showuploads = findViewById(R.id.text_view_show_uploads);
        bookname = findViewById(R.id.edit_text_book_name);
        price = findViewById(R.id.edit_text_price);
        owner = findViewById(R.id.edit_text_avail);
        image = findViewById(R.id.image_view);
        progressBar = findViewById(R.id.progress_bar);
        mStorageRef = FirebaseStorage.getInstance().getReference("/uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("/uploads");
         btnchooseimage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 openfilechooser();

             }
         });

         btnupload.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (mUploadTask != null && mUploadTask.isInProgress()) {
                     Toast.makeText(upload_book.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                 } else {
                     uploadFile();
                 }

             }
         });
         showuploads.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 openImagesActivity();

             }
         });
    }
    private  void  openfilechooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,pick_image_req);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == pick_image_req && resultCode == RESULT_OK && data != null && data.getData()!=null){
            imageuri = data.getData();
            Picasso.get().load(imageuri).into(image);
            image.setImageURI(imageuri);
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile() {
        if (imageuri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageuri));

            mUploadTask = fileReference.putFile(imageuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(upload_book.this, "Upload successful", Toast.LENGTH_LONG).show();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();

                            //Log.d(TAG, "onSuccess: firebase download url: " + downloadUrl.toString()); //use if testing...don't need this line.
                            Upload upload = new Upload(bookname.getText().toString().trim(),downloadUrl.toString(),owner.getText().toString().trim(),price.getText().toString().trim());

                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);

//                            Upload upload = new Upload(bookname.getText().toString().trim(),availability.getText().toString().trim(),price.getText().toString().trim(),taskSnapshot.getStorage().getDownloadUrl().toString());
//                            String uploadId = mDatabaseRef.push().getKey();
//                            mDatabaseRef.child(uploadId).setValue(upload);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(upload_book.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
    private void openImagesActivity() {
        Intent intent = new Intent(this, ImagesActivity.class);
        startActivity(intent);
    }

}