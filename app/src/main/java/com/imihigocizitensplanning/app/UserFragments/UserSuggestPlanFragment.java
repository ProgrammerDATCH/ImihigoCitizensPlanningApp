package com.imihigocizitensplanning.app.UserFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imihigocizitensplanning.app.Models.PendingImihigo;
import com.imihigocizitensplanning.app.Models.User;
import com.imihigocizitensplanning.app.R;
import com.imihigocizitensplanning.app.UsefullClasses.Categories;
import com.imihigocizitensplanning.app.UsefullClasses.UsefulFunctions;

public class UserSuggestPlanFragment extends Fragment {



    TextInputEditText suggestedPlanName, suggestedPlanDesc;
    MaterialButton suggestPlanBtn;
    LinearLayout notAllowedLayout, allowedLayout;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    Spinner suggestedPlanCategory;

    String selectedPlanCategory;
    User currentUser = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_suggest_plan, container, false);

        suggestedPlanName = rootView.findViewById(R.id.suggestedPlanName);
        suggestedPlanDesc = rootView.findViewById(R.id.suggestedPlanDesc);
        suggestPlanBtn = rootView.findViewById(R.id.suggestPlanBtn);
        allowedLayout = rootView.findViewById(R.id.allowedLayout);
        notAllowedLayout = rootView.findViewById(R.id.notAllowedLayout);
        suggestedPlanCategory = rootView.findViewById(R.id.suggestedPlanCategory);



        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String uid = mAuth.getCurrentUser().getUid();

        database.getReference().child("Users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        database.getReference().child("districts").child("huye").child("season").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int currentSeason = snapshot.getValue(Integer.class);
                if(currentSeason == 1)
                {
                    allowedLayout.setVisibility(View.GONE);
                    notAllowedLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Time to implement", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });


        String pendingPlanCategories[] = new String[]{Categories.IMIBEREHO_MYIZA, Categories.IMIYOBORERE_MYIZA, Categories.ITERAMBERE_RY_UBUKUNGU};
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, pendingPlanCategories);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        suggestedPlanCategory.setAdapter(categoriesAdapter);

        suggestedPlanCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPlanCategory = pendingPlanCategories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        suggestPlanBtn.setOnClickListener(v -> {
            boolean errorOccured = false;
            String newPlanName = suggestedPlanName.getText().toString().trim();
            String newPlanDesc = suggestedPlanDesc.getText().toString().trim();
            String newPlanCategory = selectedPlanCategory;
            String newPlanUserId = mAuth.getCurrentUser().getUid();



            if(newPlanName.equals(""))
            {
                suggestedPlanName.setError("Enter Plan Name please!");
                errorOccured = true;
            }
            if(newPlanDesc.equals(""))
            {
                suggestedPlanDesc.setError("Enter Plan Description please!");
                errorOccured = true;
            }
            if(newPlanCategory.equals(""))
            {
                Toast.makeText(getContext(),"Select category please!", Toast.LENGTH_SHORT).show();
                errorOccured = true;
            }

            if(errorOccured){return;}


            if(currentUser == null){
                Toast.makeText(getContext(), "Waiting Network...", Toast.LENGTH_SHORT).show();
                return;}
            String newPlanUserNames = currentUser.getNames();
            String newPlanUserPhone = currentUser.getPhone();
            String newPlanUserSector = currentUser.getSector();
            String newPlanUserCell = currentUser.getCell();

            PendingImihigo umuhigo = new PendingImihigo(newPlanName, newPlanCategory, newPlanDesc, newPlanUserId, newPlanUserNames, newPlanUserPhone, newPlanUserSector, newPlanUserCell, 0);
            dbWritePlan(umuhigo);

            suggestedPlanName.setText("");
            suggestedPlanDesc.setText("");

        });

        return rootView;
    }

    private void dbWritePlan( PendingImihigo umuhigo) {
        DatabaseReference umuhigoRef = database.getReference().child("districts").child("huye").child("pendingImihigo");
        String umuhigoId = umuhigoRef.push().getKey();
        umuhigoRef.child(umuhigoId).setValue(umuhigo);
        Toast.makeText(getContext(), "Sent!", Toast.LENGTH_SHORT).show();
    }
}