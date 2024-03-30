package com.imihigocizitensplanning.app.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
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
import com.imihigocizitensplanning.app.UsefullClasses.Categories;
import com.imihigocizitensplanning.app.UsefullClasses.Constants;
import com.imihigocizitensplanning.app.UsefullClasses.PdfGenerator;

import java.util.ArrayList;

public class LeadersActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth auth;
    RecyclerView leadersRecyclerView;
    MaterialButton generateCellLeaders;

    MaterialButton generateSectorLeadersDistrict, generateCellLeadersDistrict;

    LinearLayout layoutForSector, layoutForDistrict;

    String sectorLeaderName = "0";
    ArrayList<User> allLeaders = new ArrayList<>();
    ArrayList<User> allSectorLeaders = new ArrayList<>();
    ArrayList<User> allCellLeaders = new ArrayList<>();
    TextView titleOfLeaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaders);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        leadersRecyclerView = findViewById(R.id.leadersRecyclerView);
        leadersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        titleOfLeaders = findViewById(R.id.titleOfLeaders);

        //extra
        generateCellLeaders = findViewById(R.id.generateCellLeaders);

        layoutForDistrict = findViewById(R.id.layoutForDistrict);
        layoutForSector = findViewById(R.id.layoutForSector);

        generateSectorLeadersDistrict = findViewById(R.id.generateSectorLeadersDistrict);
        generateCellLeadersDistrict = findViewById(R.id.generateCellLeadersDistrict);
        //end of extra

        Intent intent = getIntent();
        boolean isFromSector = intent.getBooleanExtra("isFromSector", false);

        if(isFromSector)
        {
            layoutForSector.setVisibility(View.VISIBLE);
            layoutForDistrict.setVisibility(View.GONE);
            sectorLeaderName = intent.getStringExtra("sectorName");
            titleOfLeaders.setText("Cell Leaders in " + sectorLeaderName + " Sector");
        }

        generateCellLeaders.setOnClickListener(v -> {
            String titleDoc = "Leaders of Cells in " + sectorLeaderName + " Sector";
            PdfGenerator.generateUserPdf(this, allLeaders, titleDoc);
        });

        generateSectorLeadersDistrict.setOnClickListener(v -> {
            leadersRecyclerView.setAdapter(new LeadersAdapter(LeadersActivity.this, allSectorLeaders));
            new AlertDialog.Builder(LeadersActivity.this)
                    .setTitle("Confirm Report Generation")
                    .setMessage("Generate PDF Report of Sector Leaders?")
                    .setPositiveButton("Generate", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String titleDoc = "Sector Leaders in HUYE District";
                            PdfGenerator.generateUserPdf(LeadersActivity.this, allSectorLeaders, titleDoc);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).show();
        });


        generateCellLeadersDistrict.setOnClickListener(v -> {
            leadersRecyclerView.setAdapter(new LeadersAdapter(LeadersActivity.this, allCellLeaders));

            new AlertDialog.Builder(LeadersActivity.this)
                    .setTitle("Confirm Report Generation")
                    .setMessage("Generate PDF Report of Cell Leaders?")
                    .setPositiveButton("Generate", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String titleDoc = "Cell Leaders of HUYE District";
                            PdfGenerator.generateUserPdf(LeadersActivity.this, allCellLeaders, titleDoc);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).show();

        });


        if(isFromSector)
        {
            database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren())
                    {
                        User user = dataSnapshot.getValue(User.class);
                        if(user.getUserCategory().equals(Constants.CELL_LEADER) && user.getSector().equals(sectorLeaderName))
                        {
                            allLeaders.add(user);
                        }
                    }
                    leadersRecyclerView.setAdapter(new LeadersAdapter(LeadersActivity.this, allLeaders));
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
                        if(user.getUserCategory().equals(Constants.SECTOR_LEADER))
                        {
                            allSectorLeaders.add(user);
                        }
                        if(user.getUserCategory().equals(Constants.CELL_LEADER))
                        {
                            allCellLeaders.add(user);
                        }
                    }
                    leadersRecyclerView.setAdapter(new LeadersAdapter(LeadersActivity.this, allSectorLeaders));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}