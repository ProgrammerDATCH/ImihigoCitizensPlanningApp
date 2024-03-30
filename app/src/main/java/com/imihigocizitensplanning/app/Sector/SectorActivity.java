package com.imihigocizitensplanning.app.Sector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imihigocizitensplanning.app.Adapters.SectorViewPagerAdapter;
import com.imihigocizitensplanning.app.Auth.LoginActivity;
import com.imihigocizitensplanning.app.Common.CitizensReportActivity;
import com.imihigocizitensplanning.app.Common.LeadersActivity;
import com.imihigocizitensplanning.app.District.DistrictActivity;
import com.imihigocizitensplanning.app.MainActivity;
import com.imihigocizitensplanning.app.Models.User;
import com.imihigocizitensplanning.app.R;

public class SectorActivity extends AppCompatActivity {

    TabLayout sectorTabLayout;
    ViewPager sectorViewPager;
    TextView sectorLogoutBtn;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    TextView currentUserNamesS, currentUserSectorS, currentUserCellS;
    Button sectorLeadersBtn, sectorCitizensBtn;
    User currentSectorUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sector);

        sectorTabLayout = findViewById(R.id.sectorTabLayout);
        sectorViewPager = findViewById(R.id.sectorViewPager);
        sectorLogoutBtn = findViewById(R.id.sectorLogoutBtn);
        sectorLeadersBtn = findViewById(R.id.sectorLeadersBtn);
        sectorCitizensBtn = findViewById(R.id.sectorCitizensBtn);
        //extra
        database = FirebaseDatabase.getInstance();
        currentUserNamesS = findViewById(R.id.currentUserNamesS);
        currentUserCellS = findViewById(R.id.currentUserCellS);
        currentUserSectorS = findViewById(R.id.currentUserSectorS);
        //end of extra
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
                        currentSectorUser = currentUser;
                        currentUserNamesS.setText("Hello, " + currentUser.getNames());
                        currentUserSectorS.setText("Sector: " + currentUser.getSector());
                        currentUserCellS.setText("Cell: " + currentUser.getCell());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
        
        sectorLeadersBtn.setOnClickListener(v ->
        {
            Intent i = new Intent(SectorActivity.this, LeadersActivity.class);
            i.putExtra("isFromSector", true);
            if(currentSectorUser != null)
            {
                i.putExtra("sectorName", currentSectorUser.getSector());
            }
            else
            {
                Toast.makeText(this, "Your Sector name is Null!", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(i);
        });

        sectorCitizensBtn.setOnClickListener(v ->
        {
            Intent i = new Intent(SectorActivity.this, CitizensReportActivity.class);
            i.putExtra("isFromSector", true);
            if(currentSectorUser != null)
            {
                i.putExtra("sectorName", currentSectorUser.getSector());
            }
            else
            {
                Toast.makeText(this, "Your Sector name is Null!", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(i);
        });
        
        sectorLogoutBtn.setOnClickListener(v -> {
            if(mAuth.getCurrentUser()!= null)
            {
                mAuth.signOut();
                Intent intent = new Intent(SectorActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        sectorTabLayout.setupWithViewPager(sectorViewPager);
        sectorViewPager.setAdapter(new SectorViewPagerAdapter(getSupportFragmentManager()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SectorActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}