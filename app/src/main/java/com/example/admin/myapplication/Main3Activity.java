package com.example.admin.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Main3Activity extends AppCompatActivity {
    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Intent intent=getIntent();
        int pid=intent.getIntExtra("pid",0);
        Log.i("我们接收到了id",""+pid);
        this.textView=(TextView) findViewById(R.id.text);
        this.button=(Button) findViewById(R.id.button2);
        this.button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Main3Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        String weatherUrl = "http://guolin.tech/api/china/"+pid;
        HttpUtil.sendOkHttpRequest(weatherUrl,new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException{
                final String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(responseText);
                    }
                });
            }
        });


    }
}