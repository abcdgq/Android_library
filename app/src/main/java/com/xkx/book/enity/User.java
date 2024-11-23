package com.xkx.book.enity;

public class User {
    public int id = 1; //序号
    public String userid; //用户id
    public String username; //姓名

    public String tag;
    public String prefer;

    public long password; //密码
    public int is_book;//借书状态
    public int user_status; //管理员状态
    public int is_deleted;//删除状态

    public User() {
    }

    public User(String userid, String username, long password, int is_book, int user_status, int is_deleted) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.is_book = is_book;
        this.user_status = user_status;
        this.is_deleted = is_deleted;
        this.tag = "空";
        this.prefer = "空";
    }

    public User(String userid, String username, long password, int is_book, int user_status, int is_deleted, String tags, String prefer) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.is_book = is_book;
        this.user_status = user_status;
        this.is_deleted = is_deleted;
        this.tag = tags;
        this.prefer = prefer;
    }

    public String getPrefer() {
        return this.prefer;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getPassword() {
        return password;
    }

    public void setPassword(long password) {
        this.password = password;
    }

    public int getIs_book() {
        return is_book;
    }

    public void setIs_book(int is_book) {
        this.is_book = is_book;
    }

    public int getUser_status() {
        return user_status;
    }

    public void setUser_status(int user_status) {
        this.user_status = user_status;
    }

    public int getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(int is_deleted) {
        this.is_deleted = is_deleted;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userid=" + userid +
                ", username='" + username + '\'' +
                ", password=" + password +
                ", is_book=" + is_book +
                ", user_status=" + user_status +
                ", is_deleted=" + is_deleted +
                '}';
    }
}
