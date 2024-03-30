package com.imihigocizitensplanning.app.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.imihigocizitensplanning.app.R;

public class PendingPlansViewHolder extends RecyclerView.ViewHolder{

    TextView pendingPlanName, pendingUserNames, pendingPlanSector, PendingPlanCell, pendingPlanCategory, pendingPlanDesc, pendingPlanProgress, pendingPlanUserPhone, pendingPlanDeclineReason;
    MaterialButton btnApprovePlan, btnDeclinePlan;
    LinearLayout buttonsLayout;
    public PendingPlansViewHolder(@NonNull View itemView) {
        super(itemView);
        pendingPlanName = itemView.findViewById(R.id.pendingPlanName);
        pendingUserNames = itemView.findViewById(R.id.pendingUserNames);
        pendingPlanSector = itemView.findViewById(R.id.pendingPlanSector);
        PendingPlanCell = itemView.findViewById(R.id.PendingPlanCell);
        pendingPlanCategory = itemView.findViewById(R.id.pendingPlanCategory);
        pendingPlanDesc = itemView.findViewById(R.id.pendingPlanDesc);
        btnApprovePlan = itemView.findViewById(R.id.btnApprovePlan);
        btnDeclinePlan = itemView.findViewById(R.id.btnDeclinePlan);
        pendingPlanProgress = itemView.findViewById(R.id.pendingPlanProgress);
        pendingPlanUserPhone = itemView.findViewById(R.id.pendingPlanUserPhone);

        pendingPlanDeclineReason = itemView.findViewById(R.id.pendingPlanDeclineReason);
        buttonsLayout = itemView.findViewById(R.id.buttonsLayout);
    }
}
