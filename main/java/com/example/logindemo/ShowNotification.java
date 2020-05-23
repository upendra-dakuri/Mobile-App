package com.example.logindemo.utils;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.logindemo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShowNotification extends AppCompatActivity {
    TextView notification;
    private ImageView imageView;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notification);
        notification = (TextView) findViewById(R.id.notification);
        imageView = (ImageView) findViewById(R.id.imageView);
        showNotification(getIntent().getStringExtra("ID"));
    }

    private void showNotification(String id) {
        String url = "http://10.0.2.2:3000/announcements/"+getIntent().getStringExtra("ID");

        RequestQueue requestQueue = Volley.newRequestQueue(ShowNotification.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    System.out.println(object.getString("imageURL"));
                    notification.setText("Date :"+object.getString("date")+"\n\n"+"Title :\n"+object.getString("title")+"\n\n"+"Description :\n"+object.getString("description")+"\n\nDetails :\n"+object.getString("details")+"\n\nLink :\n"+object.getString("link"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                // params.put("Content-Type","application/json; charset=utf-8");
                params.put("Authorization","Bearer "+getIntent().getStringExtra("TOKEN"));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

}