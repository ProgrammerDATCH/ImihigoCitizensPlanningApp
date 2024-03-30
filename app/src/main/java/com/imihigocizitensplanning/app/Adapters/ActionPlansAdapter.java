package com.imihigocizitensplanning.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.imihigocizitensplanning.app.R;
import com.imihigocizitensplanning.app.UsefullClasses.UsefulFunctions;

import java.util.ArrayList;


public class ActionPlansAdapter extends RecyclerView.Adapter<ActionPlansViewHolder> {


    Context context;
    ArrayList<String> actionPlans;
    ArrayList<String> actionPlanKeysList;
    boolean fromAdmin;

    public ActionPlansAdapter(Context context, ArrayList<String> actionPlans, ArrayList<String> actionPlanKeysList, boolean fromAdmin) {
        this.context = context;
        this.actionPlans = actionPlans;
        this.actionPlanKeysList = actionPlanKeysList;
        this.fromAdmin = fromAdmin;
    }

    @NonNull
    @Override
    public ActionPlansViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ActionPlansViewHolder(LayoutInflater.from(context).inflate(R.layout.action_plan_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ActionPlansViewHolder holder, int position) {
        holder.actionPlanName.setText(actionPlans.get(position));
        holder.actionPlanLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(fromAdmin)
                {
                    new UsefulFunctions().deleteActionPlan(context, actionPlanKeysList.get(position));
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return actionPlans.size();
    }
}
