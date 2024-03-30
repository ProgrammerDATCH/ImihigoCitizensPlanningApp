package com.imihigocizitensplanning.app.Adapters;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.imihigocizitensplanning.app.R;

public class LeadersViewHolder extends RecyclerView.ViewHolder{

    TextView leaderNames, leaderPosition, leaderPhone;

    public LeadersViewHolder(@NonNull View itemView) {
        super(itemView);
        leaderNames = itemView.findViewById(R.id.leaderNames);
        leaderPosition = itemView.findViewById(R.id.leaderPosition);
        leaderPhone = itemView.findViewById(R.id.leaderPhone);
    }
}
