//导入必要的包和类
package com.DiaryLYX;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import com.DiaryLYX.DB.initialization_dao;
import com.DiaryLYX.diary.diary;
import com.DiaryLYX.diary.update;
import com.DiaryLYX.diary.adapter;
import com.DiaryLYX.login.edit;
import com.DiaryLYX.login.login;
//继承自AppCompatActivity,提供了与Activity类相同的生命周期方法，并且可以与支持库中的其他组件一起使用,允许在旧版本的Android操作系统上使用AppCompat主题和样式。
//使用AppCompatActivity作为基类，可以编写兼容多个Android版本的应用程序，并确保应用程序在较旧的设备上也能正常运行。
public class MainActivity extends AppCompatActivity {
    // 声明变量
    private ListView listView;
    //用于适配日记的数据和视图，将日记数据显示在列表中的适配器类。在后续的逻辑中使用该变量来管理和显示日记数据。
    private com.DiaryLYX.diary.adapter adapter;
    private Button write;
    private Button close;
    private Button edit;
    //用于实现与数据库交互的数据访问对象（Data Access Object，DAO）模式。
    // 通过该类，可以对数据库进行增删改查等操作。
    //使用该变量来操作数据库，例如插入新的日记数据
    private com.DiaryLYX.DB.dao dao;
    //用来存放数据的数组
    // 可以向 list 中添加新的日记对象，对其进行遍历、访问操作。
    // newList 则可能用于存储从数据库中查询到的最新日记对象集合。
    private ArrayList<diary> list = new ArrayList<>();
    private ArrayList<diary> newList = new ArrayList<>();
    private int i = 0;
    String author="lyx";
    private TextView text;

    //用于抑制 lint 警告信息。
    @SuppressLint("MissingInflatedId")
    @Override
    //在onCreate方法中设置布局和初始化视图
    protected void onCreate(Bundle savedInstanceState) {
        // 初始化操作
        super.onCreate(savedInstanceState);
        //将布局文件与活动进行关联。
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listview);
        write=findViewById(R.id.write);
        close=findViewById(R.id.close);
        edit=findViewById(R.id.edit2);
        //初始化数据库操作对象。
        DbUtil();
        text=findViewById(R.id.text);

        //在onCreate方法中读取SharedPreferences中的作者信息
        //第一个参数是偏好设置的名称，第二个参数是访问模式
        SharedPreferences sharedPreferences= getSharedPreferences("test", Activity.MODE_PRIVATE);
        if(!sharedPreferences.getString("author", "").equals("")){
            author =sharedPreferences.getString("author", "");
        }

        //显示ListView并设置点击事件监听器
        list=dao.find(author);
        adapter = new adapter(MainActivity.this, R.layout.list_item, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {   //点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 点击列表项时触发的操作，跳转到 update 活动
                Intent intent = new Intent(MainActivity.this, update.class);
                i=position;//表示列表项位置的整数值
                intent.putExtra("d_author",list.get(i).getAuthor());
                intent.putExtra("d_date",list.get(i).getDate());
                intent.putExtra("d_title",list.get(i).getTitle());
                intent.putExtra("d_content",list.get(i).getContent());
                intent.putExtra("d_image",list.get(i).getImage());
                //启动目标活动，并希望在目标活动完成后返回结果。第一个参数是要启动的活动的 Intent 对象，
                // 第二个参数是请求码，用于在返回结果时标识该请求。这里使用变量 i 作为请求码。
                startActivityForResult(intent, i);
            }
        });
        //设置按钮的点击事件监听器：
        write.setOnClickListener(new View.OnClickListener() {
            // 点击“记录生活”按钮时触发的操作
            // 当用户点击按钮时，将创建一个带有作者数据的 Intent 对象，并启动名为 write 的日记写作活动。
            // 这样用户就可以在该活动中进行日记的编写和保存。
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, com.DiaryLYX.diary.write.class);
                intent.putExtra("author",author);
                startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            // 点击“编辑”按钮时触发的操作
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, edit.class);
                intent.putExtra("author",author);
                startActivity(intent);
            }
        });
        //当用户点击按钮时，将创建一个跳转到登录活动的 Intent 对象，并设置标志位以清除之上的所有活动栈。
        // 这样用户就可以从当前活动跳转到登录活动，且无法返回到之前的活动。
        close.setOnClickListener(new View.OnClickListener() {
            // 点击“关闭”按钮时触发的操作
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, login.class);
                //用于指定在启动目标活动之前清除目标活动之上的所有活动栈。
                // 这样，在跳转到登录活动后，用户无法返回到之前的活动。
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
            }
        });
        //设置ListView的长按事件监听器（OnItemLongClickListener）
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //删除对应的item索引
                dao.delete(list.get(position));
                list.remove(position);
                adapter.notifyDataSetChanged();
                return true;
            }
        });


    }
//实现onActivityResult方法，处理从更新日记界面返回的结果：
//检查返回结果是否为 RESULT_OK，
// 如果是，则进一步检查请求码是否与变量 i 相匹配。
// 如果匹配，则获取更新后的日记数据，并将其应用于对应的日记对象，然后更新数据库中的数据。
// 接着，重新获取最新的数据并刷新适配器，以反映更新后的结果。
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==i){
                // 获取更新后的日记数据并刷新列表项
                Bundle bundle = data.getExtras();
                diary d=list.get(i);
                d.setTitle(bundle.getString("title"));
                d.setContent(bundle.getString("content"));
                d.setImage(bundle.getString("image"));
                dao.update(d);
                //使用新的容器获得最新查询出来的数据
                newList = dao.find(author);
                //清除原容器里的所有数据
                list.clear();
                //将新容器里的数据添加到原来容器里
                list.addAll(newList);
                //通知适配器数据发生变化，刷新适配器
                adapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 实现DbUtil方法，用于初始化DAO对象
     */
    public void DbUtil(){
        dao = ((initialization_dao)this.getApplication()).getDao();
    }


    /**
     * 重写onResume方法，在页面重新回到活动时刷新ListView
     */
    @Override
    protected void onResume() {
        super.onResume();
        //使用新的容器获得最新查询出来的数据
        newList = dao.find(author);
        //清除原容器里的所有数据
        list.clear();
        //将新容器里的数据添加到原来容器里
        list.addAll(newList);
        //刷新适配器
        adapter.notifyDataSetChanged();
    }


}