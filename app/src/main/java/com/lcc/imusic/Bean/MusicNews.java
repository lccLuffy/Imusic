package com.lcc.imusic.bean;

/**
 * Created by lcc_luffy on 2016/3/23.
 */
public class MusicNews {
    public String picture;
    public String title;
    public String url;

    public MusicNews(){}
    public MusicNews(String title, String picture, String description) {
        this.title = title;
        this.picture = picture;
        this.description = description;
    }

    public String description;
}
