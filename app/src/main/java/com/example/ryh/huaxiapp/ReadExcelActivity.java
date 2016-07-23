package com.example.ryh.huaxiapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;


import jxl.*;

public class ReadExcelActivity extends Activity {
    TextView txt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_excel);

        Intent i = getIntent();
        String date = i.getStringExtra("date");

        txt = (TextView)findViewById(R.id.txt_show);
        txt.setMovementMethod(ScrollingMovementMethod.getInstance());

        readExcel(date);
    }

    public void readExcel(String date) {
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            Workbook book = Workbook.getWorkbook(new File(sdCard,"down/"+date+"result.xls"));
            int num = book.getNumberOfSheets();
            txt.setText("the num of sheets is " + num+  "\n");
            // 获得第一个工作表对象
            Sheet sheet = book.getSheet(0);
            int Rows = sheet.getRows();
            int Cols = sheet.getColumns();
            txt.append("the name of sheet is " + sheet.getName() + "\n");
            txt.append("total rows is " + Rows + "\n");
            txt.append("total cols is " + Cols + "\n");
            for (int i = 0; i < Rows; ++i) {
                for (int j = 0; j < Cols; ++j) {
                    // getCell(Col,Row)获得单元格的值
//                    txt.append("contents:" + sheet.getCell(i,j).getContents() + "\n");
                    txt.append(sheet.getCell(j,i).getContents()+"   ");
                }
                txt.append("\n");
            }
            book.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}