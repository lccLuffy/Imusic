package com.lcc.imusic.bean;

import java.util.List;

/**
 * Created by lcc_luffy on 2016/5/29.
 */
public class TopicReply {

    /**
     * totalRow : 11
     * pageNumber : 1
     * firstPage : true
     * lastPage : true
     * totalPage : 1
     * pageSize : 20
     * list : [{"topicid":8,"addtime":"2016-05-17 23:41:42","enable":1,"authorName":"JackLee","id":20,"text":"都别拦着我，让我自虐的听完Gala的Garbage英文。。一整张","avatar":"/file/avatar/9/树懒.jpg","userid":9},{"topicid":8,"addtime":"2016-05-18 00:44:02","enable":1,"authorName":"LuckyPanda","id":21,"text":"天呐！居然是04年的，这些年我都错过了什么！","avatar":"/file/avatar/50/pandaSmall4.jpg","userid":50},{"topicid":8,"addtime":"2016-05-18 01:51:36","enable":1,"authorName":"LuckyDog","id":23,"text":"为何我在young for u中听出了伤感，想哭","avatar":"/file/avatar/51/avatar-lcl.JPG","userid":51},{"topicid":8,"addtime":"2016-05-18 12:31:32","enable":1,"authorName":"wwwtttwwwttt","id":24,"text":"321","avatar":"/file/avatar/48/login.png","userid":48},{"topicid":8,"addtime":"2016-05-18 12:31:37","enable":1,"authorName":"wwwtttwwwttt","id":25,"text":"123","avatar":"/file/avatar/48/login.png","userid":48},{"topicid":8,"addtime":"2016-05-18 12:31:40","enable":1,"authorName":"wwwtttwwwttt","id":26,"text":"321","avatar":"/file/avatar/48/login.png","userid":48},{"topicid":8,"addtime":"2016-05-18 13:27:48","enable":1,"authorName":"JackLee","id":27,"text":"重新再来听这首歌 ， 以前第一次听的时候感觉节奏感很好。慢慢的听出了年轻的激情与不羁，现在再听，听出的却是青春的无力与伤感！","avatar":"/file/avatar/9/树懒.jpg","userid":9},{"topicid":8,"addtime":"2016-05-18 22:37:54","enable":1,"authorName":"LuckyPanda","id":28,"text":"慢慢的听出了年轻的激情与不羁，现在再听，听出的却是青春的无力与伤感！","avatar":"/file/avatar/50/pandaSmall4.jpg","userid":50},{"topicid":8,"addtime":"2016-05-18 22:44:39","enable":1,"authorName":"LuckyDog","id":29,"text":"我放young for you 室友听到说好逗比啊","avatar":"/file/avatar/51/avatar-lcl.JPG","userid":51},{"topicid":8,"addtime":"2016-05-18 22:51:17","enable":1,"authorName":"JackLee","id":30,"text":"蛇精病~太喜欢了~","avatar":"/file/avatar/9/树懒.jpg","userid":9},{"topicid":8,"addtime":"2016-05-18 22:55:12","enable":1,"authorName":"LuckyPanda","id":31,"text":"快乐的节奏","avatar":"/file/avatar/50/pandaSmall4.jpg","userid":50}]
     */

    public int totalRow;
    public int pageNumber;
    public boolean firstPage;
    public boolean lastPage;
    public int totalPage;
    public int pageSize;
    /**
     * topicid : 8
     * addtime : 2016-05-17 23:41:42
     * enable : 1
     * authorName : JackLee
     * id : 20
     * text : 都别拦着我，让我自虐的听完Gala的Garbage英文。。一整张
     * avatar : /file/avatar/9/树懒.jpg
     * userid : 9
     */

    public List<TopicReplyItem> list;

    public static class TopicReplyItem {
        public int topicid;
        public String addtime;
        public int enable;
        public String authorName;
        public int id;
        public String text;
        public String avatar;
        public int userid;
    }
}
