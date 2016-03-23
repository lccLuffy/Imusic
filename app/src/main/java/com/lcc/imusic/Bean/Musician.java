package com.lcc.imusic.bean;

/**
 * Created by lcc_luffy on 2016/3/22.
 */
public class Musician {
    public Musician() {

    }

    public Musician(long id, String avatar, String name) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
    }

    public long id;
    public String avatar;
    public String name;
}
