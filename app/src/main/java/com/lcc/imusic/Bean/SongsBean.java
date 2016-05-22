package com.lcc.imusic.bean;

import java.util.List;

/**
 * Created by lcc_luffy on 2016/5/22.
 */
public class SongsBean {
    public int totalRow;
    public int pageNumber;
    public boolean firstPage;
    public boolean lastPage;
    public int totalPage;
    public int pageSize;
    /**
     * cover : /file/photo4music/1/断掉的霜之哀伤1.jpg
     * songpath : /file/music/1/Jim Brickman - Serenade.mp3
     * lyric : 霜之哀伤饿了
     * addtime : 2016-05-15 01:06:15
     * musicianName : supermttt
     * musicianid : 1
     * id : 14
     * views : 49
     * songname : Jim Brickman - Serenade.mp3
     * status : 2
     */

    public List<ListBean> list;

    public static class ListBean {
        public String cover;
        public String songpath;
        public String lyric;
        public String addtime;
        public String musicianName;
        public int musicianid;
        public int id;
        public int views;
        public String songname;
        public int status;
    }
}
