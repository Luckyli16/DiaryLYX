package com.DiaryLYX.login;
//用于编辑用户信息的Android应用程序的活动（Activity）类
//通过这个活动类，用户可以编辑自己的用户名（author）、年龄（age）和兴趣（like），并将这些信息保存在应用程序的SharedPreferences中。
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.DiaryLYX.R;

public class edit extends AppCompatActivity {
//类定义了各种成员变量，例如author、age、like、sharedPreferences、editor、aName、sexual、ages等，
// 代表了用户界面元素和数据存储对象。
    private EditText author;
    private EditText age;
    private EditText like;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String aName;
    private String sexual;
    private String ages;
    //作为该活动的入口点。它初始化用户界面元素，并从SharedPreferences中读取之前保存的用户信息。
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        sharedPreferences = getSharedPreferences("test", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        author=findViewById(R.id.edt_setusername);
        like = findViewById(R.id.like);
        age=findViewById(R.id.edt_setage);

        aName = sharedPreferences.getString("author", null);
        sexual = sharedPreferences.getString("like", null);
        ages=sharedPreferences.getString("age",null);

        if(aName.equals("")){
            author.setText("");
        }
        else{
            author.setText(aName);
        }

        like.setText(sexual);
        age.setText(ages);

    }
    //当活动暂停时会被调用。在该方法中调用saveAuthorInfo方法，
    // 将用户编辑的信息保存到SharedPreferences中。
    @Override
    protected void onPause() {
        super.onPause();
        saveAuthorInfo();
    }

    /**
     * 用于将编辑的用户信息保存到SharedPreferences中。
     * 它获取author、age和like的文本内容，
     * 并使用editor对象将它们存储到SharedPreferences中。
     */
    private void saveAuthorInfo(){
        //save
        editor.putString("author", author.getText().toString());
        editor.putString("age", age.getText().toString());
        editor.putString("like", like.getText().toString());
        editor.commit();
    }
}
