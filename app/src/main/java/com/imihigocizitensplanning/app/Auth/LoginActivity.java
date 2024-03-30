package com.imihigocizitensplanning.app.Auth;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imihigocizitensplanning.app.Cell.CellActivity;
import com.imihigocizitensplanning.app.Citizen.UserActivity;
import com.imihigocizitensplanning.app.District.DistrictActivity;
import com.imihigocizitensplanning.app.MainActivity;
import com.imihigocizitensplanning.app.R;
import com.imihigocizitensplanning.app.Sector.SectorActivity;
import com.imihigocizitensplanning.app.UsefullClasses.Constants;

public class LoginActivity extends AppCompatActivity {
    MaterialButton loginDistrictBtn;
    TextInputEditText districtEmail, districtPassword;
    ProgressBar loginDistrictLoading;
    TextView errorDistrictTxt;
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_login);

        loginDistrictBtn = findViewById(R.id.loginDistrictBtn);
        districtEmail = findViewById(R.id.districtEmail);
        districtPassword = findViewById(R.id.districtPassword);
        loginDistrictLoading = findViewById(R.id.loginDistrictLoading);
        errorDistrictTxt = findViewById(R.id.errorDistrictTxt);


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        if (mAuth.getCurrentUser() != null) {
            loginDistrictLoading.setVisibility(View.VISIBLE);
            String uid = mAuth.getCurrentUser().getUid();
            database.getReference().child("Users").child(uid).child("userCategory").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loginDistrictLoading.setVisibility(View.GONE);
                        }
                    });
                    if (!snapshot.exists()) {
                        Toast.makeText(LoginActivity.this, "User Not Found!", Toast.LENGTH_SHORT).show();
                    } else {
                        String userCategory = snapshot.getValue(String.class);
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
                            case Constants.DISTRICT_LEADER:
                                whereToGo = DistrictActivity.class;
                            default:
                                whereToGo = MainActivity.class;
                                break;
                        }
                        if(whereToGo == MainActivity.class)
                        {
                            return;
                        }
                        else
                        {
                            Intent i = new Intent(LoginActivity.this, whereToGo);
                            startActivity(i);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loginDistrictLoading.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }

        loginDistrictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDistrictLoading.setVisibility(View.VISIBLE);
                loginDistrictBtn.setVisibility(View.GONE);
                errorDistrictTxt.setText("");
                String givenDistrictEmail = districtEmail.getText().toString().trim();
                String givenDistrictPassword = districtPassword.getText().toString().trim();
                if (givenDistrictEmail.equals("")) {
                    districtEmail.setError("Provide Email Please!");
                    errorDistrictTxt.append("\nProvide Email Please!");
                    districtEmail.setFocusable(true);
                    if (givenDistrictPassword.equals("")) {
                        districtPassword.setError("Provide Password Please!");
                        errorDistrictTxt.append("\nProvide Password Please!");
                    }
                    loginDistrictLoading.setVisibility(View.GONE);
                    loginDistrictBtn.setVisibility(View.VISIBLE);
                    return;
                }
                if (givenDistrictPassword.equals("")) {
                    districtPassword.setError("Provide Password Please!");
                    errorDistrictTxt.append("\nProvide Password Please!");
                    districtPassword.setFocusable(true);
                    loginDistrictLoading.setVisibility(View.GONE);
                    loginDistrictBtn.setVisibility(View.VISIBLE);
                    return;
                }

                mAuth.signInWithEmailAndPassword(givenDistrictEmail, givenDistrictPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //check user category:
                                    String uid = mAuth.getCurrentUser().getUid();
                                    database.getReference().child("Users").child(uid).child("userCategory").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if (!snapshot.exists()) {
                                                Toast.makeText(LoginActivity.this, "User Not Found!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                String userCategory = snapshot.getValue(String.class);
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
                                                Intent i = new Intent(LoginActivity.this, whereToGo);
                                                startActivity(i);
                                                finish();
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                } else {
                                    errorDistrictTxt.setText("Login Failed:\n" + task.getException().getMessage());
                                    loginDistrictLoading.setVisibility(View.GONE);
                                    loginDistrictBtn.setVisibility(View.VISIBLE);
                                }
                            }
                        });
            }


        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}