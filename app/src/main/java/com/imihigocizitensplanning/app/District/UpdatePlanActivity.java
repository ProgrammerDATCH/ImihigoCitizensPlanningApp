package com.imihigocizitensplanning.app.District;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imihigocizitensplanning.app.Models.Imihigo;
import com.imihigocizitensplanning.app.R;
import com.imihigocizitensplanning.app.UsefullClasses.Categories;

import java.util.Calendar;

public class UpdatePlanActivity extends AppCompatActivity {

    int givenPlanLevel = 0;
    private Calendar calendar;
    String currentDistrictName = "Huye";
    TextInputEditText givenPlanStartDate, givenPlanEndDate, givenPlanName, givenPlanBudget, givenPlanTarget, givenPlanTargetKey, givenNewPlanLevel;
    MaterialButton uploadPlanBtn;

    FirebaseDatabase database;
    String oldPlanKey;

    String currentCategory = Categories.IMIBEREHO_MYIZA;
    String userKey = "0";
    String planCategory = Categories.IMIBEREHO_MYIZA;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_plan);

        givenPlanTarget = findViewById(R.id.givenNewPlanTarget);
        givenPlanTargetKey = findViewById(R.id.givenNewPlanTargetKey);
        givenPlanStartDate = findViewById(R.id.givenNewPlanStartDate);
        givenPlanEndDate = findViewById(R.id.givenNewPlanEndDate);
        givenPlanName = findViewById(R.id.givenNewPlanName);
        givenPlanBudget = findViewById(R.id.givenNewPlanBudget);
        givenNewPlanLevel = findViewById(R.id.givenNewPlanLevel);
        uploadPlanBtn = findViewById(R.id.updatePlanBtn);

        database = FirebaseDatabase.getInstance();
        calendar = Calendar.getInstance();


        Intent intent = getIntent();
        boolean fromUpdate = intent.getBooleanExtra("fromUpdate", false);

        if(fromUpdate)
        {
            userKey = intent.getStringExtra("userKey");
            database.getReference().child("districts").child("huye").child("imihigo").child(userKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Imihigo umuhigo = snapshot.getValue(Imihigo.class);
                    givenPlanName.setText(umuhigo.getPlanName());
                    givenPlanTarget.setText(String.valueOf(umuhigo.getPlanTarget()));
                    givenPlanTargetKey.setText(umuhigo.getPlanTargetKey());
                    givenPlanStartDate.setText(umuhigo.getPlanStartDate());
                    givenPlanEndDate.setText(umuhigo.getPlanEndDate());
                    givenPlanBudget.setText(String.valueOf(umuhigo.getPlanBudget()));
                    givenNewPlanLevel.setText(String.valueOf(umuhigo.getPlanLevel()));
                    currentCategory = umuhigo.getPlanCategory();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(UpdatePlanActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            String planName = intent.getStringExtra("planName");
            planCategory = intent.getStringExtra("planCategory");
            oldPlanKey = intent.getStringExtra("oldPlanKey");
            givenPlanName.setText(planName);
        }



        givenPlanStartDate.setOnClickListener(v -> {
            showDatePickerDialog(givenPlanStartDate);
        });
        givenPlanEndDate.setOnClickListener(v -> {
            showDatePickerDialog(givenPlanEndDate);
        });

        uploadPlanBtn.setOnClickListener(v -> {
            boolean errorOccured = false;
            String newPlanName = givenPlanName.getText().toString().trim();
            String newPlanStartDate = givenPlanStartDate.getText().toString().trim();
            String newPlanEndDate = givenPlanEndDate.getText().toString().trim();
            String newPlanTargetKey = givenPlanTargetKey.getText().toString().trim();


            if(newPlanName.equals(""))
            {
                givenPlanName.setError("Enter Plan Name please!");
                errorOccured = true;
            }
            if(newPlanStartDate.equals(""))
            {
                givenPlanStartDate.setError("Enter Plan Start Date please!");
                errorOccured = true;
            }
            if(newPlanEndDate.equals(""))
            {
                givenPlanEndDate.setError("Enter Plan End Date please!");
                errorOccured = true;
            }
            if(givenPlanBudget.getText().toString().trim().equals(""))
            {
                givenPlanBudget.setError("Enter Plan Budget Please!");
                errorOccured = true;
            }
            if(givenPlanTarget.getText().toString().trim().equals(""))
            {
                givenPlanTarget.setError("Enter Plan Target Please!");
                errorOccured = true;
            }
            if(givenNewPlanLevel.getText().toString().trim().equals(""))
            {
                givenNewPlanLevel.setError("Enter Plan Level Please!");
                errorOccured = true;
            }
            if(newPlanTargetKey.equals(""))
            {
                givenPlanTargetKey.setError("Enter Plan Target Word Please!");
                errorOccured = true;
            }

            if(errorOccured){return;}

            int newPlanBudget = Integer.parseInt(givenPlanBudget.getText().toString().trim());
            int newPlanTarget = Integer.parseInt(givenPlanTarget.getText().toString());
            int newPlanLevel = Integer.parseInt(givenNewPlanLevel.getText().toString().trim());
            String newPlanImageLink = "";

            if(newPlanTarget < 1)
            {
                Toast.makeText(this, "Target can not be Negative number or Zero!", Toast.LENGTH_LONG).show();
                return;
            }

            if(fromUpdate)
            {
                Imihigo newUmuhigo = new Imihigo(newPlanName, newPlanStartDate, newPlanEndDate, currentDistrictName, newPlanBudget, newPlanTarget, newPlanTargetKey, newPlanImageLink, newPlanLevel, currentCategory);
                dbUpdatePlan(newUmuhigo, userKey);
            }
            else
            {
                Imihigo newUmuhigo = new Imihigo(newPlanName, newPlanStartDate, newPlanEndDate, currentDistrictName, newPlanBudget, newPlanTarget, newPlanTargetKey, newPlanImageLink, newPlanLevel, planCategory);
                dbWritePlan(newUmuhigo, oldPlanKey);
            }


            givenPlanName.setText("");
            givenPlanStartDate.setText("");
            givenPlanEndDate.setText("");
            givenPlanTargetKey.setText("");
            givenPlanBudget.setText("");
            givenPlanTarget.setText("");
            givenNewPlanLevel.setText("");

            //orient user accordingly...
//            Intent i = new Intent(UpdatePlanActivity.this, DistrictActivity.class);
//            startActivity(i);
            finish();

        });
    }

    public void showDatePickerDialog(TextInputEditText to) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (datePicker, year1, month1, day1) -> {
                    String formattedMonth = (month1 + 1 < 10) ? "0" + (month1 + 1) : String.valueOf(month1 + 1);
                    String formattedDay = (day1 < 10) ? "0" + day1 : String.valueOf(day1);
                    String selectedDate = formattedDay + "-" + formattedMonth + "-" + year1;
                    to.setText(selectedDate);
                    to.setError(null);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void dbWritePlan(Imihigo umuhigo, String oldPlanKey) {
        DatabaseReference umuhigoRef = database.getReference().child("districts").child("huye").child("imihigo");
        String umuhigoId = umuhigoRef.push().getKey();
        umuhigoRef.child(umuhigoId).setValue(umuhigo);
        Toast.makeText(this, "Uploaded!", Toast.LENGTH_SHORT).show();
        //change progress
        database.getReference().child("districts").child("huye").child("pendingImihigo").child(oldPlanKey).child("pendingPlanProgress").setValue(3);
    }
    private void dbUpdatePlan(Imihigo umuhigo, String keyToUpdate) {
        if(keyToUpdate.equals("0"))
        {
            Toast.makeText(this, "Error Ocuured! Try again later.", Toast.LENGTH_SHORT).show();
            return;
        }
        DatabaseReference umuhigoRef = database.getReference().child("districts").child("huye").child("imihigo");
        umuhigoRef.child(keyToUpdate).setValue(umuhigo);
        Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
    }
}