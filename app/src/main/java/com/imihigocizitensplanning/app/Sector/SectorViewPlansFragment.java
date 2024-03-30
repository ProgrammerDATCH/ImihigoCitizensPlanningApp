package com.imihigocizitensplanning.app.Sector;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imihigocizitensplanning.app.Adapters.PlansAdapter;
import com.imihigocizitensplanning.app.Models.Imihigo;
import com.imihigocizitensplanning.app.R;

import java.util.ArrayList;

public class SectorViewPlansFragment extends Fragment {

    RecyclerView districtPlansRecycleView;
    FirebaseDatabase database;
    FirebaseAuth auth;
    LinearLayout loadingPlansProgress, districtViewPlansEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_sector_view_plans, container, false);
        loadingPlansProgress = rootView.findViewById(R.id.districtLoadingPlansProgress);
        districtViewPlansEmpty = rootView.findViewById(R.id.districtViewPlansEmpty);
        districtPlansRecycleView = rootView.findViewById(R.id.districtPlansRecyclerView);
        districtPlansRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        database.getReference().child("districts").child("huye").child("imihigo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loadingPlansProgress.setVisibility(View.GONE);
                if(snapshot.exists())
                {
                    districtViewPlansEmpty.setVisibility(View.GONE);
                }
                else
                {
                    districtViewPlansEmpty.setVisibility(View.VISIBLE);
                }
                ArrayList<Imihigo> allImihigo = new ArrayList<>();
                ArrayList<String> planKeysList = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Imihigo umuhigo = dataSnapshot.getValue(Imihigo.class);
                    allImihigo.add(umuhigo);
                    planKeysList.add(dataSnapshot.getKey());
                }
                districtPlansRecycleView.setAdapter(new PlansAdapter(getContext(), allImihigo, planKeysList, true, 2, auth.getCurrentUser().getUid(), auth.getCurrentUser().getEmail()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingPlansProgress.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

}