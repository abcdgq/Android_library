package com.xkx.book.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xkx.book.R;

import java.util.List;

// 自定义适配器类：管理轮播页面


public class FindWayAdapter extends RecyclerView.Adapter<FindWayAdapter.FindWayViewHolder> {

    private String fullData; // 完整传入数据
    private List<String> data; // 解析后的数据部分
    private Context context;

    public FindWayAdapter(String fullData, List<String> data, Context context) {
        this.fullData = fullData; // 初始化完整数据
        this.data = data; // 初始化解析数据
        this.context = context;
    }

    @NonNull
    @Override
    public FindWayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载单个页面布局文件
        View view = LayoutInflater.from(context).inflate(R.layout.page_find_way, parent, false);
        return new FindWayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FindWayViewHolder holder, int position) {
        // 设置完整数据，显示在顶部
        holder.tvFullData.setText("完整位置数据：" + fullData + "\n\n" + "查看具体位置，请左右滑动");

        // 根据页面位置设置具体的文本内容和图片
        switch (position) {
            case 0:
                // 第一个页面的内容和图片
                holder.tvPageContent.setText(
                        "你要去往的是 " + data.get(0) + "，请前往中间走廊北侧的楼梯或南侧的电梯");
                holder.ivDirectionImage.setImageResource(R.drawable.img); // 设置 img.png
                break;
            case 1:
                // 第二个页面的内容和图片，根据层数设置图片
                holder.tvPageContent.setText(
                        "你要去往的是 " + data.get(1) + "，请根据以下图片寻找位置");
                String floor = data.get(0); // 获取楼层信息（如“1层”）
                if (floor.contains("1层")) {
                    holder.ivDirectionImage.setImageResource(R.drawable.img_1); // 设置 img_1.png
                } else if (floor.contains("2层")) {
                    holder.ivDirectionImage.setImageResource(R.drawable.img_2); // 设置 img_2.png
                } else if (floor.contains("3层")) {
                    holder.ivDirectionImage.setImageResource(R.drawable.img_3); // 设置 img_3.png
                } else {
                    holder.ivDirectionImage.setImageResource(android.R.color.transparent); // 缺省情况设置透明
                }
                break;
            case 2:
                // 第三个页面的内容和图片
                holder.tvPageContent.setText(
                        "你要寻找的是 " + data.get(2) + "，书架的序号为由北向南递增，由西向东递增，书架两侧会给出它的序号");
                holder.ivDirectionImage.setImageResource(R.drawable.img_4); // 设置 img_4.png
                break;
            case 3:
                // 第四个页面的内容和图片
                holder.tvPageContent.setText(
                        "你要寻找的书籍编号为 " + data.get(3) + "，在每个书架内，书籍编号由左向右递增，由上向下递增");
                holder.ivDirectionImage.setImageResource(R.drawable.img_5); // 设置 img_5.png
                break;
            default:
                holder.tvPageContent.setText("未知页面");
                holder.ivDirectionImage.setImageResource(android.R.color.transparent); // 缺省情况设置透明
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size(); // 页面数量（数据部分的长度）
    }

    // 内部类：管理控件引用
    static class FindWayViewHolder extends RecyclerView.ViewHolder {
        TextView tvFullData; // 显示完整数据的文本框
        TextView tvPageContent; // 显示每个页面具体内容的文本框
        ImageView ivDirectionImage; // 图片控件

        public FindWayViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFullData = itemView.findViewById(R.id.tv_full_data);
            tvPageContent = itemView.findViewById(R.id.tv_page_content);
            ivDirectionImage = itemView.findViewById(R.id.iv_direction_image);
        }
    }
}