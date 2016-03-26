package com.lcc.imusic.bean;

/**
 * Created by lcc_luffy on 2016/3/24.
 */
public class CommentBean {
    public String username;
    public String avatar;
    public String content;
    public String time = "3月22日";

    public CommentBean(String username, String avatar, String content, String time) {
        this.username = username;
        this.avatar = avatar;
        this.content = content;
        this.time = time;
    }

    public CommentBean() {
    }
}
