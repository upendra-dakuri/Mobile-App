package com.example.logindemo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Toast;
import okhttp3.ResponseBody;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.subscriptions.CompositeSubscription;
import com.example.logindemo.model.LoginData;
import com.example.logindemo.network.FactoryAPI;
import com.example.logindemo.utils.Constants;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.UserName);
        password = (EditText) findViewById(R.id.Password);
        login = (Button) findViewById(R.id.login);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(username.getText().toString(), password.getText().toString());
            }
        });
    }

    private void login(String username, String password) {

        int err = 0;
        if (err == 0) {
            loginProcess(username, password);

        } else {

            Toast.makeText(MainActivity.this, "Enter Valid Credentials", Toast.LENGTH_SHORT).show();
        }
    }

    String TAG = "Reg";
    private void loginProcess(String username, String password) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        FactoryAPI api = retrofit.create(FactoryAPI.class);
        LoginData data = new LoginData(username, password);

        Call<ResponseBody> call = api.login(data);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                if (null != response.body()) {
                    Intent intent = new Intent(getApplicationContext(), SecondActivity.class);

                   try {
                        String fullresponse = (String) response.body().string();
                        String[] fullresponseArray = fullresponse.split(",");
                        System.out.println("array elements"+fullresponseArray[2]);
                        String[] usernameArray = fullresponseArray[1].split(":");
                       //intent.putExtra("key", "Hello" + " " +fullresponseArray[0] );
                        intent.putExtra("key", "Hello" + " " + usernameArray[1].replace("\"", ""));

                   } catch (IOException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                    } else {
                    Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                      }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, " Username or Password", Toast.LENGTH_SHORT).show();
            }
        });
   }
    }

