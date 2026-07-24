package com.example.attendancemodule.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.attendancemodule.models.AttendanceRecord;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfGenerator {

    public static void generateAttendancePdf(Context context, List<AttendanceRecord> records, String summaryText) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // Title
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        titlePaint.setTextSize(24);
        titlePaint.setColor(Color.BLACK);
        canvas.drawText("School Attendance Report", 130, 50, titlePaint);

        // Summary
        paint.setTextSize(12);
        canvas.drawText("Summary: " + summaryText, 30, 100, paint);

        // Table Header
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Student Name", 30, 140, paint);
        canvas.drawText("ID", 180, 140, paint);
        canvas.drawText("Date", 270, 140, paint);
        canvas.drawText("Time", 380, 140, paint);
        canvas.drawText("Status", 490, 140, paint);

        // Line below header
        paint.setStrokeWidth(1);
        canvas.drawLine(30, 150, 565, 150, paint);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        int y = 175;
        for (AttendanceRecord record : records) {
            canvas.drawText(record.getFullName(), 30, y, paint);
            canvas.drawText(record.getStudentId(), 180, y, paint);
            canvas.drawText(record.getDate(), 270, y, paint);
            canvas.drawText(record.getTimeMarked(), 380, y, paint);
            canvas.drawText(record.getStatus(), 490, y, paint);
            
            y += 25;
            if (y > 800) break; // Simple page limit for now
        }

        pdfDocument.finishPage(page);

        File downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadsDir, "Attendance_Report_" + System.currentTimeMillis() + ".pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(context, "PDF saved successfully!", Toast.LENGTH_SHORT).show();
            sharePdf(context, file);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error saving PDF", Toast.LENGTH_SHORT).show();
        } finally {
            pdfDocument.close();
        }
    }

    private static void sharePdf(Context context, File file) {
        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intent, "Share Attendance Report"));
    }
}
