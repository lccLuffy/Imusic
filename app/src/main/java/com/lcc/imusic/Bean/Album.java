package com.lcc.imusic.bean;

/**
 * Created by lcc_luffy on 2016/3/22.
 */
public class Album {
    public Album() {

    }

    public Album(long id, String cover, String title) {
        this.id = id;
        this.cover = cover;
        this.title = title;
    }

    long id;
    public String cover;
    public String title;
}
