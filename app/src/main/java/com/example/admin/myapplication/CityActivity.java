package com.example.admin.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CityActivity extends AppCompatActivity {
    private TextView textView;
    private Button button;
    private int[] cids=new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private List<String> data=new ArrayList<>();
    private ListView listview;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        Intent intent=getIntent();
        final int pid=intent.getIntExtra("pid",0);
        Log.i("我们接收到了id",""+pid);
        this.textView=(TextView) findViewById(R.id.text);
        this.button=(Button) findViewById(R.id.button2);
        this.listview = (ListView) findViewById(R.id.listview);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listview.setAdapter(adapter);
        this.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(CityActivity.this,CountryActivity.class);
                intent.putExtra("cid",CityActivity.this.cids[position]);
                intent.putExtra("pid",pid);
                startActivity(intent);
            }
        });
//        this.button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent=new Intent(CityActivity.this,CountryActivity.class);
//                startActivity(intent);
//            }
//        });
        String weatherUrl = "http://guolin.tech/api/china/"+pid;
        HttpUtil.sendOkHttpRequest(weatherUrl,new Callback(){

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException{

                final String responseText = response.body().string();
                parseJSONObject(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(responseText);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
    private void parseJSONObject(String responseText) {
        JSONArray jsonArray=null;
        this.data.clear();
        try{
            jsonArray=new JSONArray(responseText);
//                String[] result=new String[jsonArray.length()];
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=null;
                jsonObject=jsonArray.getJSONObject(i);
                this.data.add(jsonObject.getString("name"));
                this.cids[i]=jsonObject.getInt("id");
            }
        }catch(JSONException e) {
            e.printStackTrace();
        }
    }
}