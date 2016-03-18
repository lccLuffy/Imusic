package com.lcc.imusic.bean;

import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/17.
 */
public class Song {

    public int count;
    public int start;
    public int total;
    /**
     * rating : {"max":10,"average":"8.8","numRaters":2094,"min":0}
     * author : [{"name":"中岛美嘉"}]
     * alt_title : LOVE
     * image : https://img1.doubanio.com/spic/s4714892.jpg
     * tags : [{"count":540,"name":"中岛美嘉"},{"count":305,"name":"日本"},{"count":270,"name":"J-Pop"},{"count":253,"name":"中島美嘉"},{"count":95,"name":"Jpop"},{"count":75,"name":"女声"},{"count":41,"name":"MIKA"},{"count":40,"name":"Love"}]
     * mobile_link : http://m.douban.com/music/subject/1460672/
     * attrs : {"publisher":["ソニーミュージックエンタテインメント"],"singer":["中岛美嘉"],"discs":["1"],"pubdate":["2003"],"title":["LØVE"],"media":["CD"],"tracks":["1. Venus in The Dark\n2. Love Addict\n3. aroma\n4. 雪の華\n5. RESISTANCE (album version)\n6. FIND THE WAY\n7. marionette\n8. 接吻\n9. You send me love\n10. Be in Silence\n11. LOVE NO CRY\n12. 愛してる (album version)\n13. LAST WALTZ"],"version":["Import"]}
     * title : LØVE
     * alt : http://music.douban.com/subject/1460672/
     * id : 1460672
     */

    public List<MusicsEntity> musics;

    public static class MusicsEntity {
        /**
         * max : 10
         * average : 8.8
         * numRaters : 2094
         * min : 0
         */

        public RatingEntity rating;
        public String alt_title;
        public String image;
        public String mobile_link;
        public AttrsEntity attrs;
        public String title;
        public String alt;
        public String id;
        /**
         * name : 中岛美嘉
         */

        public List<AuthorEntity> author;
        /**
         * count : 540
         * name : 中岛美嘉
         */

        public List<TagsEntity> tags;


        public static class RatingEntity {
            public int max;
            public String average;
            public int numRaters;
            public int min;

        }

        public static class AttrsEntity {
            public List<String> publisher;
            public List<String> singer;
            public List<String> discs;
            public List<String> pubdate;
            public List<String> title;
            public List<String> media;
            public List<String> tracks;
            public List<String> version;

        }

        public static class AuthorEntity {
            public String name;
        }

        public static class TagsEntity {
            public int count;
            public String name;

        }
    }
}
