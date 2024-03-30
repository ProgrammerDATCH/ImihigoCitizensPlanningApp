package com.imihigocizitensplanning.app.UserFragments;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imihigocizitensplanning.app.Adapters.ActionPlansAdapter;
import com.imihigocizitensplanning.app.R;

import java.util.ArrayList;


public class UserViewActionPlansFragment extends Fragment {

    FirebaseDatabase database;
    RecyclerView userActionPlansRecyclerView;
    LinearLayout userActionPlansEmpty;
    LinearLayout userLoadingActionPlans;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_view_action_plans, container, false);

        userActionPlansRecyclerView = rootView.findViewById(R.id.userActionPlansRecyclerView);
        userActionPlansEmpty = rootView.findViewById(R.id.userActionPlansEmpty);
        userLoadingActionPlans = rootView.findViewById(R.id.userLoadingActionPlans);
        database = FirebaseDatabase.getInstance();

        userActionPlansRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        database.getReference().child("districts").child("huye").child("actionPlans").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userLoadingActionPlans.setVisibility(View.GONE);
                if(snapshot.exists())
                {
                    userActionPlansEmpty.setVisibility(View.GONE);
                }
                else
                {
                    userActionPlansEmpty.setVisibility(View.VISIBLE);
                }
                ArrayList<String> actionPlanKeysList = new ArrayList<>();
                ArrayList<String> allActionPlans = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    allActionPlans.add(dataSnapshot.getValue(String.class));
                    actionPlanKeysList.add(dataSnapshot.getKey());
                }
                userActionPlansRecyclerView.setAdapter(new ActionPlansAdapter(getContext(), allActionPlans, actionPlanKeysList, false));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userActionPlansEmpty.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}