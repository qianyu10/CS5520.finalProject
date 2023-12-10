package com.project.xiangmu.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 文件名：CountdownView
 * 作  者：
 * 日  期：1/21/22 11:27 AM
 * 描述：启动页倒计时按钮
 */
public class CountdownView extends AppCompatTextView {

    private static int MAX_TIME = 5;            //最大倒计时时间
    private String CONCAT_STR = "s跳过";         //默认倒计时后面显示的文字
    private int updateTime = 0;                 //当前倒计时时间
    private int bgColor = Color.GRAY;           //背景颜色
    private int bgCorner = 10;                  //有设置画的背景时默认圆角10dp
    private BgStyle bgStyle = BgStyle.CLEAR;    //背景样式
    private Paint mPaint;                       //绘制背景的画笔
    private int vW, vH;                         //此view的宽\高
    private ValueAnimator valueAnimator;        //动画，通过动画的进度来控制view的文字绘制
    private CountdownEndListener endListener;   //倒计时结束

    //背景样式
    public enum BgStyle {
        LINE,   //一圈线
        FILL,   //填充背景
        CLEAR   //没有背景  可以自己设置background
    }

    /**
     * 设置倒计时结束监听
     *
     * @param endListener .
     * @return thisView
     */
    public CountdownView setEndListener(CountdownEndListener endListener) {
        this.endListener = endListener;
        return this;
    }

    /**
     * 设置倒计时时间 (单位s)
     *
     * @param maxTime 最大时间 default 5 ms
     * @return thisView
     */
    public CountdownView setMaxTime(int maxTime) {
        MAX_TIME = maxTime;
        return this;
    }

    /**
     * 设置倒计时后面拼接的字符串
     *
     * @param concatStr eg:"s跳过"
     * @return thisView
     */
    public CountdownView setConcatStr(String concatStr) {
        this.CONCAT_STR = concatStr;
        return this;
    }

    /**
     * 设置背景样式
     *
     * @param bgStyle 背景样式 默认无
     * @return thisView
     */
    public CountdownView setBgStyle(BgStyle bgStyle) {
        this.bgStyle = bgStyle;
        invalidate();
        return this;
    }

    /**
     * 设置背景圆角大小 （单位dp）
     *
     * @param bgCorner 圆角大小
     * @return thisView
     */
    public CountdownView setBgCorner(int bgCorner) {
        this.bgCorner = bgCorner;
        invalidate();
        return this;
    }

    /**
     * 存在背景时，背景的颜色
     *
     * @param bgColor 颜色值
     * @return thisView
     */
    public CountdownView setBgColor(int bgColor) {
        this.bgColor = bgColor;
        invalidate();
        return this;
    }

    public CountdownView(Context context) {
        this(context, null);
    }

    public CountdownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountdownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        vW = w;
        vH = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        drawBgShape(canvas);

        //textView显示文字
        super.onDraw(canvas);
    }

    @SuppressLint("NewApi")
    private void drawBgShape(Canvas canvas) {
        switch (bgStyle) {
            case LINE:
                //画线模式
                mPaint.setStyle(Paint.Style.STROKE);
                break;
            case FILL:
                //填充模式
                mPaint.setStyle(Paint.Style.FILL);
                break;
            case CLEAR:
                //不画背景
                return;
            default:
                return;
        }
        //清除原背景
        setBackgroundResource(0);
        //画背景
        mPaint.setColor(bgColor);

        int minW = Math.min(vW, vH);
        //圆角大小
        if (bgCorner > (minW >> 1))
            bgCorner = (minW >> 1);
        //圆角小于0的情况不存在 强制为0
        if (bgCorner < 0)
            bgCorner = 0;

        canvas.save();
        canvas.drawRoundRect(0, 0, vW, vH,
                dp2Px(getContext(), bgCorner), dp2Px(getContext(), bgCorner), mPaint);
        canvas.restore();
    }

    /**
     * 获取当前应该显示的文字内容
     *
     * @return .
     */
    private String getShouldShowText() {
        if (updateTime != 0)
            return String.valueOf(updateTime).concat(CONCAT_STR);
        else
            return CONCAT_STR.substring(1);
    }

    /**
     * 倒计时开始
     */
    public synchronized void timeStart() {
        startAnimation();
    }

    /**
     * 倒计时结束；或者手动调用此方法结束倒计时动画
     */
    public synchronized void timeEnd() {
        if (valueAnimator != null) {
            //停止动画，清空动画资源
            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.removeAllUpdateListeners();
            valueAnimator = null;
        }
    }

    private void startAnimation() {
        if (null == valueAnimator) {
            valueAnimator = ValueAnimator.ofInt(MAX_TIME, 0);
            //动画进度监听
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    //当前进度值
                    int value = (int) animation.getAnimatedValue();
                    if (updateTime != value) {
                        //重置倒计时时间，
                        updateTime = value;

                        //重新绘制view显示文字
                        setText(getShouldShowText());

                        //倒计时结束的回调
                        if (updateTime == 0 && null != endListener)
                            endListener.countdownEnd();
                    }
                }
            });
            //设置匀速差值器
            valueAnimator.setInterpolator(new LinearInterpolator());
        }
        if (valueAnimator.isRunning())
            return;
        //设置动画执行时间
        valueAnimator.setDuration(MAX_TIME * 1000);

        valueAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        //销毁时，清空动画
        timeEnd();
        super.onDetachedFromWindow();
    }

    private static int dp2Px(Context context, float dpValue) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * density);
    }

    /**
     * 倒计时结束监听
     */
    public interface CountdownEndListener {
        //倒计时结束
        void countdownEnd();
    }

}
