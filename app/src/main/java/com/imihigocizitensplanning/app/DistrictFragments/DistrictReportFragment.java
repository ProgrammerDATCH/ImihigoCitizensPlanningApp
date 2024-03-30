package com.imihigocizitensplanning.app.DistrictFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imihigocizitensplanning.app.Common.CitizensReportActivity;
import com.imihigocizitensplanning.app.Common.LeadersActivity;
import com.imihigocizitensplanning.app.District.DistrictActivity;
import com.imihigocizitensplanning.app.Models.Imihigo;
import com.imihigocizitensplanning.app.Models.PendingImihigo;
import com.imihigocizitensplanning.app.UsefullClasses.Categories;
import com.imihigocizitensplanning.app.UsefullClasses.PdfGenerator;
import com.imihigocizitensplanning.app.R;
import com.imihigocizitensplanning.app.UsefullClasses.PendingPdfGenerator;
import com.imihigocizitensplanning.app.UsefullClasses.UsefulFunctions;

import java.util.ArrayList;


public class DistrictReportFragment extends Fragment {

    MaterialButton generateBtn, generateBtnCategory, generateBtnWhich, generateBtnSector;

    FirebaseDatabase database;
    boolean isLoaded = false;
    Spinner reportPlanCategory, reportPlanWhich, reportPlanSector, reportPlanCell;

    String selectedCategory;
    String selectedSector;
    String selectedCell;
    int selectedWhich;

    String currentCells[];

    Button districtLeadersBtn, districtCitizensBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_district_report, container, false);

//        generateBtn = rootView.findViewById(R.id.generateBtn);
        generateBtnCategory = rootView.findViewById(R.id.generateBtnCategory);
        generateBtnSector = rootView.findViewById(R.id.generateBtnSector);
        generateBtnWhich = rootView.findViewById(R.id.generateBtnWhich);

        reportPlanCategory = rootView.findViewById(R.id.reportPlanCategory);
        reportPlanWhich = rootView.findViewById(R.id.reportPlanWhich);
        reportPlanSector = rootView.findViewById(R.id.reportPlanSector);
        reportPlanCell = rootView.findViewById(R.id.reportPlanCell);

        districtCitizensBtn = rootView.findViewById(R.id.districtCitizensBtn);
        districtLeadersBtn = rootView.findViewById(R.id.districtLeadersBtn);

        UsefulFunctions usefulFunctions = new UsefulFunctions();

        String pendingPlanCategories[] = new String[]{Categories.IMIBEREHO_MYIZA, Categories.IMIYOBORERE_MYIZA, Categories.ITERAMBERE_RY_UBUKUNGU};
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, pendingPlanCategories);
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportPlanCategory.setAdapter(categoriesAdapter);

        String planWhich[] = new String[]{"Approved Plans", "Rejected Plans"};
        ArrayAdapter<String> whichAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, planWhich);
        whichAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportPlanWhich.setAdapter(whichAdapter);


        String sectors[] = usefulFunctions.allSectors();
        ArrayAdapter<String> sectorsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, sectors);
        sectorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportPlanSector.setAdapter(sectorsAdapter);


        districtLeadersBtn.setOnClickListener(v ->
        {
            Intent i = new Intent(getContext(), LeadersActivity.class);
            i.putExtra("isFromSector", false);
            startActivity(i);
        });

        districtCitizensBtn.setOnClickListener(v ->
        {
            Intent i = new Intent(getContext(), CitizensReportActivity.class);
            i.putExtra("isFromSector", false);
            startActivity(i);
        });

        reportPlanSector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSector = sectors[position];
                String cells[] = usefulFunctions.allCells(selectedSector);
                currentCells = cells;
                ArrayAdapter<String> cellsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cells);
                cellsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                reportPlanCell.setAdapter(cellsAdapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        reportPlanCell.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCell = currentCells[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        reportPlanWhich.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedWhich = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






        reportPlanCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = pendingPlanCategories[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        database = FirebaseDatabase.getInstance();
//        database.getReference().child("districts").child("huye").child("imihigo").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(!snapshot.exists())
//                {
//                    Toast.makeText(getContext(), "No DATA!", Toast.LENGTH_SHORT).show();
//                }
//                allImihigo = new ArrayList<>();
//                for(DataSnapshot dataSnapshot : snapshot.getChildren())
//                {
//                    Imihigo umuhigo = dataSnapshot.getValue(Imihigo.class);
//                    allImihigo.add(umuhigo);
//                }
//                isLoaded = true;
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
//            }
//        });

//        generateBtn.setOnClickListener(v -> {
//            if(isLoaded)
//            {
//                PdfGenerator.generatePDF(getContext(), allImihigo);
//            }
//            else {
//                Toast.makeText(getContext(), "Wait Network...", Toast.LENGTH_SHORT).show();
//            }
//        });

        generateBtnCategory.setOnClickListener(v -> {
            database.getReference().child("districts").child("huye").child("imihigo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists())
                    {
                        Toast.makeText(getContext(), "No DATA!", Toast.LENGTH_SHORT).show();
                    }
                    ArrayList<Imihigo> allImihigo = new ArrayList<>();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        Imihigo umuhigo = dataSnapshot.getValue(Imihigo.class);
                        if(umuhigo.getPlanCategory().equals(selectedCategory))
                        {
                            allImihigo.add(umuhigo);
                        }
                    }
                    if(allImihigo.size() < 1)
                    {
                        Toast.makeText(getContext(), "No DATA!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    PdfGenerator.generatePDF(getContext(), allImihigo);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        });

        generateBtnWhich.setOnClickListener(v -> {
            if(selectedWhich == 0)
            {
                database.getReference().child("districts").child("huye").child("imihigo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists())
                        {
                            Toast.makeText(getContext(), "No DATA!", Toast.LENGTH_SHORT).show();
                        }
                        ArrayList<Imihigo> allImihigo = new ArrayList<>();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            Imihigo umuhigo = dataSnapshot.getValue(Imihigo.class);
                            allImihigo.add(umuhigo);
                        }
                        if(allImihigo.size() < 1)
                        {
                            Toast.makeText(getContext(), "No DATA!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        PdfGenerator.generatePDF(getContext(), allImihigo);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else
            {
                database.getReference().child("districts").child("huye").child("pendingImihigo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists())
                        {
                            Toast.makeText(getContext(), "No DATA!", Toast.LENGTH_SHORT).show();
                        }
                        ArrayList<PendingImihigo> imihigos = new ArrayList<>();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            PendingImihigo umuhigo = dataSnapshot.getValue(PendingImihigo.class);
                            imihigos.add(umuhigo);
                        }
                        if(imihigos.size() < 1)
                        {
                            Toast.makeText(getContext(), "No DATA!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        PendingPdfGenerator.generatePendingPDF(getContext(), imihigos);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        generateBtnSector.setOnClickListener(v -> {
            database.getReference().child("districts").child("huye").child("pendingImihigo").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!snapshot.exists())
                    {
                        Toast.makeText(getContext(), "No DATA!", Toast.LENGTH_SHORT).show();
                    }
                    ArrayList<PendingImihigo> allImihigo = new ArrayList<>();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        //check cell
                        PendingImihigo umuhigo = dataSnapshot.getValue(PendingImihigo.class);
                        if(umuhigo.getPendingPlanUserCell().equals(selectedCell))
                        {
                            allImihigo.add(umuhigo);
                        }
                    }
                    if(allImihigo.size() < 1)
                    {
                        Toast.makeText(getContext(), "No DATA!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    PendingPdfGenerator.generatePendingPDF(getContext(), allImihigo);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        });

        return rootView;
    }

}