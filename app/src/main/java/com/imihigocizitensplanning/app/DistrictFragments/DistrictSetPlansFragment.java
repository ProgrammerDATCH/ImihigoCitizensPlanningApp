package com.imihigocizitensplanning.app.DistrictFragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.imihigocizitensplanning.app.R;
import com.imihigocizitensplanning.app.UsefullClasses.Categories;

import java.util.Calendar;

public class DistrictSetPlansFragment extends Fragment{


    int givenPlanLevel = 0;
    private Calendar calendar;
    String currentDistrictName = "Huye";
    TextInputEditText givenPlanStartDate, givenPlanEndDate, givenPlanName, givenPlanBudget, givenPlanTarget, givenPlanTargetKey;
    MaterialButton uploadPlanBtn;

    FirebaseDatabase database;
    FirebaseAuth auth;
    String districtLeaderCategory = Categories.IMIBEREHO_MYIZA;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_district_set_plans, container, false);

        givenPlanTarget = rootView.findViewById(R.id.givenPlanTarget);
        givenPlanTargetKey = rootView.findViewById(R.id.givenPlanTargetKey);
        givenPlanStartDate = rootView.findViewById(R.id.givenPlanStartDate);
        givenPlanEndDate = rootView.findViewById(R.id.givenPlanEndDate);
        givenPlanName = rootView.findViewById(R.id.givenPlanName);
        givenPlanBudget = rootView.findViewById(R.id.givenPlanBudget);
        uploadPlanBtn = rootView.findViewById(R.id.uploadPlanBtn);





        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        database.getReference().child("DistrictLeadersCategory").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                districtLeaderCategory = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        calendar = Calendar.getInstance();


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
            if(newPlanTargetKey.equals(""))
            {
                givenPlanTargetKey.setError("Enter Plan Target Word Please!");
                errorOccured = true;
            }

            if(errorOccured){return;}

            int newPlanBudget = Integer.parseInt(givenPlanBudget.getText().toString().trim());
            int newPlanTarget = Integer.parseInt(givenPlanTarget.getText().toString());
            int newPlanLevel = 0;
            String newPlanImageLink = "";

            if(newPlanTarget < 1)
            {
                Toast.makeText(getContext(), "Target can not be Negative number or Zero!", Toast.LENGTH_LONG).show();
                return;
            }

            Imihigo newUmuhigo = new Imihigo(newPlanName, newPlanStartDate, newPlanEndDate, currentDistrictName, newPlanBudget, newPlanTarget, newPlanTargetKey, newPlanImageLink, newPlanLevel, districtLeaderCategory);
            dbWritePlan(newUmuhigo);

            givenPlanName.setText("");
            givenPlanStartDate.setText("");
            givenPlanEndDate.setText("");
            givenPlanTargetKey.setText("");
            givenPlanBudget.setText("");
            givenPlanTarget.setText("");

        });

        return rootView;
    }


    public void showDatePickerDialog(TextInputEditText to) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
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

    private void dbWritePlan( Imihigo umuhigo) {
        DatabaseReference umuhigoRef = database.getReference().child("districts").child("huye").child("imihigo");
        String umuhigoId = umuhigoRef.push().getKey();
        umuhigoRef.child(umuhigoId).setValue(umuhigo);
        Toast.makeText(getContext(), "Uploaded!", Toast.LENGTH_SHORT).show();
    }

}