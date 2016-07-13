package cn.xmrk.jbook.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import cn.xmrk.jbook.R;
import cn.xmrk.jbook.helper.PaintHelper;
import cn.xmrk.rkandroid.utils.CommonUtil;
import cn.xmrk.rkandroid.utils.StringUtil;

/**
 * Created by Au61 on 2016/7/7.
 */
public class DrawView extends View {

    private Bitmap mDrawBitmap;
    private Bitmap fontBitmap;

    private int mHeight;
    private int mWidth;

    /**
     * 写的文字和文字的位置
     **/
    private String drawText;
    private int drawTextPointX;
    private int drawTextPointY;

    private float startX = 0;
    private float startY = 0;
    private long startTime = 0;
    /**
     * 表示为点击事件的时间
     **/
    private final long CLICK_TIME = 100;
    private StaticLayout textLayout;
    private Paint.Align mAlignment;

    private Bitmap lightBitmap;
    private Bitmap dimBitmap;
    private Bitmap upArrow;
    private Bitmap downArrow;
    private Bitmap leftBack;
    private Bitmap rightBack;

    private int drawLightPointX;
    private int drawLightPointY;
    private int drawDimPointX;
    private int drawDimPointY;

    /**
     * 上下流出的距离
     **/
    private int upDownHeight = CommonUtil.dip2px(70);

    /**
     * 左右的宽度
     **/
    private int leftRightHeight = CommonUtil.dip2px(50);

    private CountDownTimer mCountDownTimer;


    private int mode;
    private final int LEFT_TOUCH = 1;
    private final int TEXT_TOUCH = 2;
    private final int RIGHT_TOUCH = 3;

    private boolean isNeedSeekShow;
    private boolean isSeekShow;

    /**
     * 实现消失的效果
     **/
    private Paint alphPaint;

    /**
     * 画文字的paint
     **/
    private TextPaint mPaint;

    /**
     * 主要处理透明度
     **/
    private Paint drawPaint;

    private PaintHelper mPaintHelper;

    public void setIsNeedSeekShow(boolean isNeedSeekShow) {
        this.isNeedSeekShow = isNeedSeekShow;
        this.isSeekShow = isNeedSeekShow;
    }

    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 设置它的位置
     **/
    public void initStaticLayout() {
        textLayout = new StaticLayout(drawText, mPaint, (int) (CommonUtil.getScreenDisplay().getWidth() - CommonUtil.dip2px(20)), Layout.Alignment.ALIGN_NORMAL, 1f, 0, false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景图片
        if (mDrawBitmap != null) {
            canvas.drawBitmap(mDrawBitmap, 0, 0, drawPaint);
        }
        //画前景图片
        if (fontBitmap != null) {
            canvas.drawBitmap(fontBitmap, 0, 0, null);
        }

        if (isNeedSeekShow && isSeekShow) {
            //画左边的seekView
            canvas.drawBitmap(leftBack, 0, 0, alphPaint);
            canvas.drawBitmap(lightBitmap, drawLightPointX - lightBitmap.getWidth() / 2, drawLightPointY - lightBitmap.getHeight() / 2, alphPaint);
            //画上的箭头
            if (drawLightPointY > upDownHeight) {//流出一个srcBitmap的距离
                canvas.drawBitmap(upArrow, drawLightPointX - upArrow.getWidth() / 2, drawLightPointY - upArrow.getHeight() / 2 - lightBitmap.getHeight(), alphPaint);
            }
            //画下面的箭头
            if (drawLightPointY < mHeight - upDownHeight) {//流出一个srcBitmap的距离
                canvas.drawBitmap(downArrow, drawLightPointX - downArrow.getWidth() / 2, drawLightPointY - downArrow.getHeight() / 2 + lightBitmap.getHeight(), alphPaint);
            }

            //画右边的seekView
            canvas.drawBitmap(rightBack, mWidth - rightBack.getWidth(), 0, null);
            canvas.drawBitmap(dimBitmap, drawDimPointX - dimBitmap.getWidth() / 2, drawDimPointY - dimBitmap.getHeight() / 2, alphPaint);
            //画上的箭头
            if (drawDimPointY > upDownHeight) {//流出一个srcBitmap的距离
                canvas.drawBitmap(upArrow, drawDimPointX - upArrow.getWidth() / 2, drawDimPointY - upArrow.getHeight() / 2 - dimBitmap.getHeight(), alphPaint);
            }
            //画下面的箭头
            if (drawDimPointY < mHeight - upDownHeight) {//流出一个srcBitmap的距离
                canvas.drawBitmap(downArrow, drawDimPointX - downArrow.getWidth() / 2, drawDimPointY - downArrow.getHeight() / 2 + dimBitmap.getHeight(), alphPaint);
            }
        }

        //画文字
        if (!StringUtil.isEmptyString(drawText)) {
            canvas.translate(drawTextPointX, drawTextPointY);
            textLayout.draw(canvas);
        }
    //    canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                startX = event.getX();
                startY = event.getY();
                if (isNeedSeekShow) {
                    if (startX < leftRightHeight) {
                        setSeekShowOrDissmiss(true);
                        mode = LEFT_TOUCH;
                    } else if (startX > mWidth - leftRightHeight) {
                        setSeekShowOrDissmiss(true);
                        mode = RIGHT_TOUCH;
                    } else {
                        mode = TEXT_TOUCH;
                    }
                } else {
                    mode = TEXT_TOUCH;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                float endY = event.getY();
                if (mode == LEFT_TOUCH) {//左
                    drawLightPointY += (endY - startY);

                    if (drawLightPointY < upDownHeight) {//边界处理
                        drawLightPointY = upDownHeight;
                    } else if (drawLightPointY > mHeight - upDownHeight) {
                        drawLightPointY = mHeight - upDownHeight;
                    }
                    if (mOnDrawViewClickListener != null) {
                        mOnDrawViewClickListener.onLightSeek(((float) drawLightPointY - upDownHeight) / (mHeight - 2 * upDownHeight));
                    }
                } else if (mode == TEXT_TOUCH) {//文字
                    drawTextPointX += (endX - startX);
                    drawTextPointY += (endY - startY);
                } else {//右
                    drawDimPointY += (endY - startY);

                    if (drawDimPointY < upDownHeight) {//边界处理
                        drawDimPointY = upDownHeight;
                    } else if (drawDimPointY > mHeight - upDownHeight) {
                        drawDimPointY = mHeight - upDownHeight;
                    }
                    if (mOnDrawViewClickListener != null) {
                        mOnDrawViewClickListener.onDimSeek(((float) drawDimPointY - upDownHeight) / (mHeight - 2 * upDownHeight));
                    }
                }
                startX = endX;
                startY = endY;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //此时表示的是点击事件
                if (System.currentTimeMillis() - startTime <= CLICK_TIME) {
                    if (mode == TEXT_TOUCH) {//文字点击
                        if (mOnDrawViewClickListener != null) {
                            mOnDrawViewClickListener.onClick(drawText);
                        }
                    }
                }
                if (mode != TEXT_TOUCH) {
                    setSeekShowOrDissmiss(false);
                }
                invalidate();
                break;
        }
        return true;
    }

    public void setSeekShowOrDissmiss(boolean isShow) {
        if (isShow) {
            alphPaint.setAlpha(255);
            isSeekShow = true;
            invalidate();
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
                mCountDownTimer = null;
            }
        } else {
            if (mCountDownTimer != null) {
                mCountDownTimer.cancel();
                mCountDownTimer = null;
            }
            mCountDownTimer = new CountDownTimer(2000, 100) {
                @Override
                public void onTick(long millisUntilFinished) {
                    alphPaint.setAlpha((int) (((float) millisUntilFinished / 2000) * 255));
                    invalidate();
                }

                @Override
                public void onFinish() {
                    alphPaint.setAlpha(255);
                    isSeekShow = false;
                    invalidate();
                }
            }.start();
        }
    }

    /**
     * 设置初始状态显示
     **/
    public void setDefault() {
        setBrightPaint(127);
        //左右的位置
        drawLightPointX = leftRightHeight / 2;
        drawLightPointY = mHeight / 2;
        drawDimPointX = mWidth - leftRightHeight / 2;
        drawDimPointY = upDownHeight;
        //设置透明度
        alphPaint.setAlpha(255);
        invalidate();
    }

    private void init() {
        mPaintHelper = new PaintHelper(getContext());
        //初始默认的图片的布局大小
        mDrawBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.texture1);
        changeDrawBitmap();
        mWidth = CommonUtil.getScreenDisplay().getWidth();
        mHeight = CommonUtil.getScreenDisplay().getWidth();
        //初始文字和文字的位置
        drawText = "写在这里";
        drawTextPointX = mWidth / 2;
        drawTextPointY = mHeight / 2;

        mAlignment = Paint.Align.CENTER;
        //实例化画笔
        initPaint();
        //实例化文字绘画,初始为居中的状态
        initStaticLayout();
        setBrightPaint(127);

        //上下的箭头
        upArrow = BitmapFactory.decodeResource(getResources(), R.drawable.slider_up_arrow);
        downArrow = BitmapFactory.decodeResource(getResources(), R.drawable.slider_down_arrow);
        lightBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slider_thumb_brightness);
        dimBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slider_thumb_blur);
        //左右的位置
        drawLightPointX = leftRightHeight / 2;
        drawLightPointY = mHeight / 2;

        drawDimPointX = mWidth - leftRightHeight / 2;
        drawDimPointY = upDownHeight;
        //左右的背景
        leftBack = BitmapFactory.decodeResource(getResources(), R.drawable.slider_bg_left);
        rightBack = BitmapFactory.decodeResource(getResources(), R.drawable.slider_bg_right);
        Matrix matrix = new Matrix();
        matrix.setScale(((float) leftRightHeight) / leftBack.getWidth(), ((float) mHeight) / leftBack.getHeight());
        leftBack = Bitmap.createBitmap(leftBack, 0, 0, leftBack.getWidth(), leftBack.getHeight(), matrix, false);
        rightBack = Bitmap.createBitmap(rightBack, 0, 0, rightBack.getWidth(), rightBack.getHeight(), matrix, false);
    }

    public void setAlignment(Paint.Align mAlignment) {
        //这边需要根据上次的模式来更换当前的x坐标值
        this.mAlignment = mAlignment;
        mPaint.setTextAlign(mAlignment);
        //装换之后需要重新设置坐标
        if (this.mAlignment == Paint.Align.LEFT) {
            drawTextPointX = CommonUtil.dip2px(10);
        } else if (this.mAlignment == Paint.Align.CENTER) {
            drawTextPointX = mWidth / 2;
        } else if (this.mAlignment == Paint.Align.RIGHT) {
            drawTextPointX = mWidth - CommonUtil.dip2px(10);
        }
    }

    public String getDrawText() {
        return drawText;
    }

    /**
     * 设定文字和位置
     **/
    public void setDrawText(String drawText) {
        this.drawText = drawText;
        initStaticLayout();
    }


    /**
     * 对要画的图片进行放大缩小操作，让他适配整个屏幕
     **/
    private void changeDrawBitmap() {
        //计算比例
        float fr = ((float) CommonUtil.getScreenDisplay().getWidth()) / mDrawBitmap.getWidth();
        Matrix matrix = new Matrix();
        matrix.setScale(fr, fr);
        mDrawBitmap = Bitmap.createBitmap(mDrawBitmap, 0, 0, mDrawBitmap.getWidth(), mDrawBitmap.getHeight(), matrix, false);
    }

    private void changeFontBitmap(int mColorRes) {
        fontBitmap = Bitmap.createBitmap(CommonUtil.getScreenDisplay().getWidth(), CommonUtil.getScreenDisplay().getWidth(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(fontBitmap);
        canvas.drawColor(getResources().getColor(mColorRes));
    }

    /**
     * 实例化画笔
     **/
    private void initPaint() {
        mPaint = new TextPaint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(mAlignment);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setTextSize(mPaintHelper.getTextSize());
        //默认灰色的的字体颜色
        mPaint.setColor(mPaintHelper.getTextColor());

        alphPaint = new Paint();
        alphPaint.setAlpha(255);

        drawPaint = new Paint();
    }

    public void setBrightPaint(float brightness) {
        brightness = brightness - 127;
        Log.i("brightness", brightness + "");
        ColorMatrix mCm = new ColorMatrix();
        mCm.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness,
                0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});
        drawPaint.setColorFilter(new ColorMatrixColorFilter(mCm));
    }

    public void setPaintColor(int color) {
        mPaint.setColor(color);
        mPaintHelper.saveTextColor(color);
        invalidate();
    }

    public int getPaintColor() {
        return mPaint.getColor();
    }

    public void setTextStyle(Typeface typeface) {
        mPaint.setTypeface(typeface);
        invalidate();
    }

    public void addOrDecreaseTextSize(boolean isAdd) {
        float size = mPaint.getTextSize();
        if (isAdd) {
            size += CommonUtil.dip2px(2);
        } else {
            size -= CommonUtil.dip2px(2);
        }
        if (size > CommonUtil.dip2px(30)) {
            size = CommonUtil.dip2px(30);
        } else if (size < CommonUtil.dip2px(10)) {
            size = CommonUtil.dip2px(10);
        }
        mPaint.setTextSize(size);
        mPaintHelper.saveTextSize(size);
        initStaticLayout();
        invalidate();
    }


    public void setBackBitmap(Bitmap mDrawBitmap, int mColorRes) {
        if (mDrawBitmap != null) {
            this.mDrawBitmap = mDrawBitmap;
        }
        if (mColorRes == 0) {//为0的时候就不需要蒙版了
            fontBitmap = null;
        } else {
            changeFontBitmap(mColorRes);
        }
        changeDrawBitmap();
        invalidate();
    }

    public Bitmap getSrcBitmap() {
        return mDrawBitmap;
    }

    public static interface OnDrawViewClickListener {
        void onClick(String text);

        void onLightSeek(float percent);

        void onDimSeek(float percent);
    }

    private OnDrawViewClickListener mOnDrawViewClickListener;

    public void setOnDrawViewClickListener(OnDrawViewClickListener mOnDrawViewClickListener) {
        this.mOnDrawViewClickListener = mOnDrawViewClickListener;

    }

    public Bitmap getShareBitmap() {
        Bitmap srcBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(srcBitmap);
        //画背景图片
        if (mDrawBitmap != null) {
            canvas.drawBitmap(mDrawBitmap, 0, 0, drawPaint);
        }
        //画前景图片
        if (fontBitmap != null) {
            canvas.drawBitmap(fontBitmap, 0, 0, null);
        }
        //画文字
        if (!StringUtil.isEmptyString(drawText)) {
            canvas.translate(drawTextPointX, drawTextPointY);
            textLayout.draw(canvas);
        }

        return srcBitmap;
    }

}
