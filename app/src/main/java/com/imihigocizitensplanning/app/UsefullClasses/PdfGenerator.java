package com.imihigocizitensplanning.app.UsefullClasses;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.widget.Toast;

import com.imihigocizitensplanning.app.Models.Imihigo;
import com.imihigocizitensplanning.app.Models.User;
import com.imihigocizitensplanning.app.R;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
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

public class PdfGenerator {



    public static void generatePDF(Context context, ArrayList<Imihigo> allImihigo) {
        Document document = new Document();

        try {
            File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "HUYE District Reports");
            if (!pdfFolder.exists()) {
                pdfFolder.mkdirs();
            }

            Date date = new Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
            String newFileName = "Huye_Report_" + timeStamp + ".pdf";
            File pdfFile = new File(pdfFolder, newFileName);

            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            Paragraph title = new Paragraph("HUYE REPORT FOR Year 2022-2023");
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Resources resources = context.getResources();
            Drawable logoDrawable = resources.getDrawable(R.drawable.logo_no_bg);
            Bitmap logoBitmap = ((BitmapDrawable) logoDrawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            logoBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            com.itextpdf.text.Image logoImage = com.itextpdf.text.Image.getInstance(byteArray);
            float desiredWidth = 100;
            float desiredHeight = logoImage.getHeight() * (desiredWidth / logoImage.getWidth());
            logoImage.scaleToFit(desiredWidth, desiredHeight);
            logoImage.setAlignment(Element.ALIGN_CENTER);

            document.add(logoImage);


            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            String[] headers = {"Plan Name", "Start Date", "End Date", "District Name", "Budget", "Target", "Level", "Percent Level"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

            for (Imihigo imihigo : allImihigo) {

                float perc = (int) ((float) imihigo.getPlanLevel() / imihigo.getPlanTarget() * 100);
                String percLevel = perc + "%";
                String budget = imihigo.getPlanBudget() + " Rwf";
                String planTarget = imihigo.getPlanTarget() + " " + imihigo.getPlanTargetKey();
                String planLevel = imihigo.getPlanLevel() + " " + imihigo.getPlanTargetKey();

                table.addCell(imihigo.getPlanName());
                table.addCell(imihigo.getPlanStartDate());
                table.addCell(imihigo.getPlanEndDate());
                table.addCell(imihigo.getPlanDistrictName());
                table.addCell(budget);
                table.addCell(planTarget);
                table.addCell(planLevel);
                table.addCell(percLevel);
            }

            document.add(table);
            document.close();

            Toast.makeText(context, "Report Generated! with Name: (" + newFileName + ") in Documents.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void generateUserPdf(Context context, ArrayList<User> userList, String givenTitle) {
        Document document = new Document();

        try {
            File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "User Reports");
            if (!pdfFolder.exists()) {
                pdfFolder.mkdirs();
            }

            Date date = new Date();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
            String newFileName = "HUYE_Imihigo_Users_Report_" + timeStamp + ".pdf";
            File pdfFile = new File(pdfFolder, newFileName);

            PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();

            Paragraph title = new Paragraph(givenTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            String[] headers = {"National ID", "Name", "Sector", "Cell", "Gender", "Phone", "Position"};
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            }

            for (User user : userList) {
                table.addCell(user.getNationalId());
                table.addCell(user.getNames());
                table.addCell(user.getSector());
                table.addCell(user.getCell());
                table.addCell(user.getGender());
                table.addCell(user.getPhone());
                String userPosition = " - ";
                if(user.getUserCategory().equals(Constants.CITIZEN))
                {
                    userPosition = "Citizen of " + user.getCell() + " Cell, " + user.getSector() + " Sector.";
                }
                else if(user.getUserCategory().equals(Constants.CELL_LEADER))
                {
                    userPosition = "Cell Leader of " + user.getCell();
                }
                else if(user.getUserCategory().equals(Constants.SECTOR_LEADER))
                {
                    userPosition = "Sector Leader of " + user.getSector();
                }
                table.addCell(userPosition);
            }

            document.add(table);
            document.close();

            Toast.makeText(context, "Report Generated! with Name: (" + newFileName + ") in Documents.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
