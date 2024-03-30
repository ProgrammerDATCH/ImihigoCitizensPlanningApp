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

public class UserViewDeniedPlansFragment extends Fragment {

    RecyclerView userSuggestedPlansRecyclerView;
    FirebaseDatabase database;
    FirebaseAuth auth;
    LinearLayout userSuggestedLoadingPlansProgress, userSuggestedEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_view_denied_plans, container, false);
        userSuggestedLoadingPlansProgress = rootView.findViewById(R.id.userSuggestedLoadingPlansProgress);
        userSuggestedEmpty = rootView.findViewById(R.id.userSuggestedEmpty);
        userSuggestedPlansRecyclerView = rootView.findViewById(R.id.userSuggestedPlansRecyclerView);
        userSuggestedPlansRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        loadImihigo();

        return rootView;
    }

    public  void loadImihigo()
    {
        database.getReference().child("districts").child("huye").child("pendingImihigo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    userSuggestedEmpty.setVisibility(View.GONE);
                }
                else
                {
                    userSuggestedEmpty.setVisibility(View.VISIBLE);
                }

                String currentUserId = auth.getUid();

                ArrayList<String> planKeysList = new ArrayList<>();
                ArrayList<PendingImihigo> allImihigo = new ArrayList<>();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    PendingImihigo umuhigo = dataSnapshot.getValue(PendingImihigo.class);

                    if(currentUserId.equalsIgnoreCase(umuhigo.getPendingUserId()))
                    {
                        allImihigo.add(umuhigo);
                        planKeysList.add(dataSnapshot.getKey());
                    }
                }
                if(allImihigo.size() < 1)
                {
                    userSuggestedEmpty.setVisibility(View.VISIBLE);
                }
                userSuggestedLoadingPlansProgress.setVisibility(View.GONE);
                userSuggestedPlansRecyclerView.setAdapter(new PendingPlansAdapter(getContext(), allImihigo, planKeysList, -5));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                userSuggestedLoadingPlansProgress.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}