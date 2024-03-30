package com.imihigocizitensplanning.app.District;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imihigocizitensplanning.app.Adapters.DistrictViewPagerAdapter;
import com.imihigocizitensplanning.app.Auth.LoginActivity;
import com.imihigocizitensplanning.app.Common.LeadersActivity;
import com.imihigocizitensplanning.app.MainActivity;
import com.imihigocizitensplanning.app.Models.User;
import com.imihigocizitensplanning.app.R;

public class DistrictActivity extends AppCompatActivity {

    TabLayout districttabLayout;
    ViewPager districtViewPager;
    TextView logoutBtn, currentLeaderCategory;
    FirebaseAuth mAuth;
    Spinner seasonSpinner;
    boolean spinnerSelectedByPc = true;
    FirebaseDatabase database;
    String[] planStatuses;
    TextView currentUserNames, currentUserSector, currentUserCell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district);

        districttabLayout = findViewById(R.id.districtTabLayout);
        districtViewPager = findViewById(R.id.districtViewPager);
        //extra
        currentUserNames = findViewById(R.id.currentUserNamesD);
        currentUserCell = findViewById(R.id.currentUserCellD);
        currentUserSector = findViewById(R.id.currentUserSectorD);
        //end of extra
        logoutBtn = findViewById(R.id.logoutBtn);
        seasonSpinner = findViewById(R.id.seasonSpinner);
        currentLeaderCategory = findViewById(R.id.currentLeaderCategory);
        planStatuses = getResources().getStringArray(R.array.plan_season);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        if(mAuth != null)
        {
            database.getReference().child("Users").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists())
                    {
                        return;
                    }
                    else
                    {
                        User currentUser = snapshot.getValue(User.class);
                        currentUserNames.setText("Hello, " + currentUser.getNames());
                        currentUserSector.setText("Sector: " + currentUser.getSector());
                        currentUserCell.setText("Cell: " + currentUser.getCell());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }


        logoutBtn.setOnClickListener(v -> {
            if(mAuth.getCurrentUser() != null)
            {
                mAuth.signOut();
                Intent intent = new Intent(DistrictActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        districttabLayout.setupWithViewPager(districtViewPager);
        districtViewPager.setAdapter(new DistrictViewPagerAdapter(getSupportFragmentManager()));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.plan_season,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seasonSpinner.setAdapter(adapter);

        seasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinnerSelectedByPc)
                {
                    spinnerSelectedByPc = false;
                }
                else
                {
                    dbWriteSeason(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });

        String currentUserId = mAuth.getCurrentUser().getUid();
        database.getReference().child("DistrictLeadersCategory").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentDistrictLeaderCategory = snapshot.getValue(String.class);
                currentLeaderCategory.setText("Your Category: " + currentDistrictLeaderCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference().child("districts").child("huye").child("season").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int currentSeason = snapshot.getValue(Integer.class);
                if(currentSeason == 1)
                {
                    seasonSpinner.setSelection(currentSeason);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DistrictActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void dbWriteSeason( int newSeason) {
        DatabaseReference seasonRef = database.getReference().child("districts").child("huye").child("season");
        seasonRef.setValue(newSeason);
        String selectedText = planStatuses[newSeason];
        Toast.makeText(this, "Current Season is: " + selectedText, Toast.LENGTH_LONG).show();
    }
}