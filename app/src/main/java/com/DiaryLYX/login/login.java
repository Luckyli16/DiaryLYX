package com.DiaryLYX.login;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

import com.DiaryLYX.MainActivity;
import com.DiaryLYX.R;
//用户可以在登录页面输入用户名，点击登录按钮后，用户名、年龄和兴趣将保存在应用程序的SharedPreferences中，并跳转到主页面。
public class login extends AppCompatActivity {
    private EditText name;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        name=findViewById(R.id.name);
        login=findViewById(R.id.login);
    }
    //onClick方法是一个点击事件的回调方法，当用户点击登录按钮时被调用。
    // 在该方法中，通过getSharedPreferences方法获取一个SharedPreferences对象，用于存储用户信息。
    //通过SharedPreferences.Editor对象将用户输入的用户名、年龄和兴趣存储到SharedPreferences中。
    public void onClick(View view){
        SharedPreferences mySharedPreferences= getSharedPreferences("test", Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        //用putString的方法保存数据
        editor.putString("author", name.getText().toString());
        editor.putString("age", "18");
        editor.putString("like", "Android");
        //提交当前数据
        editor.commit();
        //通过Intent启动主页面（MainActivity），并跳转到该页面。
        Intent intent = new Intent(login.this, MainActivity.class);
        startActivity(intent);
    }


}
