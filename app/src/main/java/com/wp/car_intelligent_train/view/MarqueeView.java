package com.wp.car_intelligent_train.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 跑马灯（定时1S滚动一张图片）
 * @author wangping
 * @date 2018/7/7 17:44
 */
public class MarqueeView extends RecyclerView {
    private Thread thread = null;
    private AtomicBoolean shouldContinue = new AtomicBoolean(false);
    private Handler mHandler;
    private int size;
    private int position;
    private int nextPos;

    public MarqueeView(Context context) {
        super(context);
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void run() {
        //主线程的handler，用于执行Marquee的滚动消息
        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case 1://滚动到下一个位置
                            MarqueeView.this.smoothScrollToPosition(size == 0 ? position : position%size);
                            position++;
                            break;
                    }
                }
            };
        }
        if (thread == null) {
            thread = new Thread() {
                public void run() {
                    while (shouldContinue.get()) {
                        try {
                            Thread.sleep(2000L);
                        } catch (Exception e) {
                            Log.e("wangping", "", e);
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.what = 1;
                        if (nextPos != -1) {
                            position = nextPos;
                            nextPos = -1;
                        }
                        msg.sendToTarget();
                    }
                    //退出循环时清理handler
                    mHandler=null;
                }
            };
        }
    }

    private void init() {
        startMarquee(-1);
    }

    public void startMarquee(int nextPos) {
        if (shouldContinue.get()) return;
        if (nextPos != -1) {
            this.nextPos = nextPos % size;
        } else {
            this.nextPos = position;
        }
        shouldContinue.set(true);
        run();
        thread.start();
    }

    @Override
    /**
     * 在附到窗口的时候开始滚动
     */
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
        Log.d("wangping", "onAttachedToWindow");
    }

    @Override
    /**
     * 在脱离窗口时处理相关内容
     */
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopMarquee();
        Log.d("wangping", "onDetachedFromWindow");
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
//        switch (e.getAction()) {
//            case MotionEvent.ACTION_DOWN://按下
//                stopMarquee();
//                break;
//            case MotionEvent.ACTION_MOVE://移动
//                break;
//            case MotionEvent.ACTION_UP://松开
//                startMarquee(-1);
//                break;
//        }
//        return super.onTouchEvent(e);
        return false;
    }

    /**
     * 停止滚动
     */
    public void stopMarquee() {
        shouldContinue.set(false);
        thread = null;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public AtomicBoolean getShouldContinue() {
        return shouldContinue;
    }
}
