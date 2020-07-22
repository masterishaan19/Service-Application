package com.example.enrserviceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class ExportToExcelActivity extends AppCompatActivity {
    ArrayList < Integer > imageList;
    customAdapterWorkCompleted customAdapter;
    Button export;
//    ViewPager2 viewPager;
    ImageSlideShowAdapter imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_to_excel);
        // Code creation starts from here !!
//        viewPager = findViewById(R.id.viewPager);
        imageList = new ArrayList<>();
        FirebaseRecyclerOptions< WorkDoneClass > options = new
                FirebaseRecyclerOptions.Builder<WorkDoneClass>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("WorkDone"), WorkDoneClass.class)
                .build();
        export = findViewById(R.id.export);
        export.setEnabled(true);
//        imageList.add(R.drawable.jungle);
//        imageList.add(R.drawable.location);
//        imageList.add(R.drawable.jungle);
//        imageList.add(R.drawable.location);
//        imageList.add(R.drawable.trial_back);
//        imageList.add(R.drawable.jungle);
//        TODO : ADD images for the slide show of demo of the export data !!!
        imageAdapter = new ImageSlideShowAdapter(ExportToExcelActivity.this, imageList);
//        viewPager.setAdapter(imageAdapter);
//        imageAdapter.notifyDataSetChanged();
        customAdapter = new customAdapterWorkCompleted(options);
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        exportNow();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0 && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
            }
        }else
            Toast.makeText(this, "Permission Denied !!", Toast.LENGTH_SHORT).show();
    }
    private void startExporting(){
        try{
            if (!isExternalStorageAvailable() || isExternalStorageReadOnly()){
                Toast.makeText(this, "Something went wrong !", Toast.LENGTH_SHORT).show();
                return;
            }
            /*TODO : Change the format as well as other factor for the export to excel sheet !! */
            Workbook wb = new HSSFWorkbook();
            // Created the excel sheet
            // Creating the font style
            HSSFFont font = (HSSFFont) wb.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short)10);
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
            // Creating the style for the export data !!
            CellStyle headingStyle = wb.createCellStyle();
            headingStyle.setFillForegroundColor(HSSFColor.AQUA.index);
            headingStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            headingStyle.setFont(font);

            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            Sheet sheet = wb.createSheet("Completed Services"); // Setting Sheet name

            Row titleRow = sheet.createRow(5); // Creating the row
            Cell c = null;
            c = titleRow.createCell(5); // Setting the column / cell in that row
            c.setCellValue("Completed Work Details"); // assigning value  to the cell for the row nd column
            c.setCellStyle(headingStyle);

            int column = 2, rowNumber = 7;
            Row row = sheet.createRow(rowNumber++);
            // Creating the columns
            c = row.createCell(column++);
            c.setCellValue("Title"); // 1
            c.setCellStyle(headingStyle);

            c = row.createCell(column++);
            c.setCellValue("Work Type"); //2
            c.setCellStyle(headingStyle);

            c = row.createCell(column++);
            c.setCellValue("Worker Id"); //3
            c.setCellStyle(headingStyle);

            c = row.createCell(column++);
            c.setCellValue("Customer Id"); ///4
            c.setCellStyle(headingStyle);

            c = row.createCell(column++);
            c.setCellValue("Start Time");//5
            c.setCellStyle(headingStyle);

            c = row.createCell(column++);
            c.setCellValue("End Time");///6
            c.setCellStyle(headingStyle);

            c = row.createCell(column++);
            c.setCellValue("Break Time");//7
            c.setCellStyle(headingStyle);
            column = 2;
            sheet.setColumnWidth(column++, (15 * 200)); // 1
            sheet.setColumnWidth(column++, (15 * 500)); // 2
            sheet.setColumnWidth(column++, (15 * 400)); // 3
            sheet.setColumnWidth(column++, (15 * 400)); // 4
            sheet.setColumnWidth(column++, (15 * 400)); // 5
            sheet.setColumnWidth(column++, (15 * 400)); // 6
            sheet.setColumnWidth(column++, (15 * 200)); // 7
            column = 2;
//            rowNumber++
            // Adding the data to excel sheet !!
            for(int i=0; i<customAdapter.getItemCount(); i++) {
                row = sheet.createRow(rowNumber++);
                //creating the row for next row
                // title, work-type, worker email, customer email, start time, end time, break time
                // Title
                c = row.createCell(column++);
                c.setCellValue(customAdapter.getItem(i).getTitle());
                if((i%2==0 && column%2==0) || (i%2  != 0 && column%2 != 0))
                    c.setCellStyle(cellStyle);
                // Work Type
                c = row.createCell(column++);
                c.setCellValue(customAdapter.getItem(i).getWorkType());
                if((i%2 == 0 && column%2==0) || (i%2  != 0 && column%2 != 0))
                    c.setCellStyle(cellStyle);
                // Worker Email
                c = row.createCell(column++);
                c.setCellValue(customAdapter.getItem(i).getWorkerId());
                if((i%2 == 0 && column%2==0) || (i%2  != 0 && column%2 != 0))
                    c.setCellStyle(cellStyle);
                // Customer Email
                c = row.createCell(column++);
                c.setCellValue(customAdapter.getItem(i).getCustomerId());
                if((i%2 == 0 && column%2==0) || (i%2  != 0 && column%2 != 0))
                    c.setCellStyle(cellStyle);
                //Start Time
                c = row.createCell(column++);
                c.setCellValue(customAdapter.getItem(i).startTime() + " on Date: " + customAdapter.getItem(i).getStartingDate());
                if((i%2 == 0 && column%2==0) || (i%2  != 0 && column%2 != 0))
                    c.setCellStyle(cellStyle);
                // End time
                c = row.createCell(column++);
                c.setCellValue(customAdapter.getItem(i).endingTime() + " on Date: " + customAdapter.getItem(i).getEndingDate());
                if((i%2 == 0 && column%2==0) || (i%2  != 0 && column%2 != 0))
                    c.setCellStyle(cellStyle);
                // Break Time
                c = row.createCell(column++);
                c.setCellValue(customAdapter.getItem(i).getBreakTime());
                if((i%2 == 0 && column%2==0) || (i%2  != 0 && column%2 != 0))
                    c.setCellStyle(cellStyle);
                column = 2;
            }
            File file = new File(Environment.getExternalStorageDirectory(), "Completed Service.xls");
            try{
                FileOutputStream os = new FileOutputStream(file);
                wb.write(os);
                Toast.makeText(getApplicationContext(), "Data exported", Toast.LENGTH_SHORT).show();
                os.close();
                finish();
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                finish();
            }
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    public void exportNow(){
        AlertDialog.Builder loading = new AlertDialog.Builder(ExportToExcelActivity.this);
        loading.setTitle("Fetching Data")
                .setView(LayoutInflater.from(ExportToExcelActivity.this).inflate(R.layout.export_dialog, null, false));
        final AlertDialog loadingDialog = loading.create();
        loadingDialog.show();
        CountDownTimer count = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                export.setEnabled(true);
                startExporting();
                loadingDialog.dismiss();
            }
        };
        count.start();
    }
    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }
    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
    @Override
    protected void onStart() {
        super.onStart();
        customAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        customAdapter.stopListening();
    }
}