package com.imihigocizitensplanning.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.imihigocizitensplanning.app.Models.Comment;
import com.imihigocizitensplanning.app.R;
import com.imihigocizitensplanning.app.UsefullClasses.UsefulFunctions;
import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    Context context;
    ArrayList<Comment> comments;
    int fromLevel;
    String planId;
    ArrayList<String> commentsKeys;

    public CommentsAdapter(Context context, ArrayList<Comment> comments, ArrayList<String> commentsKeys, int fromLevel, String planId) {
        this.context = context;
        this.comments = comments;
        this.fromLevel = fromLevel;
        this.planId = planId;
        this.commentsKeys = commentsKeys;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(context).inflate(R.layout.comment_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);

        holder.commentBy.setText("By: " + comment.getUserName());
        holder.comment.setText("Comment: " + comment.getUserMsg());

        // Display replies
        String repliesText = "Replies:\n";
        if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
            for (Comment.Reply reply : comment.getReplies()) {
                repliesText += "Reply: " + reply.getReplyMsg() + "\n";
                if (reply.getRepliedBy() != null) {
                    repliesText += "Replied by: " + reply.getRepliedBy() + "\n";
                }
            }
        } else {
            repliesText = "No replies";
        }
        holder.reply.setText(repliesText);

        holder.replyCommentBtn.setOnClickListener(v -> {
            String fromLevelPosition = "-";
            switch (fromLevel) {
                case 1:
                    fromLevelPosition = "Cell Leader";
                    break;
                case 2:
                    fromLevelPosition = "Sector Leader";
                    break;
                case 3:
                    fromLevelPosition = "District Leader";
                    break;
                default:
                    fromLevelPosition = "-";
                    break;
            }
            if (fromLevel == 3 || fromLevel == 2 || fromLevel == 1) {
                new UsefulFunctions().replyComment(context, planId, comment, commentsKeys.get(position), fromLevelPosition);
            }
        });

        // Hide buttons if level is -
        if (fromLevel < 0) {
            holder.commentButtonLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
