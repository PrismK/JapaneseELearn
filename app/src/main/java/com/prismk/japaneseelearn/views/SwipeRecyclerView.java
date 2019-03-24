package com.prismk.japaneseelearn.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class SwipeRecyclerView extends RecyclerView {

    private static final String TAG = "RecycleView";
    private int xDown, yDown, xMove, yMove;
    /**
     * 当前选中的item索引（这个很重要）
     */
    private int curSelectPosition;
    private Scroller mScroller;
    Context mContext;
    private LinearLayout mCurItemLayout, mLastItemLayout;

    /**
     * 是否是第一次touch
     */
    private boolean isFirst = true;


    public SwipeRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        //初始化Scroller
        mScroller = new Scroller(context, new LinearInterpolator(context, null));
    }

    
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int x = (int)e.getX();
        int y = (int)e.getY();
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                //记录当前按下的坐标
                xDown = x;
                yDown = y;
                //计算选中哪个Item
                int firstPosition = ((LinearLayoutManager)getLayoutManager()).findFirstVisibleItemPosition();
                Rect itemRect = new Rect();

                final int count = getChildCount();
                for (int i=3; i<count-1; i++){
                    final View child = getChildAt(i);
                    if (child.getVisibility() == View.VISIBLE){
                        child.getHitRect(itemRect);
                        if (itemRect.contains(x, y)){
                            curSelectPosition = firstPosition + i;
                            break;
                        }
                    }
                }

            case MotionEvent.ACTION_MOVE:
                xMove = x;
                yMove = y;
                int dx = xMove - xDown;//为负时：手指向左滑动；为正时：手指向右滑动。这与Android的屏幕坐标定义有关
                int dy = yMove - yDown;//
                break;
            case MotionEvent.ACTION_UP://手抬起时
                mLastItemLayout = mCurItemLayout;
                break;
        }
        return super.onTouchEvent(e);
    }




    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mCurItemLayout.scrollBy(mScroller.getCurrX(), 0);
            invalidate();
        }
    }
}