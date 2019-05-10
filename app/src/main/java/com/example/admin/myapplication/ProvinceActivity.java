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


    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String COUNTRY = "country";
    private int cityid=0;
    private String currentlevel=PROVINCE;
    private int pid=0;
    private String wid;
    private List<Integer> pids=new ArrayList<>();
    private List<String> data=new ArrayList<>();
    private List<String> weatherId=new ArrayList<>();
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
                if (currentlevel==PROVINCE){
                    pid=ProvinceActivity.this.pids.get(position);
                    currentlevel= CITY;
                    try {
                        getData(adapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if(currentlevel== CITY){
                    currentlevel=COUNTRY;
                    cityid=ProvinceActivity.this.pids.get(position);
                    try {
                        getData(adapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if(currentlevel==COUNTRY){
                    wid=ProvinceActivity.this.weatherId.get(position);
                    try {
                        getData(adapter);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent=new Intent(ProvinceActivity.this,WeatherActivity.class);
                    intent.putExtra("wid",ProvinceActivity.this.weatherId.get(position));
                    startActivity(intent);
                }

            }
        });

        try {
            getData(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getData(final ArrayAdapter<String> adapter) throws IOException {
            String weatherUrl =currentlevel==PROVINCE?"http://guolin.tech/api/china/":(currentlevel==CITY?"http://guolin.tech/api/china/"+pid:"http://guolin.tech/api/china/"+pid+"/"+cityid);


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
            this.pids.clear();
            this.weatherId.clear();
            try{
                jsonArray=new JSONArray(responseText);
//                String[] result=new String[jsonArray.length()];
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject=null;
                    jsonObject=jsonArray.getJSONObject(i);
                    this.data.add(jsonObject.getString("name"));
                    this.pids.add(jsonObject.getInt("id"));
                    if(jsonObject.has("weather_id")){
                      this.weatherId.add(jsonObject.getString("weather_id"));
                    }
                }
            }catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }

