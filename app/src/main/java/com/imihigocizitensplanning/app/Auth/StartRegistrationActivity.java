package com.imihigocizitensplanning.app.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.imihigocizitensplanning.app.MainActivity;
import com.imihigocizitensplanning.app.UsefullClasses.ApiDataFetcher;
import com.imihigocizitensplanning.app.R;
import com.imihigocizitensplanning.app.UsefullClasses.Constants;
import com.imihigocizitensplanning.app.UsefullClasses.NationalIDFormatter;

import org.json.JSONException;
import org.json.JSONObject;

public class StartRegistrationActivity extends AppCompatActivity {

    TextInputEditText citizenNID;
    MaterialButton checkNID, registBtn;
    MaterialTextView result;
    ProgressBar loading;
    boolean isValid = false;

    Spinner registCategory;

    String id, nationalId, names, sector, cell, gender, phone, occupation;
    String selectedUserCategory = Constants.CITIZEN;
    String lastSelectedUserCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citizen_registration);

        citizenNID = findViewById(R.id.citizenNID);
        checkNID = findViewById(R.id.checkNID);
        result = findViewById(R.id.result);
        loading = findViewById(R.id.loading);
        registBtn = findViewById(R.id.registBtn);

        registCategory = findViewById(R.id.registCategory);


        String[] userCategories = new String[]{Constants.CITIZEN, Constants.CELL_LEADER, Constants.SECTOR_LEADER, Constants.DISTRICT_LEADER};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, userCategories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        registCategory.setAdapter(adapter);

        registCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUserCategory = userCategories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        registBtn.setOnClickListener(v -> {
            if (isValid) {
                Intent i = new Intent(StartRegistrationActivity.this, FinishRegistrationActivity.class);
                i.putExtra("nationalId", nationalId);
                i.putExtra("names", names);
                i.putExtra("sector", sector);
                i.putExtra("cell", cell);
                i.putExtra("gender", gender);
                i.putExtra("phone", phone);
                i.putExtra("occupation", occupation);
                i.putExtra("userCategory", lastSelectedUserCategory);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(this, "Check your National ID first.", Toast.LENGTH_SHORT).show();
            }
        });


        checkNID.setOnClickListener(v -> {

            String NID = citizenNID.getText().toString().trim();
            if (NID.isEmpty()) {
                citizenNID.setError("ID is required");
                return;
            }
            if (NID.length() != 16) {
                citizenNID.setError("ID is must be 16 characters");
                Toast.makeText(this, "ID is must be 16 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            String apiUrl;
            switch (selectedUserCategory) {
                case Constants.CITIZEN:
                    apiUrl = "https://rwanda-citizens-api.000webhostapp.com/is-citizen.php?nid=" + NID;
                    break;

                case Constants.CELL_LEADER:
                    apiUrl = "https://rwanda-citizens-api.000webhostapp.com/is-cell.php?nid=" + NID;
                    break;

                case Constants.SECTOR_LEADER:
                    apiUrl = "https://rwanda-citizens-api.000webhostapp.com/is-sector.php?nid=" + NID;
                    break;

                case Constants.DISTRICT_LEADER:
                    apiUrl = "https://rwanda-citizens-api.000webhostapp.com/is-district.php?nid=" + NID;
                    break;

                default:
                    apiUrl = "https://rwanda-citizens-api.000webhostapp.com/is-citizen.php?nid=" + NID;
                    break;
            }

            //call API
            ApiDataFetcher apiDataFetcher = new ApiDataFetcher();

            loading.setVisibility(View.VISIBLE);
            result.setText("");
            result.setTextColor(Color.GREEN);
            isValid = false;
            registBtn.setVisibility(View.GONE);
            apiDataFetcher.fetchDataFromApi(apiUrl, new ApiDataFetcher.DataFetchCallback() {
                @Override
                public void onSuccess(String data) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading.setVisibility(View.GONE);
                        }
                    });
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        boolean resultStatus = jsonObject.getBoolean("ResultStatus");
                        if (resultStatus) {
                            JSONObject resultData = jsonObject.getJSONObject("ResultData");
                            id = resultData.getString("id");
                            nationalId = resultData.getString("national_id");
                            names = resultData.getString("names");
                            sector = resultData.getString("sector");
                            cell = resultData.getString("cell");
                            gender = resultData.getString("gender");
                            phone = resultData.getString("phone");

                            occupation = (selectedUserCategory.equals(Constants.CITIZEN)) ? resultData.getString("occupation") : selectedUserCategory;

                            lastSelectedUserCategory = selectedUserCategory;

                            runOnUiThread(() -> {
                                isValid = true;
                                registBtn.setVisibility(View.VISIBLE);
                                result.setText("Names: " + names + "\n\nSector: " + sector + "\n\nCell: " + cell + "\n\ngender: " + gender + "\n\nphone: " + phone + "\n\noccupation: " + occupation);
                            });
                        } else {
                            String errorData = jsonObject.getString("ResultData");
                            runOnUiThread(() -> {
                                result.setTextColor(Color.RED);
                                result.setText("Error: " + errorData);
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loading.setVisibility(View.GONE);
                        }
                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StartRegistrationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(StartRegistrationActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}