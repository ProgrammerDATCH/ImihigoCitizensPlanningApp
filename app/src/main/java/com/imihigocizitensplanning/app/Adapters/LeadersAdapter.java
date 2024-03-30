package com.imihigocizitensplanning.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.imihigocizitensplanning.app.Models.Comment;
import com.imihigocizitensplanning.app.Models.User;
import com.imihigocizitensplanning.app.R;
import com.imihigocizitensplanning.app.UsefullClasses.Categories;
import com.imihigocizitensplanning.app.UsefullClasses.Constants;
import com.imihigocizitensplanning.app.UsefullClasses.UsefulFunctions;

import java.util.ArrayList;

public class LeadersAdapter extends RecyclerView.Adapter<LeadersViewHolder> {

    Context context;
    ArrayList<User> allLeaders;

    public LeadersAdapter(Context context, ArrayList<User> allLeaders) {
        this.context = context;
        this.allLeaders = allLeaders;
    }

    @NonNull
    @Override
    public LeadersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LeadersViewHolder(LayoutInflater.from(context).inflate(R.layout.leader_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LeadersViewHolder holder, int position)
    {
        User currentLeader = allLeaders.get(position);

        holder.leaderNames.setText("Names: " + currentLeader.getNames());
        holder.leaderPhone.setText("Phone: " + currentLeader.getPhone());
        if(currentLeader.getUserCategory().equals(Constants.SECTOR_LEADER))
        {
            holder.leaderPosition.setText("Position: Leader of " + currentLeader.getSector() + " Sector.");
        }
        else if(currentLeader.getUserCategory().equals(Constants.CITIZEN))
        {
            holder.leaderPosition.setText("Position: Citizen of " + currentLeader.getCell() + " Cell, " + currentLeader.getSector() + " Sector.");
        }
        else
        {
            holder.leaderPosition.setText("Position: Leader of " + currentLeader.getCell() + " Cell.");
        }

    }

    @Override
    public int getItemCount() {
        return allLeaders.size();
    }
}
