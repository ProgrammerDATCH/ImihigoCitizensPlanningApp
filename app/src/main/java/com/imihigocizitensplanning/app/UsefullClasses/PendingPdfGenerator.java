package com.imihigocizitensplanning.app.UsefullClasses;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.Toast;

import com.imihigocizitensplanning.app.Models.PendingImihigo;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PendingPdfGenerator {

    public static void generatePendingPDF(Context context, ArrayList<PendingImihigo> pendingImihigoList) {
        Document document = new Document();

        try {
            File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "HUYE District Pending Reports");
            if (!pdfFolder.exists()) {
                pdfFolder.mkdirs();
            }

            Date date = new Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
            String newFileName = "Huye_Pending_Report_" + timeStamp + ".pdf";
            File pdfFile = new File(pdfFolder, newFileName);

            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            String[] headers = {"Plan Name", "Category", "Description", "User ID", "User Names", "User Phone", "User Sector", "User Cell"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(Phrase.getInstance(header));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            for (PendingImihigo pendingImihigo : pendingImihigoList) {
                table.addCell(pendingImihigo.getPendingPlanName());
                table.addCell(pendingImihigo.getPendingPlanCategory());
                table.addCell(pendingImihigo.getPendingPlanDesc());
                table.addCell(pendingImihigo.getPendingUserId());
                table.addCell(pendingImihigo.getPendingPlanUserNames());
                table.addCell(pendingImihigo.getPendingPlanUserPhone());
                table.addCell(pendingImihigo.getPendingPlanUserSector());
                table.addCell(pendingImihigo.getPendingPlanUserCell());
            }

            document.add(table);
            document.close();

            // Display a toast message to indicate that the report has been generated
            Toast.makeText(context, "Pending Report Generated! with Name: (" + newFileName + ") in Documents.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

