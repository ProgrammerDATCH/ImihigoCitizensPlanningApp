package com.imihigocizitensplanning.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imihigocizitensplanning.app.Auth.LoginActivity;
import com.imihigocizitensplanning.app.Auth.StartRegistrationActivity;
import com.imihigocizitensplanning.app.Models.Notifikation;
import com.imihigocizitensplanning.app.UsefullClasses.Notify;
import com.imihigocizitensplanning.app.UsefullClasses.UsefulFunctions;

public class MainActivity extends AppCompatActivity {

    Button adminLogin, citizenRegist;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        if(auth.getCurrentUser() != null)
        {
            database.getReference().child("Notifications").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists())
                    {
                        return;
                    }
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Notifikation notifikation = dataSnapshot.getValue(Notifikation.class);
                        if(!notifikation.isDone())
                        {
                            Notify.NotifyUser(notifikation.getMsg(), MainActivity.this);
                            //set is Done
                            database.getReference().child("Notifications").child(auth.getCurrentUser().getUid()).child(dataSnapshot.getKey()).child("done").setValue(true);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }



        adminLogin = findViewById(R.id.adminLogin);
        citizenRegist = findViewById(R.id.citizenRegist);

        adminLogin.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        });

        citizenRegist.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, StartRegistrationActivity.class);
            startActivity(i);
        });



    }
    @Override
    public void onBackPressed() {
        UsefulFunctions usefulFunctions = new UsefulFunctions();
        usefulFunctions.preventBack(this, this);
    }
}