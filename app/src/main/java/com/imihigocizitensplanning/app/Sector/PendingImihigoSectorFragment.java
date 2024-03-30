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

public class PendingImihigoSectorFragment extends Fragment {

    RecyclerView sectorSuggestedPlansRecyclerView;
    FirebaseDatabase database;
    FirebaseAuth auth;
    LinearLayout sectorSuggestedLoadingPlansProgress, sectorSuggestedEmpty;
    User currentSectorLeader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pending_imihigo_sector, container, false);

        sectorSuggestedLoadingPlansProgress = rootView.findViewById(R.id.sectorSuggestedLoadingPlansProgress);
        sectorSuggestedEmpty = rootView.findViewById(R.id.sectorSuggestedEmpty);
        sectorSuggestedPlansRecyclerView = rootView.findViewById(R.id.sectorSuggestedPlansRecyclerView);
        sectorSuggestedPlansRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
                    sectorSuggestedEmpty.setVisibility(View.GONE);
                }
                else
                {
                    sectorSuggestedEmpty.setVisibility(View.VISIBLE);
                }

                ArrayList<String> planKeysList = new ArrayList<>();
                ArrayList<PendingImihigo> allImihigo = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    PendingImihigo umuhigo = dataSnapshot.getValue(PendingImihigo.class);
                    //check level

                    if(umuhigo.getPendingPlanProgress() == 1)
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
                    sectorSuggestedEmpty.setVisibility(View.VISIBLE);
                }
                sectorSuggestedLoadingPlansProgress.setVisibility(View.GONE);
                sectorSuggestedPlansRecyclerView.setAdapter(new PendingPlansAdapter(getContext(), allImihigo, planKeysList, 1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                sectorSuggestedLoadingPlansProgress.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}