package com.lcc.imusic.bean;

import java.util.List;

/**
 * Created by lcc_luffy on 2016/5/28.
 */
public class Club {

    /**
     * totalRow : 3
     * pageNumber : 1
     * firstPage : true
     * lastPage : true
     * totalPage : 1
     * pageSize : 20
     * list : [{"addtime":"2016-05-28 20:56:00","enable":1,"musicianName":"GALA","musicianid":30,"id":27,"text":"我去饿我去额为全额","avatar":"/file/avatar/13/QQ图片201605231322041.jpg","title":"w'q'e'w'q'e'w","userid":13,"viewscount":1,"authorNmae":"lcc_luffy","replycount":0},{"addtime":"2016-05-17 23:34:32","enable":1,"musicianName":"GALA","musicianid":30,"id":9,"text":"仍然是嬉笑自嘲，仍然不靠谱，早已经不是少年，还是满腔少年心气。不赶紧攒钱买房，还一心奔跑在路上，最初的一点梦想都在手心化成了糖浆，还紧握住不放，被视作痴人也不在意，怀着一颗赤子之心一路奔跑。六年时光的打磨并不能使所有人都向生活俯首称臣，这张《追梦痴子心》","avatar":"/file/avatar/9/树懒.jpg","title":"追梦痴子心","userid":9,"viewscount":36,"authorNmae":"JackLee","replycount":6},{"addtime":"2016-05-17 22:16:57","enable":1,"musicianName":"GALA","musicianid":30,"id":8,"text":"Gala录这张唱片的时候没有鼓手，是只有主唱、吉他手、贝司手的乐队，我们在唱片中所听到的鼓是由吉他手和贝司手两个二把刀鼓手兼任所打，打得并不稳，但走向抓得很稳，如果能有一个训练有素的鼓手，他们的想法一定会实现得更到位。而换用DIY的标准界定，这张唱片对于他们已经是一个很好的开始了，既不乏大量出色的旋律，也不乏恰如其分的野心，有没有鼓手不是问题，重要的是一张唱片可以体现多少想法？表现多少驾御音乐的能力和素质？毕竟我们鼓励DIY唱片并不代表我们就鼓励所有想做音乐的人都凭着一腔热情去玩音乐，从而滥化了整个环境，做音乐的人和想做音乐的人之间还是有很大的区别的，而《Young For You》，无论是聪明顽劣的旋律，还是毛手毛脚的鼓点，相信它都属于值得期待和鼓励的前者。开玩笑地想：甚至认为他们在拥有了技术全面的鼓手以后，也该时不时地尝试一下现在这种毛手毛脚的风格\u2014\u2014不失技术含金量的毛手毛脚。","avatar":"/file/avatar/9/树懒.jpg","title":"Young For You","userid":9,"viewscount":56,"authorNmae":"JackLee","replycount":11}]
     */

    public int totalRow;
    public int pageNumber;
    public boolean firstPage;
    public boolean lastPage;
    public int totalPage;
    public int pageSize;
    /**
     * addtime : 2016-05-28 20:56:00
     * enable : 1
     * musicianName : GALA
     * musicianid : 30
     * id : 27
     * text : 我去饿我去额为全额
     * avatar : /file/avatar/13/QQ图片201605231322041.jpg
     * title : w'q'e'w'q'e'w
     * userid : 13
     * viewscount : 1
     * authorNmae : lcc_luffy
     * replycount : 0
     */

    public List<ClubItem> list;

    public static class ClubItem {
        public String addtime;
        public int enable;
        public String musicianName;
        public int musicianid;
        public int id;
        public String text;
        public String avatar;
        public String title;
        public int userid;
        public int viewscount;
        public String authorNmae;
        public int replycount;
    }
}
