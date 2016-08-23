package cn.xmrk.jbook.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import cn.xmrk.jbook.R;

public class CircleProgressbar extends View {
    private Paint paint = new Paint();
    private RectF oval = new RectF();
    //属性
    private String textFormat;
    private boolean textVisibility;
    private int textSize;
    private int radius;//内圆半径
    private int border;
    private int normalBorderColor;
    private int finishedBorderColor;
    //运行值
    private int percent = 0;//当前进度的百分比，整数形式

    public CircleProgressbar(Context context) {
        super(context);
    }

    public CircleProgressbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.circleprogressbar);
        textFormat = a.getString(R.styleable.circleprogressbar_textFormat);
        textFormat = textFormat == null ? "#%" : textFormat.trim();
        textVisibility = a.getBoolean(R.styleable.circleprogressbar_textVisibility, true);
        textSize = (int) a.getDimension(R.styleable.circleprogressbar_textSize, sp2px(this.getContext(), 20));

        normalBorderColor = (int) a.getColor(R.styleable.circleprogressbar_normalBorderColor, 0);
        if (normalBorderColor == 0)
            normalBorderColor = a.getResourceId(R.styleable.circleprogressbar_normalBorderColor, 0);
        if (normalBorderColor == 0)
            normalBorderColor = 0xFFCE3031;

        finishedBorderColor = (int) a.getColor(R.styleable.circleprogressbar_finishedBorderColor, 0);
        if (finishedBorderColor == 0)
            finishedBorderColor = a.getResourceId(R.styleable.circleprogressbar_finishedBorderColor, 0);
        if (finishedBorderColor == 0)
            finishedBorderColor = 0xFFC6E3FF;

        radius = (int) a.getDimension(R.styleable.circleprogressbar_cicleRadius, sp2px(this.getContext(), 50));
        border = (int) a.getDimension(R.styleable.circleprogressbar_cicleborder, sp2px(this.getContext(), 10));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    /**
     * 计算组件宽度
     */
    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = getDefaultWidth();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 计算组件高度
     */
    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = getDefaultHeight();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 计算默认宽度
     */
    private int getDefaultWidth() {
        return this.radius * 2 + this.border * 2;
    }

    /**
     * 计算默认宽度
     */
    private int getDefaultHeight() {
        return this.radius * 2 + this.border * 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
        drawText(canvas);
    }

    /**
     * 画圆
     */
    private void drawCircle(Canvas canvas) {
        int angle = (int) (360 * (this.percent / 100.0));//完成的弧度
        //画未完成的
        paint.setColor(this.normalBorderColor);
        paint.setAntiAlias(true);//定义画笔无锯齿
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) this.border);
        int diameter = this.radius * 2;
        oval.left = this.border;
        oval.top = this.border;
        oval.right = oval.left + diameter;
        oval.bottom = oval.top + diameter;
        canvas.drawArc(oval, 0, 360 - angle, false, paint);
        //画完成的
        paint.setColor(this.finishedBorderColor);
        canvas.drawArc(oval, 360 - angle, angle, false, paint);
    }

    /**
     * 画文字
     */
    private void drawText(Canvas canvas) {
        if (!this.textVisibility)
            return;
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(this.textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        if (this.percent == 100)
            paint.setColor(this.finishedBorderColor);
        else
            paint.setColor(this.normalBorderColor);

        FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int txtHeight = fontMetrics.bottom - fontMetrics.ascent;
        String text = this.percent + "";
        text = this.textFormat.replace("#", text);
        int txtWidth = (int) paint.measureText(text);
        canvas.drawText(text, this.getWidth() / 2, this.getHeight() / 2 - txtHeight / 2 - fontMetrics.ascent, paint);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 从 sp 的单位 转成为 px(像素)
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public String getTextFormat() {
        return textFormat;
    }

    public void setTextFormat(String textFormat) {
        this.textFormat = textFormat;
    }

    public boolean isTextVisibility() {
        return textVisibility;
    }

    public void setTextVisibility(boolean textVisibility) {
        this.textVisibility = textVisibility;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public int getNormalBorderColor() {
        return normalBorderColor;
    }

    public void setNormalBorderColor(int normalBorderColor) {
        this.normalBorderColor = normalBorderColor;
    }

    public int getFinishedBorderColor() {
        return finishedBorderColor;
    }

    public void setFinishedBorderColor(int finishedBorderColor) {
        this.finishedBorderColor = finishedBorderColor;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
        this.invalidate();
    }
}
