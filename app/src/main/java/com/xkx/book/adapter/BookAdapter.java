package com.xkx.book.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xkx.book.R;
import com.xkx.book.enity.Book;

import java.util.ArrayList;
import java.util.List;


public class BookAdapter extends BaseAdapter {
    private List<Book> mData = new ArrayList<Book>();
    private Context context;

    public BookAdapter(List<Book> mData, Context context) {
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
        private TextView tv_book_id_show;
        private TextView tv_book_name_show;
        private TextView tv_book_balance_show;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //定义ViewHolder对象
        BookAdapter.ViewHolder holder;
        if (view == null) {
            holder = new BookAdapter.ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.book_item, viewGroup, false);
//            viewGroup.addView(view);
            holder.tv_book_id_show = view.findViewById(R.id.tv_book_id_show);
            holder.tv_book_name_show = (TextView) view.findViewById(R.id.tv_book_name_show);
            holder.tv_book_balance_show = (TextView) view.findViewById(R.id.tv_book_balance_show);
            view.setTag(holder);
        } else {
            //复用选项
            holder = (BookAdapter.ViewHolder) view.getTag();
        }
//设置列表项数据
        holder.tv_book_id_show.setText(mData.get(i).getBookId());
        holder.tv_book_name_show.setText(mData.get(i).getBookName());
        holder.tv_book_balance_show.setText(String.valueOf(mData.get(i).getBookNumber()));
        return view;
    }

}
