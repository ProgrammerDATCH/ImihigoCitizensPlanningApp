package com.imihigocizitensplanning.app.Citizen;

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
import com.imihigocizitensplanning.app.Adapters.UserViewPagerAdapter;
import com.imihigocizitensplanning.app.Auth.LoginActivity;
import com.imihigocizitensplanning.app.Cell.CellActivity;
import com.imihigocizitensplanning.app.District.DistrictActivity;
import com.imihigocizitensplanning.app.MainActivity;
import com.imihigocizitensplanning.app.Models.Notifikation;
import com.imihigocizitensplanning.app.Models.User;
import com.imihigocizitensplanning.app.R;
import com.imihigocizitensplanning.app.UsefullClasses.Notify;


public class UserActivity extends AppCompatActivity {

    TextView titleText;

    TabLayout userTabLayout;
    ViewPager userViewPager;
    String currentYear = "2023-2024";
    TextView userLogoutBtn;
    TextView currentUserNames, currentUserSector, currentUserCell;
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        titleText = findViewById(R.id.titleText);
        userLogoutBtn = findViewById(R.id.userLogoutBtn);
        titleText.setText("IMIHIGO mu Karere ka HUYE umwaka ("+currentYear+")");
        //extra
        currentUserNames = findViewById(R.id.currentUserNamesU);
        currentUserCell = findViewById(R.id.currentUserCellU);
        currentUserSector = findViewById(R.id.currentUserSectorU);
        //end of extra

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

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

        if(mAuth != null)
        {
            database.getReference().child("Notifications").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                            Notify.NotifyUser(notifikation.getMsg(), UserActivity.this);
                            //set is Done
                            database.getReference().child("Notifications").child(mAuth.getCurrentUser().getUid()).child(dataSnapshot.getKey()).child("done").setValue(true);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }

        userLogoutBtn.setOnClickListener(v->{
            if(mAuth.getCurrentUser()!= null)
            {
                mAuth.signOut();
                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        userTabLayout = findViewById(R.id.userTabLayout);
        userViewPager = findViewById(R.id.userViewPager);

        userTabLayout.setupWithViewPager(userViewPager);
        userViewPager.setAdapter(new UserViewPagerAdapter(getSupportFragmentManager()));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}