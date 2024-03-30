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

public class CommentViewHolder extends RecyclerView.ViewHolder{

    TextView commentBy, comment, reply, repliedBy;
    LinearLayout commentButtonLayout;
    MaterialButton replyCommentBtn;
    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        commentBy = itemView.findViewById(R.id.commentBy);
        comment = itemView.findViewById(R.id.comment);
        reply = itemView.findViewById(R.id.reply);
        repliedBy = itemView.findViewById(R.id.repliedBy);
        commentButtonLayout = itemView.findViewById(R.id.commentButtonLayout);
        replyCommentBtn = itemView.findViewById(R.id.replyCommentBtn);
    }
}
