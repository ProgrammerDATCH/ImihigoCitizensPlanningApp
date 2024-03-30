package com.imihigocizitensplanning.app.Sector;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imihigocizitensplanning.app.Adapters.PendingPlansAdapter;
import com.imihigocizitensplanning.app.Models.PendingImihigo;
import com.imihigocizitensplanning.app.Models.User;
import com.imihigocizitensplanning.app.R;

import java.util.ArrayList;


public class DeniedImihigoSectorFragment extends Fragment {

    RecyclerView sectorDeniedPlansRecyclerView;
    FirebaseDatabase database;
    FirebaseAuth auth;
    LinearLayout sectorDeniedLoadingPlansProgress, sectorDeniedEmpty;
    User currentSectorLeader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_denied_imihigo_sector, container, false);
        sectorDeniedLoadingPlansProgress = rootView.findViewById(R.id.sectorDeniedLoadingPlansProgress);
        sectorDeniedEmpty = rootView.findViewById(R.id.sectorDeniedEmpty);
        sectorDeniedPlansRecyclerView = rootView.findViewById(R.id.sectorDeniedPlansRecyclerView);
        sectorDeniedPlansRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        String uid = auth.getUid();
        database.getReference().child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentSectorLeader = snapshot.getValue(User.class);
                loadImihigo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        return rootView;
    }

    public  void loadImihigo()
    {
        database.getReference().child("districts").child("huye").child("pendingImihigo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    sectorDeniedEmpty.setVisibility(View.GONE);
                }
                else
                {
                    sectorDeniedEmpty.setVisibility(View.VISIBLE);
                }

                ArrayList<String> planKeysList = new ArrayList<>();
                ArrayList<PendingImihigo> allImihigo = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    PendingImihigo umuhigo = dataSnapshot.getValue(PendingImihigo.class);
                    //check level

                    if(umuhigo.getPendingPlanProgress() == -2 || umuhigo.getPendingPlanProgress() == 2 || umuhigo.getPendingPlanProgress() == 3)
                    {
                        String leaderSector = currentSectorLeader.getSector().toLowerCase().trim();
                        String userSector = umuhigo.getPendingPlanUserSector().toLowerCase().trim();
                        if(leaderSector.equalsIgnoreCase(userSector))
                        {
                            allImihigo.add(umuhigo);
                            planKeysList.add(dataSnapshot.getKey());
                        }
                    }
                }
                if(allImihigo.size() < 1)
                {
                    sectorDeniedEmpty.setVisibility(View.VISIBLE);
                }
                sectorDeniedLoadingPlansProgress.setVisibility(View.GONE);
                sectorDeniedPlansRecyclerView.setAdapter(new PendingPlansAdapter(getContext(), allImihigo, planKeysList, -2));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                sectorDeniedLoadingPlansProgress.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}