package com.imihigocizitensplanning.app.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.imihigocizitensplanning.app.R;

public class PlansViewHolder extends RecyclerView.ViewHolder{

    TextView planName, planDistrict, planStartDate, planEndDate, planTargetDone, planTarget, planLevelText;
    ProgressBar planLevel;
    ImageView planImg, moreBtn;
    LinearLayout planLayout;
    public PlansViewHolder(@NonNull View itemView) {
        super(itemView);
        planLevel = itemView.findViewById(R.id.planLevel);
        planImg = itemView.findViewById(R.id.planImg);
        moreBtn = itemView.findViewById(R.id.moreBtn);
        planName = itemView.findViewById(R.id.planName);
        planDistrict = itemView.findViewById(R.id.planDistrict);
        planStartDate = itemView.findViewById(R.id.planStartDate);
        planEndDate = itemView.findViewById(R.id.planEndDate);
        planTargetDone = itemView.findViewById(R.id.planTargetDone);
        planTarget = itemView.findViewById(R.id.planTarget);
        planLevelText = itemView.findViewById(R.id.planLevelText);
        planLayout = itemView.findViewById(R.id.planLayout);
    }
}
