package com.xkx.book.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xkx.book.R;
import com.xkx.book.enity.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends BaseAdapter {
    private List<User> mData = new ArrayList<User>();
    public Context context;

    public UserAdapter(List<User> mData, Context context) {
        this.mData = mData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    //绑定数据到listview
//    定义ViewHolder静态类
    static class ViewHolder {
        private TextView tv_id_show;
        private TextView tv_name_show;
        private TextView tv_password_show;
        private TextView tv_isadmin_show;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //定义ViewHolder对象
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.user_item, viewGroup, false);
//            viewGroup.addView(view);
            holder.tv_id_show = view.findViewById(R.id.tv_id_show);
            holder.tv_name_show = (TextView) view.findViewById(R.id.tv_name_show);
            holder.tv_password_show = (TextView) view.findViewById(R.id.tv_pwd_show);
            holder.tv_isadmin_show = (TextView) view.findViewById(R.id.tv_isadmin_show);
            view.setTag(holder);
        } else {
            //复用选项
            holder = (ViewHolder) view.getTag();
        }
//设置列表项数据
        holder.tv_id_show.setText(mData.get(i).getUserid());
        holder.tv_name_show.setText(mData.get(i).getUsername());
        holder.tv_password_show.setText(String.valueOf(mData.get(i).getPassword()));
        holder.tv_isadmin_show.setText(String.valueOf(mData.get(i).getUser_status()));
        return view;
    }
}
