package com.example.pbl_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.ktx.Firebase;

public class activity_profile extends AppCompatActivity {
    private Firebase user;
    private  DatabaseReference reference;
    private  String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
}