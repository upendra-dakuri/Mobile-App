package com.example.logindemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.logindemo.model.Announcements;
import com.example.logindemo.network.FactoryAPI;
import com.example.logindemo.utils.Constants;
import com.example.logindemo.utils.ShowNotification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Headers;

public class SecondActivity extends AppCompatActivity {

    private TextView user;
    private Button logout;
    String TAG="second";
    private String token;
    ArrayAdapter<String> adapter;
    ListView listView;

    @Headers("Content-Type: application/json")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        adapter = new ArrayAdapter(this,R.layout.activity_list);
        listView = (ListView) findViewById(R.id.window_list);

        setContentView(R.layout.activity_second);
        user = (TextView) findViewById(R.id.user);
        Intent intent = getIntent();
        String str = intent.getStringExtra("key");
        user.setText(str);
        getAnnouncements();
        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(view -> logout());
    }

    private void getAnnouncements() {
        int err = 0;
        if (err == 0) {
            getAppAnnouncements();

        } else {

            Toast.makeText(SecondActivity.this, "Enter Valid Credentials", Toast.LENGTH_SHORT).show();
        }
    }
    String REG = "Reg";
    private void getAppAnnouncements() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        FactoryAPI api = retrofit.create(FactoryAPI.class);
        Call<String> call = api.getAnnouncements();
        call.enqueue(new Callback<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                System.out.println("on response");
                try {
                    JSONArray obj = new JSONArray(response);
                    List<String> announcementList = new ArrayList<>();
                    //String obj1 = Arrays.toString(obj);
                for (int i = 0; i<obj.length(); i++) {
                    JSONObject jsonObject = obj.getJSONObject(i);
                    announcementList.add(obj.getJSONObject(i).getString("_id"));
                    /*list.add( jsonArray.getString(i) );
                    announcementList.add(jsonObject.getString("_id"));*/
                    System.out.println("\nDate: "+jsonObject.getString("date")+"\n\n"+"Title: "+jsonObject.getString("title")+"\n\n"+"Description: "+jsonObject.getString("description")+"\n\n\n");
                    adapter.add("\nDate: "+jsonObject.getString("date")+"\n\n"+"Title: "+jsonObject.getString("title")+"\n\n"+"Description: "+jsonObject.getString("description")+"\n\n\n");
                    //adapter.add("\nDate: "+response.body().get(i).getDate()+"\n\n"+"Title: "+response.body().get(i).getTitle()+"\n\n"+"Description: "+response.body().get(i).getDescription()+"\n\n\n");
                    listView.setOnItemClickListener((parent, view, position, id) -> {
                        System.out.println("Clicked Position :"+position);
                        System.out.println(announcementList.get(position));
                        openActivity3(announcementList.get(position),token);
                    });
                }
                listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                    };

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("error is** "+t);
                Toast.makeText(SecondActivity.this, " On Failure on when clicked on hyperlink ", Toast.LENGTH_SHORT).show();
            }
        });
    }
    String TAG1="logout";
    private void logout() {
        finish();
    }

    private void openActivity3(String id,String token) {
        Intent intent = new Intent(this, ShowNotification.class);
        intent.putExtra("ID", id);
        intent.putExtra("TOKEN",token);
        startActivity(intent);
    }
}
