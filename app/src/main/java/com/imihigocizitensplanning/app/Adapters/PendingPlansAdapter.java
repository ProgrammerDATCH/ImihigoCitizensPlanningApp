package com.imihigocizitensplanning.app.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imihigocizitensplanning.app.Models.PendingImihigo;
import com.imihigocizitensplanning.app.Models.User;
import com.imihigocizitensplanning.app.R;
import com.imihigocizitensplanning.app.UsefullClasses.UsefulFunctions;

import java.util.ArrayList;

public class PendingPlansAdapter extends RecyclerView.Adapter<PendingPlansViewHolder> {

    Context context;
    ArrayList<PendingImihigo> plans;
    ArrayList<String> planKeysList;
    int fromLevel;

    public PendingPlansAdapter(Context context, ArrayList<PendingImihigo> plans,  ArrayList<String> planKeysList, int fromLevel) {
        this.context = context;
        this.plans = plans;
        this.planKeysList = planKeysList;
        this.fromLevel = fromLevel;
    }

    @NonNull
    @Override
    public PendingPlansViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PendingPlansViewHolder(LayoutInflater.from(context).inflate(R.layout.pending_plan_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PendingPlansViewHolder holder, int position) {

        holder.pendingPlanName.setText("Umuhigo: " + plans.get(position).getPendingPlanName());
        holder.pendingPlanCategory.setText("Category: " + plans.get(position).getPendingPlanCategory());
        holder.pendingPlanDesc.setText("Description: " + plans.get(position).getPendingPlanDesc());
        holder.pendingUserNames.setText("User Names: " + plans.get(position).getPendingPlanUserNames());
        holder.pendingPlanSector.setText("Sector: " + plans.get(position).getPendingPlanUserSector());
        holder.PendingPlanCell.setText("Cell: " + plans.get(position).getPendingPlanUserCell());
        holder.pendingPlanUserPhone.setText("Phone: " + plans.get(position).getPendingPlanUserPhone());
        //set Progress word.
        String progressWorld = "Pending";
        int progress = plans.get(position).getPendingPlanProgress();

        if(progress == 0)
        {
            progressWorld = "Pending";
        }
        else if(progress == 1)
        {
            progressWorld = "Approved by Cell";
        }
        else if(progress == 2)
        {
            progressWorld = "Approved by Sector";
        }
        else if(progress == 3)
        {
            progressWorld = "Approved by District";
        }
        else if(progress == -1)
        {
            progressWorld = "Rejected by Cell";
        }
        else if(progress == -2)
        {
            progressWorld = "Rejected by Sector";
        }
        else if(progress == -3)
        {
            progressWorld = "Rejected by District";
        }

        holder.pendingPlanProgress.setTextColor( (progress < 0) ? Color.RED : Color.GREEN);
        holder.pendingPlanProgress.setText(" " + progressWorld);

        holder.btnApprovePlan.setOnClickListener(v -> {
            if(fromLevel == 2)
            {
                new UsefulFunctions().approveUmuhigo(context, plans.get(position).getPendingPlanName(), planKeysList.get(position), plans.get(position).getPendingPlanCategory());
            }
            else if(fromLevel == 0)
            {
                new UsefulFunctions().approvePendingUmuhigoCell(context, plans.get(position).getPendingPlanName(), planKeysList.get(position), fromLevel);
            }
            else
            {
                new UsefulFunctions().approvePendingUmuhigo(context, plans.get(position).getPendingPlanName(), planKeysList.get(position), fromLevel);
            }
        });

        holder.btnDeclinePlan.setOnClickListener(v -> {
            new UsefulFunctions().delinePendingUmuhigo(context, plans.get(position).getPendingPlanName(), planKeysList.get(position), fromLevel);
        });

        //hide buttons if level is -
        if(fromLevel < 0)
        {
            holder.buttonsLayout.setVisibility(View.GONE);
            if(plans.get(position).getPendingPlanProgress() < 0)
            {
                holder.pendingPlanDeclineReason.setText("Because: " + plans.get(position).getPendingPlanDeclineReason());
            }
        }
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }
}
