package com.imihigocizitensplanning.app.Cell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imihigocizitensplanning.app.Adapters.CellViewPagerAdapter;
import com.imihigocizitensplanning.app.Auth.LoginActivity;
import com.imihigocizitensplanning.app.Citizen.UserActivity;
import com.imihigocizitensplanning.app.District.DistrictActivity;
import com.imihigocizitensplanning.app.MainActivity;
import com.imihigocizitensplanning.app.Models.Notifikation;
import com.imihigocizitensplanning.app.Models.User;
import com.imihigocizitensplanning.app.R;
import com.imihigocizitensplanning.app.UsefullClasses.Notify;

public class CellActivity extends AppCompatActivity {

    TabLayout cellTabLayout;
    ViewPager cellViewPager;
    TextView cellLogoutBtn;
    FirebaseDatabase database;
    TextView currentUserNames, currentUserSector, currentUserCell;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell);

        cellTabLayout = findViewById(R.id.cellTabLayout);
        cellViewPager = findViewById(R.id.cellViewPager);
        cellLogoutBtn = findViewById(R.id.cellLogoutBtn);
        //extra
        database = FirebaseDatabase.getInstance();
        currentUserNames = findViewById(R.id.currentUserNames);
        currentUserCell = findViewById(R.id.currentUserCell);
        currentUserSector = findViewById(R.id.currentUserSector);
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
                        currentUserNames.setText("Hello, " + currentUser.getNames());
                        currentUserSector.setText("Sector: " + currentUser.getSector());
                        currentUserCell.setText("Cell: " + currentUser.getCell());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }

        cellLogoutBtn.setOnClickListener(v->{
            if(mAuth.getCurrentUser()!= null)
            {
                mAuth.signOut();
                Intent intent = new Intent(CellActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cellTabLayout.setupWithViewPager(cellViewPager);
        cellViewPager.setAdapter(new CellViewPagerAdapter(getSupportFragmentManager()));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CellActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}