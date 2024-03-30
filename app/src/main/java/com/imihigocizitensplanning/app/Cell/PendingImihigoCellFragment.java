package com.imihigocizitensplanning.app.Cell;

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

public class PendingImihigoCellFragment extends Fragment {

    RecyclerView cellSuggestedPlansRecyclerView;
    FirebaseDatabase database;
    FirebaseAuth auth;
    LinearLayout cellSuggestedLoadingPlansProgress, cellSuggestedEmpty;
    User currentCellLeader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pending_imihigo_cell, container, false);

        cellSuggestedLoadingPlansProgress = rootView.findViewById(R.id.cellSuggestedLoadingPlansProgress);
        cellSuggestedEmpty = rootView.findViewById(R.id.cellSuggestedEmpty);
        cellSuggestedPlansRecyclerView = rootView.findViewById(R.id.cellSuggestedPlansRecyclerView);
        cellSuggestedPlansRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        String uid = auth.getUid();
        database.getReference().child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentCellLeader = snapshot.getValue(User.class);
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
                    cellSuggestedEmpty.setVisibility(View.GONE);
                }
                else
                {
                    cellSuggestedEmpty.setVisibility(View.VISIBLE);
                }

                ArrayList<String> planKeysList = new ArrayList<>();
                ArrayList<PendingImihigo> allImihigo = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    PendingImihigo umuhigo = dataSnapshot.getValue(PendingImihigo.class);
                    //check level

                    if(umuhigo.getPendingPlanProgress() == 0)
                    {
                        String leaderCell = currentCellLeader.getCell().toLowerCase().trim();
                        String userCell = umuhigo.getPendingPlanUserCell().toLowerCase().trim();
                        if(leaderCell.equalsIgnoreCase(userCell))
                        {
                            allImihigo.add(umuhigo);
                            planKeysList.add(dataSnapshot.getKey());
                        }
                    }
                }
                if(allImihigo.size() < 1)
                {
                    cellSuggestedEmpty.setVisibility(View.VISIBLE);
                }
                cellSuggestedLoadingPlansProgress.setVisibility(View.GONE);
                cellSuggestedPlansRecyclerView.setAdapter(new PendingPlansAdapter(getContext(), allImihigo, planKeysList, 0));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cellSuggestedLoadingPlansProgress.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}