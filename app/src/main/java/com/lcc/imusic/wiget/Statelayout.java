package com.lcc.imusic.wiget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcc.imusic.R;


/**
 * Created by lcc_luffy on 2016/1/30.
 */
public class StateLayout extends FrameLayout {
    private View contentView;
    private View emptyView;
    private View errorView;
    private View progressView;

    private TextView emptyTextView;
    private TextView errorTextView;
    private TextView progressTextView;

    private ImageView errorImageView;
    private ImageView emptyImageView;


    public StateLayout(Context context) {
        this(context, null);
    }


    public StateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StateLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        parseAttrs(context, attrs);

        emptyView.setVisibility(View.GONE);

        errorView.setVisibility(View.GONE);

        progressView.setVisibility(View.GONE);

        currentShowingView = contentView;
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        LayoutInflater inflater = LayoutInflater.from(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StateLayout, 0, 0);
        int progressViewId;
        Drawable errorDrawable;
        Drawable emptyDrawable;
        try {
            errorDrawable = a.getDrawable(R.styleable.StateLayout_errorDrawable);
            emptyDrawable = a.getDrawable(R.styleable.StateLayout_emptyDrawable);
            progressViewId = a.getResourceId(R.styleable.StateLayout_progressView, -1);
        } finally {
            a.recycle();
        }

        /******************************************************************************************/

        if (progressViewId != -1) {
            progressView = inflater.inflate(progressViewId, this, false);
        } else {
            progressView = inflater.inflate(R.layout.view_progress, this, false);
            progressTextView = (TextView) progressView.findViewById(R.id.progressTextView);
        }

        addView(progressView);
        /******************************************************************************************/

        /******************************************************************************************/

        errorView = inflater.inflate(R.layout.view_error, this, false);
        errorTextView = (TextView) errorView.findViewById(R.id.errorTextView);
        errorImageView = (ImageView) errorView.findViewById(R.id.errorImageView);
        if (errorDrawable != null) {
            errorImageView.setImageDrawable(errorDrawable);
        } else {
            errorImageView.setImageResource(R.mipmap.ic_error);
        }
        addView(errorView);
        /******************************************************************************************/

        /******************************************************************************************/

        emptyView = inflater.inflate(R.layout.view_empty, this, false);
        emptyTextView = (TextView) emptyView.findViewById(R.id.emptyTextView);
        emptyImageView = (ImageView) emptyView.findViewById(R.id.emptyImageView);
        if (emptyDrawable != null) {
            emptyImageView.setImageDrawable(emptyDrawable);
        } else {
            emptyImageView.setImageResource(R.mipmap.ic_empty);
        }
        addView(emptyView);
        /******************************************************************************************/

    }

    private void checkIsContentView(View view) {
        if (contentView == null && view != errorView && view != progressView && view != emptyView) {
            contentView = view;
            currentShowingView = contentView;
        }
    }

    public ImageView getErrorImageView() {
        return errorImageView;
    }

    public ImageView getEmptyImageView() {
        return emptyImageView;
    }

    private View currentShowingView;

    private void switchWithAnimation(final View toBeShown) {
        final View toBeHided = currentShowingView;
        if (toBeHided == toBeShown)
            return;

        if (toBeHided != null) {
            toBeHided.setVisibility(GONE);
        }

        if (toBeShown != null) {
            currentShowingView = toBeShown;
            toBeShown.setVisibility(VISIBLE);
        }
    }


    public void showContentView() {
        switchWithAnimation(contentView);
    }

    public void showEmptyView() {
        showEmptyView(null);
    }

    public void showEmptyView(String msg) {
        onHideContentView();
        if (!TextUtils.isEmpty(msg))
            emptyTextView.setText(msg);
        switchWithAnimation(emptyView);
    }

    public void showErrorView() {
        showErrorView(null);
    }

    public void showErrorView(String msg) {
        onHideContentView();
        if (msg != null)
            errorTextView.setText(msg);
        switchWithAnimation(errorView);
    }

    public void showProgressView() {
        showProgressView(null);
    }

    public void showProgressView(String msg) {
        onHideContentView();
        if (msg != null)
            progressTextView.setText(msg);
        switchWithAnimation(progressView);
    }

    public void setErrorAction(final OnClickListener onErrorButtonClickListener) {
        errorView.setOnClickListener(onErrorButtonClickListener);
    }


    public void setEmptyAction(final OnClickListener onEmptyButtonClickListener) {
        emptyView.setOnClickListener(onEmptyButtonClickListener);
    }

    protected void onHideContentView() {
        //Override me
    }


    /**
     * addView
     */

    @Override
    public void addView(View child) {
        checkIsContentView(child);
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        checkIsContentView(child);
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        super.addView(child, index, params);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int width, int height) {
        checkIsContentView(child);
        super.addView(child, width, height);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        checkIsContentView(child);
        return super.addViewInLayout(child, index, params);
    }

    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params, boolean preventRequestLayout) {
        checkIsContentView(child);
        return super.addViewInLayout(child, index, params, preventRequestLayout);
    }
}
