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

public class ProvinceActivity extends AppCompatActivity {

    private int cityid=0;
    private String currentlevel="province";
    private List<String> data2=new ArrayList<>();
    private int pid=0;
    private List<Integer> pids=new ArrayList<>();
    private List<String> data=new ArrayList<>();
    private ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province);
        this.listview = (ListView) findViewById(R.id.listview);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listview.setAdapter(adapter);

       this.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.v("点击了哪一个",""+position+":"+ProvinceActivity.this.pids[position]+":"+ProvinceActivity.this.data.get(position));
                if (currentlevel=="province"){
                    pid=ProvinceActivity.this.pids.get(position);
                    currentlevel="city";
                    try {
                        getData(adapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{

                }

//                Intent intent=new Intent(ProvinceActivity.this,CityActivity.class);
//                intent.putExtra("pid",ProvinceActivity.this.pids[position]);
//                if(currentlevel=="city"){
//                    intent.putExtra("cid",cids[position]);
//                }
//                startActivity(intent);
            }
        });

        try {
            getData(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getData(final ArrayAdapter<String> adapter) throws IOException {
        String weatherUrl =currentlevel=="city"?"http://guolin.tech/api/china/"+pid:"http://guolin.tech/api/china";
       // String weatherUrl =currentlevel=="city"?"http://guolin.tech/api/china/"+pid:"http://guolin.tech/api/china/"+pid+"/"+cid;
        //  String weatherUrl = "http://guolin.tech/api/china";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                parseJSONObject(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        textView.setText(responseText);
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
                    this.pids.add(jsonObject.getInt("id"));
                }
            }catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }

