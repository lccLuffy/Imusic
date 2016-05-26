package com.lcc.imusic.bean;

import java.util.List;

/**
 * Created by lcc_luffy on 2016/5/26.
 */
public class CollectedSongs {

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

    public List<CollectedSongItem> list;

    public static class CollectedSongItem {
        /**
         * enable : 1
         * addtime : 2016-05-26 17:39:13
         * name : One Night In 北京.mp3
         * songid : 22
         */
        public int enable;
        public String addtime;
        public String name;
        public int songid;
    }
}
