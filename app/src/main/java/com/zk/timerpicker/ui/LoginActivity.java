package com.zk.timerpicker.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.zk.timerpicker.MainActivity;
import com.zk.timerpicker.R;
import com.zk.timerpicker.gson.GsonTempClassOne;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private Button backBtn;
    private EditText userEditText;
    private EditText passwdEditText;
    private Button loginBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decoView = getWindow().getDecorView();
            decoView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login);

        registerDoms();

        registerListeners();
    }

    public void registerDoms() {
        backBtn = (Button) findViewById(R.id.login_back_btn);
        userEditText = (EditText) findViewById(R.id.login_user_edit);
        passwdEditText = (EditText) findViewById(R.id.login_passwd_edit);
        loginBtn = (Button) findViewById(R.id.login_btn);

        passwdEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    }

    public void registerListeners() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.this.finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNetRequests();
            }
        });

        ((Button)findViewById(R.id.login_test_btn)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                        editor.putBoolean("is_login", false);
                        editor.apply();
                    }
                }
        );
    }

    public void sendNetRequests() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://47.96.159.162/username").build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    GsonTempClassOne obj = gson.fromJson(responseData, GsonTempClassOne.class);
                    final String userName = obj.getData();
                    SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                    editor.putBoolean("is_login", true);
                    editor.putString("user_name", userName);
                    editor.apply();
                } catch (Exception e) {
                    Log.e("M - error", "fuckedup");
                }
            }
        }).start();
    }
}
