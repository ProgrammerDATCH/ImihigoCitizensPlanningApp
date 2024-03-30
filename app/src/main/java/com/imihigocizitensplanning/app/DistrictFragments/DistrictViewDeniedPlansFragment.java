package com.imihigocizitensplanning.app.DistrictFragments;

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
import com.imihigocizitensplanning.app.R;

import java.util.ArrayList;

public class DistrictViewDeniedPlansFragment extends Fragment {

    RecyclerView districtDeniedPlansRecyclerView;
    FirebaseDatabase database;
    FirebaseAuth auth;
    LinearLayout districtDeniedLoadingPlansProgress, districtDeniedEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_district_view_denied_plans, container, false);


        districtDeniedLoadingPlansProgress = rootView.findViewById(R.id.districtDeniedLoadingPlansProgress);
        districtDeniedEmpty = rootView.findViewById(R.id.districtDeniedEmpty);
        districtDeniedPlansRecyclerView = rootView.findViewById(R.id.districtDeniedPlansRecyclerView);
        districtDeniedPlansRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser().getUid();

        database.getReference().child("DistrictLeadersCategory").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentDistrictLeaderCategory = snapshot.getValue(String.class);
                database.getReference().child("districts").child("huye").child("pendingImihigo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        districtDeniedLoadingPlansProgress.setVisibility(View.GONE);
                        if(snapshot.exists())
                        {
                            districtDeniedEmpty.setVisibility(View.GONE);
                        }
                        else
                        {
                            districtDeniedEmpty.setVisibility(View.VISIBLE);
                        }
                        ArrayList<String> planKeysList = new ArrayList<>();
                        ArrayList<PendingImihigo> allImihigo = new ArrayList<>();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            PendingImihigo umuhigo = dataSnapshot.getValue(PendingImihigo.class);
                            //check progress
                            if(umuhigo.getPendingPlanProgress() == -3 && umuhigo.getPendingPlanCategory().equals(currentDistrictLeaderCategory))
                            {
                                allImihigo.add(umuhigo);
                                planKeysList.add(dataSnapshot.getKey());
                            }
                        }
                        districtDeniedPlansRecyclerView.setAdapter(new PendingPlansAdapter(getContext(), allImihigo, planKeysList, -3));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        districtDeniedLoadingPlansProgress.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return rootView;
    }
}