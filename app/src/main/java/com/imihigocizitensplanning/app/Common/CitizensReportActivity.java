package com.imihigocizitensplanning.app.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imihigocizitensplanning.app.Adapters.LeadersAdapter;
import com.imihigocizitensplanning.app.Models.User;
import com.imihigocizitensplanning.app.R;
import com.imihigocizitensplanning.app.UsefullClasses.Constants;
import com.imihigocizitensplanning.app.UsefullClasses.PdfGenerator;

import java.util.ArrayList;

public class CitizensReportActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth auth;
    RecyclerView citizensRecyclerView;
    MaterialButton generateCitizensReport;
    TextView titleOfCitizens;

    String sectorLeaderName = "0";
    ArrayList<User> allCitizens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizens_report);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        citizensRecyclerView = findViewById(R.id.citizensRecyclerView);
        generateCitizensReport = findViewById(R.id.generateCitizensReport);
        citizensRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        titleOfCitizens = findViewById(R.id.titleOfCitizens);


        Intent intent = getIntent();
        boolean isFromSector = intent.getBooleanExtra("isFromSector", false);

        if(isFromSector)
        {
            sectorLeaderName = intent.getStringExtra("sectorName");
            titleOfCitizens.setText("Citizens in " + sectorLeaderName + " Sector");
        }

        generateCitizensReport.setOnClickListener(v ->
        {
            String titleDoc = (isFromSector) ? "Citizens in " + sectorLeaderName + " Sector" : "Citizens of HUYE District";
            PdfGenerator.generateUserPdf(this, allCitizens, titleDoc);
        });

        if(isFromSector)
        {
            database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                        User user = dataSnapshot.getValue(User.class);
                        if(user.getUserCategory().equals(Constants.CITIZEN) && user.getSector().equals(sectorLeaderName))
                        {
                            allCitizens.add(user);
                        }
                    }
                    citizensRecyclerView.setAdapter(new LeadersAdapter(CitizensReportActivity.this, allCitizens));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
        else
        {
            database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                        User user = dataSnapshot.getValue(User.class);
                        if(user.getUserCategory().equals(Constants.CITIZEN))
                        {
                            allCitizens.add(user);
                        }
                    }
                    citizensRecyclerView.setAdapter(new LeadersAdapter(CitizensReportActivity.this, allCitizens));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}