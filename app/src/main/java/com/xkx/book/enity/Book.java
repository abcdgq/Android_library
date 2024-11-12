package com.xkx.book.enity;


public class Book {
    public String bookId; // 图书编号
    public String bookName; // 图书名称
    public int bookNumber; // 图书数量

    public Book() {

    }

    public Book(String bookId, String bookName, int bookNumber) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookNumber = bookNumber;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId='" + bookId + '\'' +
                ", bookName='" + bookName + '\'' +
                ", bookNumber=" + bookNumber +
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
}
