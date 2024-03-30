package com.imihigocizitensplanning.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.imihigocizitensplanning.app.Common.CommentsActivity;
import com.imihigocizitensplanning.app.Models.Imihigo;
import com.imihigocizitensplanning.app.R;
import com.imihigocizitensplanning.app.UsefullClasses.UsefulFunctions;

import java.util.ArrayList;

public class PlansAdapter extends RecyclerView.Adapter<PlansViewHolder> {

    Context context;
    ArrayList<Imihigo> plans;
    ArrayList<String> planKeysList;
    boolean isFromAdmin;
    String currentUserId;
    String currentUserNames;
    int fromLevel;

    public PlansAdapter(Context context, ArrayList<Imihigo> plans, ArrayList<String> planKeysList, boolean isFromAdmin, int fromLevel, String currentUserId, String currentUserNames) {
        this.context = context;
        this.plans = plans;
        this.planKeysList = planKeysList;
        this.isFromAdmin = isFromAdmin;
        this.currentUserId = currentUserId;
        this.currentUserNames = currentUserNames;
        this.fromLevel = fromLevel;
    }


    @NonNull
    @Override
    public PlansViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlansViewHolder(LayoutInflater.from(context).inflate(R.layout.plan_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlansViewHolder holder, int position) {
        holder.planName.setText("Umuhigo: " + plans.get(position).getPlanName());
        holder.planDistrict.setText("Akarere: " +plans.get(position).getPlanDistrictName());
        holder.planTarget.setText("Intego: " + plans.get(position).getPlanTargetKey()+ " " + plans.get(position).getPlanTarget());
        holder.planTargetDone.setText("Ibyakozwe: " + plans.get(position).getPlanTargetKey()+ " " + plans.get(position).getPlanLevel());
        holder.planStartDate.setText("Gutangira: " + plans.get(position).getPlanStartDate());
        holder.planEndDate.setText("Gusoza: " + plans.get(position).getPlanEndDate());
        holder.planLevel.setProgress((plans.get(position).getPlanLevel() == 0) ? 0 : plans.get(position).getPlanLevelPercent());
        holder.planLevelText.setText(plans.get(position).getPlanLevelPercent() + "%");
        holder.moreBtn.setOnClickListener(v -> {
            if(isFromAdmin)
            {
                if(fromLevel == 3)
                {
                    new UsefulFunctions().showAlertDialog(context, planKeysList.get(position));
                }
            }
            else
            {
                new UsefulFunctions().userAddComment(context, planKeysList.get(position), currentUserId, currentUserNames);
            }
        });
        holder.planLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFromAdmin)
                {
                    Intent intent = new Intent(context, CommentsActivity.class);
                    intent.putExtra("planId", planKeysList.get(position));
                    intent.putExtra("leaderLevel", fromLevel);
                    context.startActivity(intent);
                }
                else
                {
                    new UsefulFunctions().showUserComment(context, planKeysList.get(position), currentUserId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

}
