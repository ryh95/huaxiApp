package com.example.ryh.huaxiapp;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.FileOutputStream;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private Button download,read;
    private EditText date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        download = (Button) findViewById(R.id.download);
        date = (EditText) findViewById(R.id.date);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asyncGet(date.getText().toString());
            }
        });
    }

    private void asyncGet(String date){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://101.201.114.42/"+date+"result.xls";
        File file = Environment.getExternalStorageDirectory();
        FileAsyncHttpResponseHandler f = new FileAsyncHttpResponseHandler(new File(file,"down/"+date+"result.xls")) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Toast.makeText(MainActivity.this,"Failed!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                Toast.makeText(MainActivity.this,"Succeed! The document is in the /down ",Toast.LENGTH_SHORT).show();
            }
        };
        client.get(url,f);
    }
}
