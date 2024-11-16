package com.xkx.book.activity.borrow;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xkx.book.R;
import com.xkx.book.adapter.BorrowAdapter;
import com.xkx.book.database.BookDBHelper;
import com.xkx.book.database.BorrowDBHistory;
import com.xkx.book.enity.Book;
import com.xkx.book.enity.Borrow;
import com.xkx.book.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class BorrowHistoryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView lv_view_borrow;
    private String userId, bookId, bookName;
    private BorrowDBHistory mHistory;
    private BookDBHelper nHelper;
    private BorrowAdapter borrowAdapter;

    private TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_history);
        textView1 = findViewById(R.id.tv_extra_info_1);

        lv_view_borrow = findViewById(R.id.lv_view_borrow);
        lv_view_borrow.setOnItemClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //获取数据库帮助器的实例
        mHistory = BorrowDBHistory.getInstance(this);
        nHelper = BookDBHelper.getInstance(this);
        //打开数据库帮助器的读写连接
        mHistory.openWriteLink();
        mHistory.openReadLink();
        nHelper.openWriteLink();
        nHelper.openReadLink();

        List<Borrow> borrows = mHistory.queryAll();
        List<Book> books = new ArrayList<>();
        for (Borrow borrow : borrows) {
            Log.e("ning", borrow.toString());
            Book book = nHelper.queryById(borrow.getBorrowBookId()).get(0);
            books.add(book);
        }

        int[] numbers = {0, 0, 0, 0, 0,0}; // 创建一个包含这些值的数组，长度为 5即可，
        for (Book book : books){
            String tags = book.getBookTags();
            // 使用 split 方法按照 & 分隔字符串
            String[] tagArray = tags.split("&");

            // 打印分隔后的标签
            for (String tag : tagArray) {
                if (tag.equals("文学与艺术")){
                    numbers[0] = numbers[0] + 1;
                }else if (tag.equals("科学与技术")){
                    numbers[1] = numbers[1] + 1;
                } else if (tag.equals("社会科学")){
                    numbers[2] = numbers[2] + 1;
                }else if (tag.equals("历史与人文")){
                    numbers[3] = numbers[3] + 1;
                }else if (tag.equals("生活与健康")){
                    numbers[4] = numbers[4] + 1;
                }
            }
        }
        String s = "文学与艺术:" + numbers[0] + "次\n" +
                "科学与技术:" + numbers[1] + "次\n" +
                "社会科学:" + numbers[2] + "次\n" +
                "历史与人文:" + numbers[3] + "次\n" +
                "生活与健康:" + numbers[4] + "次";

        textView1.setText(s);

        borrowAdapter = new BorrowAdapter(borrows, BorrowHistoryActivity.this);
        lv_view_borrow.setAdapter(borrowAdapter);

        lv_view_borrow.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Borrow borrow = (Borrow) adapterView.getItemAtPosition(position);
        userId = borrow.getBorrowId();
        bookId = borrow.getBorrowBookId();
        bookName = borrow.getBorrowBookName();
        Log.e("ning", borrow.toString());
        Log.e("ning", bookId);
//        AlertDialog.Builder builder = new AlertDialog.Builder(BorrowHistoryActivity.this);
//        builder.setTitle("是否删除该借阅记录？");
//        // 确认
//        builder.setPositiveButton("确认", (dialog, whichButton) -> {
//            // 获取图书数量
//            int booknum = 0;
//            List<Book> books = nHelper.queryById(bookId);
//            for (Book b : books) {
//                booknum = b.bookNumber;
//            }
//            // 更新图书信息
//            Book returnbook = new Book(bookId, bookName, booknum + 1);
//            // 删除借阅信息
//            if (nHelper.update(returnbook) > 0 && mHistory.deleteByid(userId, bookId) > 0)
//                ToastUtil.show(this, "删除成功");
//            onStart();
//        });
//        // 取消
//        builder.setNegativeButton("取消", (dialog, whichButton) -> dialog.dismiss());
//        builder.show();
    }
}
