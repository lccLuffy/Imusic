package com.lcc.imusic.bean;

import java.util.List;

/**
 * Created by lcc_luffy on 2016/5/22.
 */
public class MusiciansBean {

    public int totalRow;
    public int pageNumber;
    public boolean firstPage;
    public boolean lastPage;
    public int totalPage;
    public int pageSize;

    public List<MuiscianItem> list;

    public static class MuiscianItem {
        public String IDnumber;
        public String IDphotopath;
        public String addtime;
        public String nickname;
        public int id;
        public String avatar;
        public int userid;
        public int views;
        public int status;
        /**
         * cover : /file/photo4music/36/Young For You.jpg
         * songpath : /file/music/36/Young For You.mp3
         * lyric : sunday's coming I wanna drive my car 星期天啊，我想开着我的座驾 to your apartment with present like a star 像大明星一样带着礼物来到你家 forecaster said the weather may be rainy hard 天气预报说今天可能雨很大 but I know the sun will shine for us 但我坚信阳光会为你我洒 oh lazy seagull fly me from the dark 黄昏中飞来一只慵懒的海鸟 I dress my jeans and feed my monkey banana 我穿上牛仔裤给猴哥喂一点儿香蕉 then I think my age how old,skyline how far 然后思考：我有多老，天有多高？ or we need each other in california 还是你我更该去加利福利亚 you show me your body before night comes down 夜晚来临之时，你展示妖娆的身段儿 I touch your face and promise to stay ever young 我摸着你小脸儿，答应永远年轻有范儿 on this ivory beach we kissed so long 在象牙色的海滩我们拥吻不断 it seems that the passion's never gone 如同激情永远不说再见 you sing me your melody and I feel so please 你对我哼着小曲儿，我也相当的惬意 I want you to want me to keep your dream 我感觉你想，让我继续留在你梦里 together we'll run wild by a summer symphony 夏季的交响乐里我们撒欢儿淘气 this is what we enjoyed not a fantasy 这就是真实发生而不是一场游戏 the tin-man's surfing I wanna try my luck 看他们冲浪，我也想玩儿一下心跳 to the top of tide rip like just have some drugs 直冲浪尖的感觉如同在嗑药 I know you have no blame for my proud moonish heart 我知道你不怪我抓狂又特别孤傲 Welcome to the golden beatnik park 在这黄金乐园，我们已经垮掉 oh diamond seashore drag me from the yard 哦，钻石般海滩将我拖出后院 incredible sunward I watch as you're in photograph 阳光下的画卷，你竟融化在我眼前 for camera your smile's so sweet,palm trees' so lush 你对镜头笑很甜，棕榈树也很炫 would you believe my honey it's califonia 亲爱的，这就是加州，你信吗？我的天！ you show me your body before night comes down 夜晚来临之时，你展示妖娆的身段儿 I touch your face and promise to stay ever young 我摸着你小脸儿，答应永远年轻有范儿 on this ivory beach we kissed so long 在象牙色的海滩我们拥吻不断 it seems that the passion's never gone 如同激情永远不说再见 you sing me your melody and I feel so please 你对我哼着小曲儿，我也相当的惬意 I want you to want me to keep your dream 我感觉你想，让我继续留在你梦里 together we'll run wild by a summer symphony 夏季的交响乐里我们撒欢儿淘气 this is what we enjoyed not a fantasy 这就是真实发生而不是一场游戏
         * addtime : 2016-05-15 15:15:37
         * musicianName : GALA
         * musicianid : 30
         * id : 23
         * views : 507
         * songname : Young For You.mp3
         * status : 2
         */

        public List<SongsBean.SongItem> songs;
    }
}

