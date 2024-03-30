package com.imihigocizitensplanning.app.UsefullClasses;
import android.content.Context;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.imihigocizitensplanning.app.R;

public class AlertUtils {
    public static void showCustomAlert(Context context, String message) {
        View customLayout = LayoutInflater.from(context).inflate(R.layout.comments_layout_for_user, null);
        TextView alertText = customLayout.findViewById(R.id.alert_text);
        alertText.setText(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
