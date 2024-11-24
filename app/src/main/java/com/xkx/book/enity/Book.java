package com.xkx.book.enity;


public class Book {
    public String bookId; // 图书编号
    public String bookName; // 图书名称
    public int bookNumber; // 图书数量
    public String bookTags; //文学与艺术&科学与技术&社会科学&历史与人文&生活与健康(至少一个，多个之间用&连接)
    public String bookIntroduction;//图书简介
    public String bookLocation;//x层（1-3）-xyy室（x对应楼层，yy 01-12）-yyy书架（001-200）-xxxx（0001-9999）


    public Book() {

    }

    public Book(String bookId, String bookName, int bookNumber,String bookTags,
                String bookIntroduction,String bookLocation) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookNumber = bookNumber;
        this.bookTags = bookTags;
        this.bookIntroduction = bookIntroduction;
        this.bookLocation = bookLocation;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId='" + bookId + '\'' +
                ", bookName='" + bookName + '\'' +
                ", bookNumber=" + bookNumber +'\'' +
                ", bookTags=" + bookTags + '\'' +
                ", bookIntroduction=" + bookIntroduction +'\'' +
                ", bookLocation=" + bookLocation +'\'' +
                '}';
    }

    public String toRecommendString(){
        return "Book{" +
                //"bookId='" + bookId + '\'' +,实际上我不需要给用户展示书的id
                bookName + ',' +
                bookTags + ',' +
                bookIntroduction +',' +
                '}';
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(int bookNumber) {
        this.bookNumber = bookNumber;
    }

    public String getBookTags() {
        return bookTags;
    }

    public void setBookTags(String bookTags) {
        this.bookTags = bookTags;
    }

    public String getBookIntroduction() {
        return bookIntroduction;
    }

    public void setBookIntroduction(String bookIntroduction) {
        this.bookIntroduction = bookIntroduction;
    }

    public String getBookLocation() {
        return bookLocation;
    }

    public void setBookLocation(String bookLocation) {
        this.bookLocation = bookLocation;
    }
}
