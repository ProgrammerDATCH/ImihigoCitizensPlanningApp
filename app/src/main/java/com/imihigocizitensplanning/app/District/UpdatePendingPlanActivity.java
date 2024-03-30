package com.imihigocizitensplanning.app.District;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.imihigocizitensplanning.app.Models.Imihigo;
import com.imihigocizitensplanning.app.Models.Notifikation;
import com.imihigocizitensplanning.app.Models.PendingImihigo;
import com.imihigocizitensplanning.app.R;
import com.imihigocizitensplanning.app.UsefullClasses.Categories;

public class UpdatePendingPlanActivity extends AppCompatActivity {

    TextInputEditText newSuggestedPlanName, newSuggestedPlanDesc;
    MaterialButton newSuggestPlanBtn;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    Spinner newSuggestedPlanCategory;
    String selectedPlanCategory = Categories.IMIBEREHO_MYIZA;
    String planUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pending_plan);

        newSuggestedPlanName = findViewById(R.id.newSuggestedPlanName);
        newSuggestedPlanDesc = findViewById(R.id.newSuggestedPlanDesc);
        newSuggestPlanBtn = findViewById(R.id.newSuggestPlanBtn);
        newSuggestedPlanCategory = findViewById(R.id.newSuggestedPlanCategory);


        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String oldPlanKey = intent.getStringExtra("oldPlanKey");
        String umuhigoName = intent.getStringExtra("umuhigoName");
        int fromLevel = intent.getIntExtra("fromLevel", 0);


        String pendingPlanCategories[] = new String[]{Categories.IMIBEREHO_MYIZA, Categories.IMIYOBORERE_MYIZA, Categories.ITERAMBERE_RY_UBUKUNGU};
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pendingPlanCategories);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        newSuggestedPlanCategory.setAdapter(categoriesAdapter);


        database.getReference().child("districts").child("huye").child("pendingImihigo").child(oldPlanKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PendingImihigo umuhigo = snapshot.getValue(PendingImihigo.class);
                newSuggestedPlanName.setText(umuhigo.getPendingPlanName());
                newSuggestedPlanDesc.setText(umuhigo.getPendingPlanDesc());
                String planCategory = umuhigo.getPendingPlanCategory();
                planUserId = umuhigo.getPendingUserId();
                int position = 0;
                if (planCategory == Categories.IMIYOBORERE_MYIZA) {
                    position = 1;
                } else if (planCategory == Categories.ITERAMBERE_RY_UBUKUNGU) {
                    position = 2;
                }
                newSuggestedPlanCategory.setSelection(position);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdatePendingPlanActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        newSuggestedPlanCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPlanCategory = pendingPlanCategories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        newSuggestPlanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPlanName = newSuggestedPlanName.getText().toString().trim();
                String newPlanDesc = newSuggestedPlanDesc.getText().toString().trim();
                String newPlanCategory = selectedPlanCategory;
                boolean errorOccured = false;


                if (newPlanName.equals("")) {
                    newSuggestedPlanName.setError("Enter Plan Name please!");
                    errorOccured = true;
                }
                if (newPlanDesc.equals("")) {
                    newSuggestedPlanDesc.setError("Enter Plan Description please!");
                    errorOccured = true;
                }

                if (errorOccured) {
                    return;
                }
                int newLevel = fromLevel + 1;
                database.getReference().child("districts").child("huye").child("pendingImihigo").child(oldPlanKey).child("pendingPlanProgress").setValue(newLevel);
                //update name and description and category
                database.getReference().child("districts").child("huye").child("pendingImihigo").child(oldPlanKey).child("pendingPlanName").setValue(newPlanName);
                database.getReference().child("districts").child("huye").child("pendingImihigo").child(oldPlanKey).child("pendingPlanDesc").setValue(newPlanDesc);
                database.getReference().child("districts").child("huye").child("pendingImihigo").child(oldPlanKey).child("pendingPlanCategory").setValue(newPlanCategory);
                Toast.makeText(UpdatePendingPlanActivity.this, umuhigoName + " Approved!", Toast.LENGTH_SHORT).show();
                //send notification
                String msg;
                switch (fromLevel)
                {
                    case 0:
                        msg = "Cell approved your Plan!";
                        break;
                    case 1:
                        msg = "Sector Approved your Plan!";
                        break;
                    case 2:
                        msg = "Wow!, District Approved your Plan!";
                        break;
                    default:
                        msg = "Your plan have been approved";
                        break;
                }
                Notifikation newNotification = new Notifikation(msg, false);
                DatabaseReference notifyRef = database.getReference().child("Notifications").child(planUserId);
                String key = notifyRef.push().getKey();
                notifyRef.child(key).setValue(newNotification);
                finish();
            }
        });
    }
}