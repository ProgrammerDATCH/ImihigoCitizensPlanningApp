package com.imihigocizitensplanning.app.Adapters;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.imihigocizitensplanning.app.R;

public class ActionPlansViewHolder extends RecyclerView.ViewHolder {

    TextView actionPlanName;
    LinearLayout actionPlanLayout;

    public ActionPlansViewHolder(@NonNull View itemView) {
        super(itemView);
        actionPlanName = itemView.findViewById(R.id.actionPlanName);
        actionPlanLayout = itemView.findViewById(R.id.actionPlanLayout);
    }

}
