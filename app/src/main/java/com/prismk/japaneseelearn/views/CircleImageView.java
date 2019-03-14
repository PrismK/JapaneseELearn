package com.prismk.japaneseelearn.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;

import com.prismk.japaneseelearn.R;

public class CircleImageView extends android.support.v7.widget.AppCompatImageView {
    private static final Xfermode MASK_XFERMODE;
    public static final int SHAPE_NONE   = 0;
    public static final int SHAPE_CIRCLE = 1;
    public static final int SHAPE_RECT   = 2;
    private Bitmap mask;
    private Paint paint;

    private int mDrawShapeType = 0;//形状:0表示不绘制形状，和正常的ImageVIew一样 1表示圆(椭圆)，2圆角矩形，3
    private boolean mIsDrawBoarder = false;//是否绘制边
    private int mBorderWidth = 10;
    private int mBorderColor = Color.parseColor("#f2f2f2");
    private int mRectRound = 10;//圆角矩形的圆角半径

    private boolean mIsDrawShadow = false;//是否绘制渐变阴影
    private int mShadowWidth = 10;
    private int mShadowColorStart = Color.parseColor("#ff5d5d5d");
    private int mShadowColorMiddle = Color.parseColor("#ff5d5d5d");
    private int mShadowColorEnd = Color.parseColor("#00ffffff");

    static {
        PorterDuff.Mode localMode = PorterDuff.Mode.DST_IN;
        MASK_XFERMODE = new PorterDuffXfermode(localMode);
    }

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
        readCustomAttribute(context, attributeSet);
    }

    public CircleImageView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        readCustomAttribute(context, attributeSet);
    }

    public void readCustomAttribute(Context context, AttributeSet attributeSet){
        try {
            TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.CircleImageView);

            mDrawShapeType = a.getInt(R.styleable.CircleImageView_shape, mDrawShapeType);

            mIsDrawBoarder = a.getBoolean(R.styleable.CircleImageView_shadow, mIsDrawBoarder);
            mBorderColor = a.getColor(R.styleable.CircleImageView_border_color, mBorderColor);
            final int defalut = (int) (2 * context.getResources().getDisplayMetrics().density + 0.5f);
            mBorderWidth = a.getDimensionPixelOffset(R.styleable.CircleImageView_border_width, defalut);
            mRectRound = a.getDimensionPixelOffset(R.styleable.CircleImageView_rect_roundcorner, mRectRound);

            mIsDrawShadow = a.getBoolean(R.styleable.CircleImageView_shadow, mIsDrawShadow);
            mShadowWidth = a.getDimensionPixelOffset(R.styleable.CircleImageView_shadow_width, defalut);
            mShadowColorStart = a.getColor(R.styleable.CircleImageView_shadow_color_star, mShadowColorStart);
            mShadowColorMiddle = a.getColor(R.styleable.CircleImageView_shadow_color_star, mShadowColorMiddle);
            mShadowColorEnd = a.getColor(R.styleable.CircleImageView_shadow_color_star, mShadowColorEnd);

            a.recycle();
        }catch (Exception e){

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (SHAPE_NONE == mDrawShapeType) {
            super.onDraw(canvas);
            return;
        }

        try {
            final Drawable localDrawable = getDrawable();
            if (localDrawable == null)
                return;
            if (localDrawable instanceof NinePatchDrawable) {
                return;
            }
            if (this.paint == null) {
                final Paint localPaint = new Paint();
                localPaint.setFilterBitmap(false);
                localPaint.setAntiAlias(true);
                localPaint.setXfermode(MASK_XFERMODE);
                this.paint = localPaint;
            }
            final int width = getWidth();
            final int height = getHeight();
            /** 保存layer */
            int layer = canvas.saveLayer(0.0F, 0.0F, width, height, null, Canvas.ALL_SAVE_FLAG);
            /** 设置drawable的大小 */
            localDrawable.setBounds(0, 0, width, height);
            /** 将drawable绑定到bitmap(this.mask)上面（drawable只能通过bitmap显示出来） */
            localDrawable.draw(canvas);
            if ((this.mask == null) || (this.mask.isRecycled())) {
                this.mask = createBitmap(width, height);
            }

            if (mIsDrawShadow) {
                /** 绘制阴影 */
                drawShadow(canvas, width, height);
            }

            /** 将bitmap画到canvas上面 */
            canvas.drawBitmap(this.mask, 0.0F, 0.0F, this.paint);
            /** 将画布复制到layer上 */
            canvas.restoreToCount(layer);


            if (mIsDrawBoarder) {
                /** 绘制描边 */
                drawBorder(canvas, width, height);
            }
        }catch (Exception e){
            super.onDraw(canvas);
        }
    }

    private void drawShadow(Canvas canvas, final int width, final int height){
        RadialGradient gradient = new RadialGradient(
                width/2,height/2,width/2,
                new int[]{mShadowColorStart,mShadowColorMiddle,mShadowColorEnd},
                new float[]{0.f,0.8f,1.0f}, Shader.TileMode.CLAMP);

        paint.setShader(gradient);
        canvas.drawCircle(width/2,height/2,width/2,paint);
    }

    /**
     * 绘制最外面的边框
     *
     * @param canvas
     * @param width
     * @param height
     */
    private void drawBorder(Canvas canvas, final int width, final int height) {
        if (mBorderWidth == 0) {
            return;
        }
        final Paint mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        if(SHAPE_CIRCLE == mDrawShapeType){
            /**
             * 坐标x：view宽度的一般 坐标y：view高度的一般 半径r：因为是view的宽度-border的一半
             */
            canvas.drawCircle(width >> 1, height >> 1, (width - mBorderWidth) >> 1, mBorderPaint);
        }else if(SHAPE_RECT == mDrawShapeType){
            RectF rectF = new RectF(0,0,width,height);
            canvas.drawRoundRect(rectF, mRectRound, mRectRound, mBorderPaint);
        }


        canvas = null;
    }

    /**
     * 获取一个bitmap，目的是用来承载drawable;
     * <p>
     * 将这个bitmap放在canvas上面承载，并在其上面画一个椭圆(其实也是一个圆，因为width=height)来固定显示区域
     *
     * @param width
     * @param height
     * @return
     */
    public Bitmap createBitmap(final int width, final int height) {
        Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
        Bitmap localBitmap = Bitmap.createBitmap(width, height, localConfig);
        Canvas localCanvas = new Canvas(localBitmap);
        Paint localPaint = new Paint();
        final int padding = mBorderWidth - 3;
        if(SHAPE_CIRCLE == mDrawShapeType){
            /**
             * 设置椭圆的大小(因为椭圆的最外边会和border的最外边重合的，如果图片最外边的颜色很深，有看出有棱边的效果，所以为了让体验更加好，
             * 让其缩进padding px)
             */
            RectF localRectF = new RectF(padding, padding, width - padding, height - padding);
            localCanvas.drawOval(localRectF, localPaint);
        }else if(SHAPE_RECT == mDrawShapeType){
            RectF localRectF = new RectF(padding, padding, width - padding, height - padding);
            localCanvas.drawRoundRect(localRectF, mRectRound, mRectRound, localPaint);
        }

        return localBitmap;
    }

    public void setmDrawShapeType(int mDrawShapeType) {
        this.mDrawShapeType = mDrawShapeType;
    }

    public void setmIsDrawBoarder(boolean mIsDrawBoarder) {
        this.mIsDrawBoarder = mIsDrawBoarder;
    }

    public void setmIsDrawShadow(boolean mIsDrawShadow) {
        this.mIsDrawShadow = mIsDrawShadow;
    }
}
