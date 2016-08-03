package com.example.ryh.huaxiapp;

import android.content.Intent;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cz.msebera.android.httpclient.Header;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeContainer;
    private TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DateFormat dateFormat = new SimpleDateFormat("MM-dd");
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        txt = (TextView)findViewById(R.id.excel);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            String date =  dateFormat.format(cal.getTime());

            @Override
            public void onRefresh() {
                asyncGet(date);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    private void asyncGet(final String date){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://101.201.114.42/"+date+"result.xls";
        File sdCard = Environment.getExternalStorageDirectory();
        FileAsyncHttpResponseHandler f = new FileAsyncHttpResponseHandler(new File(sdCard,"down/"+date+"result.xls")) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Toast.makeText(MainActivity.this,"Failed!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                Toast.makeText(MainActivity.this,"Succeed! The document is in the /down ",Toast.LENGTH_SHORT).show();
                readExcel(date);
            }
        };
        client.get(url,f);
    }

    public void readExcel(String date) {
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            Workbook book = Workbook.getWorkbook(new File(sdCard,"down/"+date+"result.xls"));
            int num = book.getNumberOfSheets();
            txt.setText("the date is " + date+  "\n");
            // 获得第一个工作表对象
            Sheet sheet = book.getSheet(0);
            int Rows = sheet.getRows();
            int Cols = sheet.getColumns();
            for (int i = 0; i < Rows; ++i) {
                for (int j = 0; j < Cols; ++j) {
                    // getCell(Col,Row)获得单元格的值
//                    txt.append("contents:" + sheet.getCell(i,j).getContents() + "\n");
                    txt.append(sheet.getCell(j,i).getContents()+"   ");
                }
                txt.append("\n");
            }
            book.close();
            swipeContainer.setRefreshing(false);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
