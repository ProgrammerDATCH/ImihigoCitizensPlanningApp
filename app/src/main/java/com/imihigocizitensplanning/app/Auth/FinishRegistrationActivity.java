package com.imihigocizitensplanning.app.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.imihigocizitensplanning.app.Cell.CellActivity;
import com.imihigocizitensplanning.app.Citizen.UserActivity;
import com.imihigocizitensplanning.app.District.DistrictActivity;
import com.imihigocizitensplanning.app.Models.User;
import com.imihigocizitensplanning.app.R;
import com.imihigocizitensplanning.app.Sector.SectorActivity;
import com.imihigocizitensplanning.app.UsefullClasses.Categories;
import com.imihigocizitensplanning.app.UsefullClasses.Constants;

public class FinishRegistrationActivity extends AppCompatActivity {

    TextInputEditText userNID, userNames, userSector, userCell, userPhone, userGender, userOccupation, userPassword, userPasswordRepeat, userEmail;

    MaterialButton registNowBtn;
    ProgressBar loadingLogin;

    TextView errorLogin, welcomeMsg;

    Spinner districtCategory;
    LinearLayout districtCategoryLayout;

    FirebaseAuth auth;
    FirebaseDatabase database;

    String currentDistrictLeadercategory = Categories.IMIBEREHO_MYIZA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_registration);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        userNID = findViewById(R.id.userNID);
        userNames = findViewById(R.id.userNames);
        userSector = findViewById(R.id.userSector);
        userCell = findViewById(R.id.userCell);
        userPhone = findViewById(R.id.userPhone);
        userGender = findViewById(R.id.userGender);
        userOccupation = findViewById(R.id.userOccupation);

        userPassword = findViewById(R.id.userPassword);
        userPasswordRepeat = findViewById(R.id.userPasswordRepeat);
        userEmail = findViewById(R.id.userEmail);
        registNowBtn = findViewById(R.id.registNowBtn);
        loadingLogin = findViewById(R.id.loadingLogin);
        errorLogin = findViewById(R.id.errorLogin);
        welcomeMsg = findViewById(R.id.welcomeMsg);

        districtCategoryLayout = findViewById(R.id.districtCategoryLayout);
        districtCategory = findViewById(R.id.districtCategory);

        Intent intent = getIntent();
        String names = intent.getStringExtra("names");
        String nationalId = intent.getStringExtra("nationalId");
        String sector = intent.getStringExtra("sector");
        String cell = intent.getStringExtra("cell");
        String gender = intent.getStringExtra("gender");
        String phone = intent.getStringExtra("phone");
        String occupation = intent.getStringExtra("occupation");
        String userCategory = intent.getStringExtra("userCategory");

        welcomeMsg.setText("Finish registration as " + userCategory);

        userNID.setText(nationalId);
        userNames.setText(names);
        userSector.setText(sector);
        userCell.setText(cell);
        userPhone.setText(phone);
        userGender.setText(gender);
        userOccupation.setText(occupation);

        userNID.setEnabled(false);
        userNames.setEnabled(false);
        userSector.setEnabled(false);
        userCell.setEnabled(false);
        userPhone.setEnabled(false);
        userGender.setEnabled(false);
        userOccupation.setEnabled(false);


        String pendingPlanCategories[] = new String[]{Categories.IMIBEREHO_MYIZA, Categories.IMIYOBORERE_MYIZA, Categories.ITERAMBERE_RY_UBUKUNGU};
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pendingPlanCategories);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtCategory.setAdapter(categoriesAdapter);

        if(userCategory.equals(Constants.DISTRICT_LEADER))
        {
            districtCategoryLayout.setVisibility(View.VISIBLE);
        }

        districtCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentDistrictLeadercategory = pendingPlanCategories[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        //Validate password

        registNowBtn.setOnClickListener(v -> {
            if (userPassword.getText().toString().equals(userPasswordRepeat.getText().toString())) {
                if (userEmail.getText().toString().trim().equals("")) {
                    userEmail.setError("Email is required");
                    return;
                }
                String email = userEmail.getText().toString().trim();
                String password = userPassword.getText().toString().trim();
                User user = new User(nationalId, names, sector, cell, gender, phone, occupation, userCategory);
                RegisterUser(email, password, user, userCategory);

            } else {
                userPassword.setError("Not match");
                userPasswordRepeat.setError("Not match");
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void RegisterUser(String email, String password, User user, String userCategory) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingLogin.setVisibility(View.VISIBLE);
                errorLogin.setVisibility(View.GONE);
            }
        });

        Class whereToGo;
        switch (userCategory) {
            case Constants.CITIZEN:
                whereToGo = UserActivity.class;
                break;
            case Constants.CELL_LEADER:
                whereToGo = CellActivity.class;
                break;
            case Constants.SECTOR_LEADER:
                whereToGo = SectorActivity.class;
                break;
            case Constants.DISTRICT_LEADER:
                whereToGo = DistrictActivity.class;
                break;
            default:
                whereToGo = UserActivity.class;
                break;
        }

        Class finalWhereToGo = whereToGo;
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingLogin.setVisibility(View.GONE);
                    }
                });
                if (task.isSuccessful()) {
                    String uid = task.getResult().getUser().getUid();
                    database.getReference().child("Users").child(uid).setValue(user);
                    //if District add category:
                    if(userCategory.equals(Constants.DISTRICT_LEADER))
                    {
                        database.getReference().child("DistrictLeadersCategory").child(uid).setValue(currentDistrictLeadercategory);
                        Intent i = new Intent(FinishRegistrationActivity.this, finalWhereToGo);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Intent i = new Intent(FinishRegistrationActivity.this, finalWhereToGo);
                        startActivity(i);
                        finish();
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            errorLogin.setText(task.getException().getMessage());
                            errorLogin.setVisibility(View.VISIBLE);
                            Toast.makeText(FinishRegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}