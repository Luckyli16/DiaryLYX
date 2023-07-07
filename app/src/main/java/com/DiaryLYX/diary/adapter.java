package com.DiaryLYX.diary;
//用于在 ListView 中显示日记列表的适配器
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import com.DiaryLYX.R;

public class adapter extends ArrayAdapter<diary> {

    private Context context;
    private int resource;
    private ArrayList<diary> diaryList;

    public adapter(@NonNull Context context, int resource, @NonNull ArrayList<diary> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        diaryList = objects;
    }
    //根据位置 position 获取对应位置的日记对象。
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;//可重用的视图对象，用于提高性能。
        DiaryHolder holder = null;

        if (row == null) { //若是第一次加载页面则调用此方法初始化
            //使用 LayoutInflater 实例获取布局资源对应的视图，并将其设置为 row 变量。
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new DiaryHolder();
            holder.title = row.findViewById(R.id.diary_title);
            holder.date =  row.findViewById(R.id.diary_date);

            row.setTag(holder);
        } else {
            holder = (DiaryHolder) row.getTag();
        }
        //将数据传递给控件并显示
        diary diary = getItem(position);
        holder.title.setText(diary.getTitle());
        holder.date.setText(diary.getDate());

        return row;
    }
    static class DiaryHolder {//在每个listview中显示标题和日期
        TextView title, date;
    }
}