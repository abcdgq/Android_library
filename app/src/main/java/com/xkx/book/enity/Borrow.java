package com.xkx.book.enity;

public class Borrow {
    public String borrowId; // 借阅用户id
    public String borrowBookId; // 借阅图书id
    public String borrowBookName; // 借阅图书名称

    public Borrow() {
    }

    public Borrow(String borrowId, String borrowBookId, String borrowBookName) {
        this.borrowId = borrowId;
        this.borrowBookId = borrowBookId;
        this.borrowBookName = borrowBookName;
    }

    public Borrow(String borrowId, String borrowBookId) {
        this.borrowId = borrowId;
        this.borrowBookId = borrowBookId;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public String getBorrowBookId() {
        return borrowBookId;
    }

    public void setBorrowBookId(String borrowBookId) {
        this.borrowBookId = borrowBookId;
    }

    public String getBorrowBookName() {
        return borrowBookName;
    }

    public void setBorrowBookName(String borrowBookName) {
        this.borrowBookName = borrowBookName;
    }

    @Override
    public String toString() {
        return "Borrow{" +
                "borrowId='" + borrowId + '\'' +
                ", borrowBookId='" + borrowBookId + '\'' +
                ", borrowBookName='" + borrowBookName + '\'' +
                '}';
    }

}

