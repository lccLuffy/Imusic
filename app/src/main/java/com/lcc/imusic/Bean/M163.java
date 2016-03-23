package com.lcc.imusic.bean;

import java.util.List;

/**
 * Created by lcc_luffy on 2016/3/21.
 */
public class M163 {

    public ResultBean result;
    public int code;

    public static class ResultBean {
        public Object artists;
        public long updateTime;
        public String coverImgUrl;
        public int specialType;
        public String description;
        public String name;
        public int id;

        public List<TracksBean> tracks;
        public List<String> tags;

        public static class TracksBean {
            public int playedNum;
            public String mp3Url;
            /**
             * bitrate : 160000
             * playTime : 178259
             * dfsId : 5915372557638586
             * sr : 44100
             * volumeDelta : -1.27
             * name : Where is Spring?
             * id : 46824278
             * size : 3612442
             * extension : mp3
             */

            public MMusicBean mMusic;
            /**
             * bitrate : 96000
             * playTime : 178259
             * dfsId : 5806520906479763
             * sr : 44100
             * volumeDelta : -1.3
             * name : Where is Spring?
             * id : 46824279
             * size : 2185948
             * extension : mp3
             */

            public LMusicBean lMusic;
            public int status;
            public Object crbt;
            public Object rtUrl;
            public String copyFrom;
            /**
             * bitrate : 96000
             * playTime : 178259
             * dfsId : 5806520906479763
             * sr : 44100
             * volumeDelta : -1.3
             * name : Where is Spring?
             * id : 46824279
             * size : 2185948
             * extension : mp3
             */

            public BMusicBean bMusic;
            public String commentThreadId;

            public AlbumBean album;
            public int fee;
            public int mvid;
            public int ftype;
            public int rtype;
            public Object rurl;
            public int position;
            public int duration;


            public AuditionBean audition;
            public String ringtone;
            public String disc;
            public int no;
            public String name;
            public int id;
            public List<?> alias;

            public List<ArtistsBean> artists;

            public static class MMusicBean {
                public int bitrate;
                public int playTime;
                public long dfsId;
                public int sr;
                public double volumeDelta;
                public String name;
                public int id;
                public int size;
                public String extension;
            }

            public static class LMusicBean {
                public int bitrate;
                public int playTime;
                public long dfsId;
                public int sr;
                public double volumeDelta;
                public String name;
                public int id;
                public int size;
                public String extension;
            }

            public static class BMusicBean {
                public int bitrate;
                public int playTime;
                public long dfsId;
                public int sr;
                public double volumeDelta;
                public String name;
                public int id;
                public int size;
                public String extension;
            }

            public static class AlbumBean {
                public String blurPicUrl;
                public int copyrightId;
                public long picId;
                public int companyId;
                public long pic;
                public String briefDesc;
                public String tags;
                public int status;
                public String picUrl;
                public String commentThreadId;
                /**
                 * img1v1Id : 0
                 * alias : []
                 * picId : 0
                 * briefDesc :
                 * trans :
                 * musicSize : 0
                 * picUrl : http://p4.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
                 * albumSize : 0
                 * img1v1Url : http://p3.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
                 * name :
                 * id : 0
                 */

                public ArtistBean artist;
                public long publishTime;
                public String company;
                public String description;
                public String name;
                public int id;
                public String type;
                public int size;
                public List<?> songs;
                public List<String> alias;
                /**
                 * img1v1Id : 0
                 * alias : []
                 * picId : 0
                 * briefDesc :
                 * trans :
                 * musicSize : 0
                 * picUrl : http://p3.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
                 * albumSize : 0
                 * img1v1Url : http://p4.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
                 * name : The Shanghai Restoration Project
                 * id : 31286
                 */

                public List<ArtistsBean> artists;

                public static class ArtistBean {
                    public int img1v1Id;
                    public int picId;
                    public String briefDesc;
                    public String trans;
                    public int musicSize;
                    public String picUrl;
                    public int albumSize;
                    public String img1v1Url;
                    public String name;
                    public int id;
                    public List<?> alias;
                }

                public static class ArtistsBean {
                    public int img1v1Id;
                    public int picId;
                    public String briefDesc;
                    public String trans;
                    public int musicSize;
                    public String picUrl;
                    public int albumSize;
                    public String img1v1Url;
                    public String name;
                    public int id;
                    public List<?> alias;
                }
            }

            public static class AuditionBean {
                public int bitrate;
                public int playTime;
                public long dfsId;
                public int sr;
                public int volumeDelta;
                public String name;
                public int id;
                public int size;
                public String extension;
            }

            public static class ArtistsBean {
                public int img1v1Id;
                public int picId;
                public String briefDesc;
                public String trans;
                public int musicSize;
                public String picUrl;
                public int albumSize;
                public String img1v1Url;
                public String name;
                public int id;
                public List<?> alias;
            }
        }
    }
}
