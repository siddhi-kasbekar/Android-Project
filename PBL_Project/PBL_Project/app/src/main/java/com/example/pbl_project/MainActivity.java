package com.example.pbl_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        getSupportActionBar().hide();
        addListenerOnButton();
    }
    public void addListenerOnButton() {

        final Context context = this;

        button= findViewById(R.id.insert_book);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, upload_book.class);
                startActivity(intent);

            }

        });

    }
}