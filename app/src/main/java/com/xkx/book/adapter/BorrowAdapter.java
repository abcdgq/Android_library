package com.xkx.book.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xkx.book.R;
import com.xkx.book.enity.Borrow;

import java.util.ArrayList;
import java.util.List;

public class BorrowAdapter extends BaseAdapter {
    private List<Borrow> mData = new ArrayList<Borrow>();
    public Context context;

    public BorrowAdapter(List<Borrow> mData, Context context) {
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
    private static class ViewHolder {
        private TextView tv_borrow_id_show;
        private TextView tv_borrow_book_id_show;
        private TextView tv_borrow_book_name_show;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //定义ViewHolder对象
        BorrowAdapter.ViewHolder holder;
        if (view == null) {
            holder = new BorrowAdapter.ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.borrow_item, viewGroup, false);
//            viewGroup.addView(view);
            holder.tv_borrow_id_show = view.findViewById(R.id.tv_borrow_id_show);
            holder.tv_borrow_book_id_show = (TextView) view.findViewById(R.id.tv_borrow_book_id_show);
            holder.tv_borrow_book_name_show = (TextView) view.findViewById(R.id.tv_borrow_book_name_show);
            view.setTag(holder);
        } else {
            //复用选项
            holder = (BorrowAdapter.ViewHolder) view.getTag();
        }
//设置列表项数据
        holder.tv_borrow_id_show.setText(mData.get(i).getBorrowId());
        holder.tv_borrow_book_id_show.setText(mData.get(i).getBorrowBookId());
        holder.tv_borrow_book_name_show.setText(mData.get(i).getBorrowBookName());
        return view;
    }
}
