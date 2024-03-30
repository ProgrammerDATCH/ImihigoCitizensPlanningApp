package com.imihigocizitensplanning.app.Common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imihigocizitensplanning.app.Adapters.CommentsAdapter;
import com.imihigocizitensplanning.app.Models.Comment;
import com.imihigocizitensplanning.app.R;
import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth auth;
    RecyclerView commentsPlansRecyclerView;
    LinearLayout loadingCommentsProgress, commentsEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        loadingCommentsProgress = findViewById(R.id.loadingCommentsProgress);
        commentsEmpty = findViewById(R.id.commentsEmpty);
        commentsPlansRecyclerView = findViewById(R.id.commentsPlansRecyclerView);
        commentsPlansRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String planId = intent.getStringExtra("planId");
        int leaderLevel = intent.getIntExtra("leaderLevel", 0);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        database.getReference().child("Comments").child(planId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loadingCommentsProgress.setVisibility(View.GONE);
                if (!snapshot.exists()) {
                    commentsEmpty.setVisibility(View.VISIBLE);
                    Toast.makeText(CommentsActivity.this, "No Comment Found!", Toast.LENGTH_SHORT).show();
                } else {
                    commentsEmpty.setVisibility(View.GONE);
                    // Loop through comments
                    ArrayList<Comment> comments = new ArrayList<>();
                    ArrayList<String> commentsKeys = new ArrayList<>();
                    for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                        Comment comment = commentSnapshot.getValue(Comment.class);
                        if (comment != null) {
                            // Display the comment and its replies
                            String commentText = "Comment: " + comment.getUserMsg();
                            if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
                                commentText += "\n\nReplies:\n";
                                for (Comment.Reply reply : comment.getReplies()) {
                                    commentText += "\nReply: " + reply.getReplyMsg() + "\n";
                                    if (reply.getRepliedBy() != null) {
                                        commentText += "Replied by: " + reply.getRepliedBy() + "\n\n\n";
                                    }
                                }
                            }
                            comments.add(comment);
                            commentsKeys.add(commentSnapshot.getKey());
                        }
                    }
                    commentsPlansRecyclerView.setAdapter(new CommentsAdapter(CommentsActivity.this, comments, commentsKeys, leaderLevel, planId));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingCommentsProgress.setVisibility(View.GONE);
                Toast.makeText(CommentsActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
