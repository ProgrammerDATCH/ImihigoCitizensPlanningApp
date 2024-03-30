package com.imihigocizitensplanning.app.UsefullClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imihigocizitensplanning.app.Adapters.CommentsAdapter;
import com.imihigocizitensplanning.app.Common.CommentsActivity;
import com.imihigocizitensplanning.app.District.UpdatePendingPlanActivity;
import com.imihigocizitensplanning.app.District.UpdatePlanActivity;
import com.imihigocizitensplanning.app.Models.Comment;
import com.imihigocizitensplanning.app.Models.Imihigo;
import com.imihigocizitensplanning.app.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class UsefulFunctions {

    FirebaseDatabase database;


    public UsefulFunctions() {
        database = FirebaseDatabase.getInstance();
    }

    public String[] allDistricts()
    {
        String districts[] = {"Bugesera", "Burera", "Gakenke", "Gasabo", "Gatsibo", "Gicumbi", "Gisagara", "Huye", "Kamonyi", "Karongi", "Kayonza", "Kicukiro", "Kirehe", "Muhanga", "Musanze", "Ngoma", "Ngororero", "Nyabihu", "Nyagatare", "Nyamagabe", "Nyamasheke", "Nyanza", "Nyarugenge", "Nyaruguru", "Rubavu", "Ruhango", "Rulindo", "Rusizi", "Rutsiro", "Rwamagana"};
        return districts;
    }

    public String[] allSectors()
    {
        String sectors[] = {"Gishamvu" ,"Huye" ,"Karama" ,"Kigoma" ,"Kinazi" ,"Maraba" ,"Mbazi" ,"Mukura" ,"Ngoma" ,"Ruhashya" ,"Rusatira" ,"Rwaniro" ,"Simbi" ,"Tumba"};
        return sectors;
    }

    public String[] allCells(String sector)
    {
        String cells[];
        switch (sector)
        {
            case "Gishamvu":
                cells = new String[]{"Nyakibanda" ,"Nyumba" ,"Ryakibogo" ,"Shori"};
                break;
            case "Huye":
                cells = new String[]{"Muyogoro" ,"Nyakagezi" ,"Rukira" ,"Sovu"};
                break;
            case "Karama":
                cells = new String[]{"Buhoro" ,"Bunazi" ,"Gahororo" ,"Kibingo" ,"Muhembe"};
                break;
            case "Kigoma":
                cells = new String[]{"Gishihe" ,"Kabatwa" ,"Kabuga" ,"Karambi" ,"Musebeya" ,"Nyabisindu" ,"Rugarama" ,"Shanga"};
                break;
            case "Kinazi":
                cells = new String[]{"Byinza" ,"Gahana" ,"Gitovu" ,"Kabona" ,"Sazange"};
                break;
            case "Maraba":
                cells = new String[]{"Buremera" ,"Gasumba" ,"Kabuye" ,"Kanyinya" ,"Shanga" ,"Shyembe"};
                break;
            case "Mbazi":
                cells = new String[]{"Gatobotobo" ,"Kabuga" ,"Mutunda" ,"Mwulire" ,"Rugango" ,"Rusagara" ,"Tare"};
                break;
            case "Mukura":
                cells = new String[]{"Bukomeye" ,"Buvumu" ,"Icyeru" ,"Rango A"};
                break;
            case "Ngoma":
                cells = new String[]{"Butare" ,"Kaburemera" ,"Matyazo" ,"Ngoma"};
                break;
            case "Ruhashya":
                cells = new String[]{"Busheshi" ,"Gatovu" ,"Karama" ,"Mara" ,"Muhororo" ,"Rugogwe" ,"Ruhashya"};
                break;
            case "Rusatira":
                cells = new String[]{"Buhimba" ,"Gafumba" ,"Kimirehe" ,"Kimuna" ,"Kiruhura" ,"Mugogwe"};
                break;
            case "Rwaniro":
                cells = new String[]{"Gatwaro" ,"Kamwambi" ,"Kibiraro" ,"Mwendo" ,"Nyamabuye" ,"Nyaruhombo" ,"Shyunga"};
                break;
            case "Simbi":
                cells = new String[]{"Cyendajuru" ,"Gisakura" ,"Kabusanza" ,"Mugobore" ,"Nyangazi"};
                break;
            case "Tumba":
                cells = new String[]{"Cyarwa" ,"Cyimana" ,"Gitwa" ,"Mpare" ,"Rango B"};
                break;
            default:
                cells = new String[]{"-"};
                break;
        }
        return cells;
    }

    public void preventBack(Context context, Activity activity)
    {
        new AlertDialog.Builder(context)
                .setTitle("EXIT")
                .setMessage("Are you sure. You want to close this app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    public void deleteThisPendingPlan(String keyToDelete)
    {
        database.getReference().child("districts").child("huye").child("pendingImihigo").child(keyToDelete).removeValue();
    }
    public void deleteActionPlan(Context context, String keyToDelete)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Delete this Action Plan?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        database.getReference().child("districts").child("huye").child("actionPlans").child(keyToDelete).removeValue();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void deleteThisPlan(String keyToDelete)
    {
        database.getReference().child("districts").child("huye").child("imihigo").child(keyToDelete).removeValue();
    }

    public void showAlertDialog(Context context, String userKey) {
        final boolean[] updated = {false};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select an Option");

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, UpdatePlanActivity.class);
                intent.putExtra("fromUpdate", true);
                intent.putExtra("userKey", userKey);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteThisPlan(userKey);
            }
        });

        builder.create().show();
    }

    public void userAddComment(Context context, String planId, String currentUserId, String currentUserName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.input_dialog, null);
        builder.setView(view);
        builder.setTitle("Add a Comment");

        final EditText input = view.findViewById(R.id.editText);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String userInput = input.getText().toString();
                if (!userInput.isEmpty()) {
                    Comment comment = new Comment(userInput, currentUserName);
                    database.getReference().child("Comments").child(planId).child(currentUserId).setValue(comment);
                    Toast.makeText(context, "Comment Added!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Empty input!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                builder.create().dismiss();
            }
        });
        builder.create().show();
    }



    TextView createTextView(String text, Context context) {
        TextView textView = new TextView(context);
        textView.setText(text);
        return textView;
    }

    public void updateUserAlert(Context context, String userKey, Imihigo umuhigo) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Umuhigo");
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText editName = new EditText(context);
        layout.addView(createTextView("Reason: ", context));
        layout.addView(editName);
        builder.setView(layout);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reasonWhy = editName.getText().toString().trim();
                if(reasonWhy.isEmpty())
                {
                    Toast.makeText(context, "Type in a reason please!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    public void approveUmuhigo(Context context, String umuhigoName, String oldPlanKey, String planCategory) {
        Intent intent = new Intent(context, UpdatePlanActivity.class);
        intent.putExtra("fromUpdate", false);
        intent.putExtra("planName", umuhigoName);
        intent.putExtra("planCategory", planCategory);
        intent.putExtra("oldPlanKey", oldPlanKey);
        context.startActivity(intent);
    }
    public void approvePendingUmuhigo(Context context, String umuhigoName, String oldPlanKey, int fromLevel) {
        Intent intent = new Intent(context, UpdatePendingPlanActivity.class);
        intent.putExtra("fromLevel", fromLevel);
        intent.putExtra("umuhigoName", umuhigoName);
        intent.putExtra("oldPlanKey", oldPlanKey);
        context.startActivity(intent);

    }

    public void approvePendingUmuhigoCell(Context context, String umuhigoName, String oldPlanKey, int fromLevel) {
        int newLevel = fromLevel + 1;
        database.getReference().child("districts").child("huye").child("pendingImihigo").child(oldPlanKey).child("pendingPlanProgress").setValue(newLevel);
        Toast.makeText(context, umuhigoName + " Approved!", Toast.LENGTH_SHORT).show();
    }

    public void replyComment(Context context, String planId, Comment comment, String userId, String fromLevelPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.input_dialog, null);
        builder.setView(view);
        builder.setTitle("Reply");
        final EditText input = view.findViewById(R.id.editText);

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String userInput = input.getText().toString();
                if (!userInput.isEmpty()) {
                    Comment.Reply reply = new Comment.Reply(userInput, fromLevelPosition);

                    // Add the reply to the comment's list of replies
//                    if (comment.getReplies() == null) {
//                        comment.setReplies(new ArrayList<>());
//                    }
                    comment.getReplies().add(reply);

                    // Update the comment with the new reply
                    database.getReference().child("Comments").child(planId).child(userId).setValue(comment);

                    Toast.makeText(context, "Reply sent!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Empty input!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                builder.create().dismiss();
            }
        });

        builder.create().show();
    }


    public void delinePendingUmuhigo(Context context, String umuhigoName, String oldPlanKey, int fromLevel) {
        //show popup to give reason

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update Umuhigo");
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText editName = new EditText(context);
        layout.addView(createTextView("Reason: ", context));
        layout.addView(editName);
        builder.setView(layout);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String reasonWhy = editName.getText().toString().trim();
                if(reasonWhy.isEmpty())
                {
                    Toast.makeText(context, "Type in a reason please!", Toast.LENGTH_SHORT).show();
                    return;
                }

                database.getReference().child("districts").child("huye").child("pendingImihigo").child(oldPlanKey).child("pendingPlanDeclineReason").setValue(reasonWhy);

                int newLevel = -(fromLevel + 1);
                database.getReference().child("districts").child("huye").child("pendingImihigo").child(oldPlanKey).child("pendingPlanProgress").setValue(newLevel);
                Toast.makeText(context, umuhigoName + " Rejected!", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }


    public void showDatePickerDialog(Context context, EditText to) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (datePicker, year1, month1, day1) -> {
                    String formattedMonth = (month1 + 1 < 10) ? "0" + (month1 + 1) : String.valueOf(month1 + 1);
                    String formattedDay = (day1 < 10) ? "0" + day1 : String.valueOf(day1);
                    String selectedDate = formattedDay + "-" + formattedMonth + "-" + year1;
                    to.setText(selectedDate);
                    to.setError(null);
                },
                year, month, day
        );
        datePickerDialog.show();
    }


    public void showUserComment(Context context, String planId, String currentUserId) {
        DatabaseReference commentsRef = database.getReference().child("Comments").child(planId);

        commentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(context, "No Comment Found!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    boolean found = false;
                    for (DataSnapshot commentSnapshot : snapshot.getChildren()) {
                        Comment comment = commentSnapshot.getValue(Comment.class);
                        if (comment != null && currentUserId.equals(commentSnapshot.getKey()))
                        {
                            found = true;
                            // Display the user's comment and replies
                            String commentText = "Comment: " + comment.getUserMsg();
                            if (comment.getReplies() != null && !comment.getReplies().isEmpty()) {
                                commentText += "\n\nReplies:\n";
                                for (Comment.Reply reply : comment.getReplies()) {
                                    commentText += "\n\nReply: " + reply.getReplyMsg() + "\n";
                                    if (reply.getRepliedBy() != null) {
                                        commentText += "Replied by: " + reply.getRepliedBy() + "\n";
                                    }
                                }
                            }
                            AlertUtils.showCustomAlert(context, commentText);
                        }
                    }
                    if (!found) {
                        Toast.makeText(context, "You didn't comment on this!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
