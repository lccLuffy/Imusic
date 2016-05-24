package com.lcc.imusic.bean;

import java.util.List;

/**
 * Created by lcc_luffy on 2016/5/24.
 */
public class MusicianItem {
    public String IDnumber;
    public String IDphotopath;
    public String addtime;
    public String nickname;
    public long id;
    public String avatar;
    public int userid;
    public int views;
    public int status;

    public List<SongsBean.SongItem> songs;
}
