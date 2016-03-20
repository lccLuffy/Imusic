package com.lcc.imusic.service;

import com.orhanobut.logger.Logger;

import java.util.Random;

/**
 * Created by lcc_luffy on 2016/3/20.
 */
public class MusicPlayManager {

    public static final int PLAY_TYPE_LOOP = 1;
    public static final int PLAY_TYPE_ONE = 2;
    public static final int PLAY_TYPE_RANDOM = 3;

    private int currentIndex;
    private int allIndex;
    private int playType = PLAY_TYPE_LOOP;

    private int previousIndex = -1;

    private Random random;

    public MusicPlayManager(int currentIndex, int all) {
        this.currentIndex = currentIndex;
        this.allIndex = all;
    }

    public int playAtIndex(int index) {
        return changeIndex(index);
    }

    public int getPlayType() {
        return playType;
    }

    public void setPlayType(int playType) {
        this.playType = playType;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setAllIndex(int allIndex) {
        this.allIndex = allIndex;
    }

    public int prevIndex() {
        if (allIndex == 1) {
            return changeIndex(0);
        }

        if (playType == PLAY_TYPE_RANDOM)
            return randomIndex();
        return changeIndex((currentIndex - 1) % allIndex);
    }

    public int nextIndex() {
        if (allIndex == 1) {
            return changeIndex(0);
        }

        if (playType == PLAY_TYPE_RANDOM)
            return randomIndex();
        return changeIndex((currentIndex + 1) % allIndex);
    }

    public int randomIndex() {
        if (allIndex == 1) {
            return changeIndex(0);
        }
        if (random == null)
            random = new Random(System.currentTimeMillis());
        int randomIndex = random.nextInt(allIndex);
        while (randomIndex == currentIndex) {
            randomIndex = random.nextInt(allIndex);
        }
        return changeIndex(randomIndex);
    }

    public int indexByPlayType() {
        int result = 0;
        switch (playType) {
            case PLAY_TYPE_ONE:
                result = currentIndex;
            case PLAY_TYPE_RANDOM:
                result = randomIndex();
            default:
                result = (currentIndex + 1) % allIndex;
        }
        return changeIndex(result);
    }
    public int lastPlayedIndex()
    {
        return previousIndex;
    }
    public void setLastPlayedIndex(int previousIndex)
    {
        this.previousIndex = previousIndex;
    }

    public boolean hasPlayed()
    {
        return previousIndex != -1;
    }

    private int changeIndex(int index) {
        Logger.i("index %d :",index);
        currentIndex = index;
        return currentIndex;
    }
}
