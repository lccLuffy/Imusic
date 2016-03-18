package com.lcc.imusic.wiget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;

/**
 * Created by lcc_luffy on 2016/3/18.
 */
public class FromUserCheckBox extends CheckBox{
    private boolean fromUser = false;
    public FromUserCheckBox(Context context) {
        super(context);
    }

    public FromUserCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FromUserCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FromUserCheckBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setChecked(boolean checked) {
        fromUser = true;
        super.setChecked(checked);
    }
}
