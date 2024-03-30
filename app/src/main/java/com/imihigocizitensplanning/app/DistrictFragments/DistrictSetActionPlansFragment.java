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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imihigocizitensplanning.app.Adapters.ActionPlansAdapter;
import com.imihigocizitensplanning.app.Adapters.PendingPlansAdapter;
import com.imihigocizitensplanning.app.Adapters.PlansAdapter;
import com.imihigocizitensplanning.app.Models.Imihigo;
import com.imihigocizitensplanning.app.Models.PendingImihigo;
import com.imihigocizitensplanning.app.R;

import java.util.ArrayList;

public class DistrictSetActionPlansFragment extends Fragment {

    RecyclerView districtActionPlansRecyclerView;
    TextInputEditText newActionPlan;
    MaterialButton addNewActionPlanBtn;
    LinearLayout actionPlansEmpty, districtLoadingActionPlans;

    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_district_set_action_plans, container, false);
        districtActionPlansRecyclerView = rootView.findViewById(R.id.districtActionPlansRecyclerView);
        newActionPlan = rootView.findViewById(R.id.newActionPlan);
        addNewActionPlanBtn = rootView.findViewById(R.id.addNewActionPlanBtn);
        actionPlansEmpty = rootView.findViewById(R.id.actionPlansEmpty);
        districtLoadingActionPlans = rootView.findViewById(R.id.districtLoadingActionPlans);

        database = FirebaseDatabase.getInstance();


        districtActionPlansRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        database.getReference().child("districts").child("huye").child("actionPlans").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                districtLoadingActionPlans.setVisibility(View.GONE);
                if(snapshot.exists())
                {
                    actionPlansEmpty.setVisibility(View.GONE);
                }
                else
                {
                    actionPlansEmpty.setVisibility(View.VISIBLE);
                }
                ArrayList<String> actionPlanKeysList = new ArrayList<>();
                ArrayList<String> allActionPlans = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    allActionPlans.add(dataSnapshot.getValue(String.class));
                    actionPlanKeysList.add(dataSnapshot.getKey());
                }
                districtActionPlansRecyclerView.setAdapter(new ActionPlansAdapter(getContext(), allActionPlans, actionPlanKeysList, true));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                actionPlansEmpty.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });


        addNewActionPlanBtn.setOnClickListener(v -> {
            String givenActionPlan = newActionPlan.getText().toString().trim();
            if(givenActionPlan.equals("")) {
                newActionPlan.setError("Action plan cannot be empty");
                return;
            }
            //add to Firebase.
            dbWriteActionPlan(givenActionPlan);
        });

        return rootView;
    }
    private void dbWriteActionPlan( String actionPlan) {
        DatabaseReference actionPlanRef = database.getReference().child("districts").child("huye").child("actionPlans");
        String actionPlanId = actionPlanRef.push().getKey();
        actionPlanRef.child(actionPlanId).setValue(actionPlan);
        Toast.makeText(getContext(), "Uploaded!", Toast.LENGTH_SHORT).show();
    }
}