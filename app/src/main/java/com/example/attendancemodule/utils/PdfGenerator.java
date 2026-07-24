package com.example.attendancemodule.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;

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
        canvas.drawText("School Attendance Report", 150, 50, titlePaint);

        // Summary
        paint.setTextSize(14);
        canvas.drawText("Summary: " + summaryText, 50, 100, paint);

        // Table Header
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Student Name", 50, 150, paint);
        canvas.drawText("Date", 300, 150, paint);
        canvas.drawText("Status", 450, 150, paint);

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        int y = 180;
        for (AttendanceRecord record : records) {
            canvas.drawText(record.getStudentName(), 50, y, paint);
            canvas.drawText(record.getDate(), 300, y, paint);
            canvas.drawText(record.getStatus(), 450, y, paint);
            y += 25;
            if (y > 800) break; 
        }

        pdfDocument.finishPage(page);

        File downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadsDir, "Attendance_Report_" + System.currentTimeMillis() + ".pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(context, "PDF saved: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();
    }
}
