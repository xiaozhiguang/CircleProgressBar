package com.app.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xiaozhiguang on 2018/3/2.
 */

public class CircleProgressBar extends View {

    private int statue;                             //  控件状态

    public static final int DOWNLOAD_DEF    = 0;    //  默认状态
    public static final int DOWNLOAD_PAUSE  = 1;    //  暂停状态
    public static final int DOWNLOAD_STATUE = 2;    //  下载状态
    public static final int DOWNLOAD_FINISH = 3;    //  完成状态

    private int width;                              // 控件的宽度
    private int height;                             // 控件的高度

    private int radius;                             // 圆形的半径
    private int progressWidth = dp2px(2);           // 圆环进度条的宽度

    private int progress = 0;                       // 进度百分比0~100;

    private Bitmap pauseBitmap;                     //  暂停时候显示的样子
    private Bitmap finishBitmap;                    //  完成时候显示的样子
    private Bitmap defaultBitmap;                   //  默认时候显示的样子
    private Bitmap downloadBitmap;                  //  下载时候显示的样子

    private RectF rectf = new RectF();
    private Paint paint = new Paint();

    @Deprecated
    float scale = 0.35f;                            // 中间背景图片相对圆环的大小的比例

    private float paddingScale = 1f;                // 控件内偏距占空间本身的比例

    private int startAngle = 270;                   //  开始的角度 0 代表在最右边 90 最下方 按照然后顺时针旋转

    private int preColor;                           // 进度条未完成的颜色
    private int circleColor;                        // 圆中间的背景颜色
    private int progressColor;                      // 进度条颜色

    private onProgressListener onProgressListener;  // 进度时间监听

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);

        pauseBitmap = getPauseBitmap();
        finishBitmap = getFinishBitmap();
        defaultBitmap = getDefaultBitmap();
        downloadBitmap = getDownloadBitmap();

        circleColor = Color.parseColor("#FFFFFF");
        statue = typedArray.getInteger(R.styleable.CircleProgressBar_statue, 0);
        preColor = typedArray.getColor(R.styleable.CircleProgressBar_cpb_default_progress_color, Color.parseColor("#ACACAC"));
        progressColor = typedArray.getColor(R.styleable.CircleProgressBar_cpb_download_progress_color, Color.parseColor("#F45189"));

        typedArray.recycle();
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int reWidth;
        int reHeight;
        if (widthMode == MeasureSpec.EXACTLY) {
            reWidth = widthSize;
        } else {
            int desired = getPaddingLeft() + width + getPaddingRight();
            reWidth = desired;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            reHeight = heightSize;
        } else {
            int desired = getPaddingTop() + height + getPaddingBottom();
            reHeight = desired;
        }

        width = reWidth;
        height = reHeight;

        setMeasuredDimension(reWidth, reHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (statue) {
            case DOWNLOAD_DEF:
                drawDefault(canvas);
                break;
            case DOWNLOAD_STATUE:
                drawProgress(canvas);
                break;
            case DOWNLOAD_PAUSE:
                drawPause(canvas);
                break;
            case DOWNLOAD_FINISH:
                drawDownloadFinish(canvas);
        }
        super.onDraw(canvas);
    }

    /**
     * 设置bitmap的宽高
     *
     * @param bm
     * @param newWidth
     * @param newHeight
     * @return
     */
    private Bitmap setBitmapSize(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高.
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        Bitmap newBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newBitmap;
    }

    /**
     * 获取暂停时的bitmap资源
     *
     * @return
     */
    private Bitmap getPauseBitmap() {
        if (pauseBitmap != null) {
            return pauseBitmap;
        } else {
            return BitmapFactory.decodeResource(getResources(), R.drawable.icon_download_pause);
        }
    }

    /**
     * 设置暂停时显示的图片
     *
     * @return
     */
    public void setPauseBitmap(int pauseImage) {
        this.pauseBitmap = BitmapFactory.decodeResource(getResources(), pauseImage);
    }

    /**
     * 获取完成时的bitmap资源
     *
     * @return
     */
    private Bitmap getFinishBitmap() {
        if (finishBitmap != null) {
            return finishBitmap;
        } else {
            return BitmapFactory.decodeResource(getResources(), R.drawable.icon_music_download);
        }
    }

    /**
     * 设置完成时显示的图片
     *
     * @return
     */
    public void setFinishBitmap(int finishImage) {
        this.finishBitmap = BitmapFactory.decodeResource(getResources(), finishImage);
    }

    /**
     * 获取默认时的bitmap资源
     *
     * @return
     */
    private Bitmap getDefaultBitmap() {
        if (defaultBitmap != null) {
            return defaultBitmap;
        } else {
            return BitmapFactory.decodeResource(getResources(), R.drawable.icon_music_download);
        }
    }

    /**
     * 设置默认时显示的图片
     *
     * @return
     */
    public void setDefaultBitmap(int defaultImage) {
        this.defaultBitmap = BitmapFactory.decodeResource(getResources(), defaultImage);
    }

    /**
     * 获取下载时的bitmap资源
     *
     * @return
     */
    private Bitmap getDownloadBitmap() {
        if (downloadBitmap != null) {
            return downloadBitmap;
        } else {
            return BitmapFactory.decodeResource(getResources(), R.drawable.icon_download_start);
        }
    }

    /**
     * 设置下载时显示的图片
     *
     * @return
     */
    public void setDownloadBitmap(int downloadImage) {
        this.downloadBitmap = BitmapFactory.decodeResource(getResources(), downloadImage);
    }

    /**
     * 绘制默认状态
     *
     * @param canvas
     */
    private void drawDefault(Canvas canvas) {
        //  图片缩小显示
        int scales = 5;
        Rect srcRect = new Rect(0, 0, width, height);
        Rect dstRect = new Rect(width / scales, height / scales, width / scales, height / scales);
        canvas.drawBitmap(setBitmapSize(defaultBitmap, width, height), 0, 0, paint);
        canvas.drawBitmap(setBitmapSize(defaultBitmap, width, height), srcRect, dstRect, paint);
    }

    /**
     * 绘制暂停时的状态
     *
     * @param canvas
     */
    private void drawPause(Canvas canvas) {
        int size = height;
        if (height > width)
            size = width;
        radius = (int) (size * paddingScale / 2f);
        paint.setAntiAlias(true);
        paint.setColor(progressColor);
        // canvas.drawCircle(width / 2, height / 2, radius + 2, paint);//画最外面的边线
        paint.setColor(preColor);
        // 绘制最大的圆 进度条圆环的背景颜色（未走到的进度）就是这个哦
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        rectf.set((width - radius * 2) / 2f, (height - radius * 2) / 2f,
                ((width - radius * 2) / 2f) + (2 * radius),
                ((height - radius * 2) / 2f) + (2 * radius));
        paint.setColor(progressColor);
        canvas.drawArc(rectf, startAngle, progress * 3.6f, true, paint);
        paint.setColor(circleColor);
        // 绘制用于遮住伞形两个边的小圆
        canvas.drawCircle(width / 2, height / 2, radius - progressWidth, paint);
        if (pauseBitmap != null) {       // 绘制中间的图片
            int width2 = (int) (rectf.width() * scale);
            int height2 = (int) (rectf.height() * scale);
            rectf.set(rectf.left + width2, rectf.top + height2, rectf.right
                    - width2, rectf.bottom - height2);
            canvas.drawBitmap(pauseBitmap, null, rectf, null);
        }
    }

    /**
     * 绘制正在下载时的状态
     *
     * @param canvas
     */
    private void drawProgress(Canvas canvas) {
        int size = height;
        if (height > width)
            size = width;
        radius = (int) (size * paddingScale / 2f);
        paint.setAntiAlias(true);
        paint.setColor(progressColor);
        // canvas.drawCircle(width / 2, height / 2, radius + 2, paint);//画最外面的边线
        paint.setColor(preColor);
        // 绘制最大的圆 进度条圆环的背景颜色（未走到的进度）
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        rectf.set((width - radius * 2) / 2f, (height - radius * 2) / 2f,
                ((width - radius * 2) / 2f) + (2 * radius),
                ((height - radius * 2) / 2f) + (2 * radius));
        paint.setColor(progressColor);
        canvas.drawArc(rectf, startAngle, progress * 3.6f, true, paint);
        paint.setColor(circleColor);
        // 绘制用于遮住伞形两个边的小圆
        canvas.drawCircle(width / 2, height / 2, radius - progressWidth, paint);
        if (downloadBitmap != null) {       // 绘制中间的图片
            int width2 = (int) (rectf.width() * scale);
            int height2 = (int) (rectf.height() * scale);
            rectf.set(rectf.left + width2, rectf.top + height2, rectf.right
                    - width2, rectf.bottom - height2);
            canvas.drawBitmap(downloadBitmap, null, rectf, null);
        }
    }

    /**
     * 绘制下载完成的状态
     *
     * @param canvas
     */
    private void drawDownloadFinish(Canvas canvas) {
        //  图片缩小显示
        int scales = 5;
        Rect srcRect = new Rect(0, 0, width, height);
        Rect dstRect = new Rect(width / scales, height / scales, width / scales, height / scales);
        canvas.drawBitmap(setBitmapSize(finishBitmap, width, height), 0, 0, paint);
        canvas.drawBitmap(setBitmapSize(finishBitmap, width, height), srcRect, dstRect, paint);
    }

    /**
     * dp转px
     *
     * @param dp dp值
     * @return
     */
    public int dp2px(int dp) {
        return (int) ((getResources().getDisplayMetrics().density * dp) + 0.5);
    }

    /**
     * 设置进度
     *
     * @param progress 进度百分比
     */
    public void setProgress(int progress) {
        if (progress > 100)
            return;
        this.progress = progress;
        invalidate();
        if (onProgressListener != null)
            onProgressListener.onProgress(progress);
    }

    /**
     * 设置按钮状态
     *
     * @param statue 状态
     */
    public void setStatue(int statue) {
        this.statue = statue;
        invalidate();
    }

    /**
     * 获取按钮状态
     */
    public int getStatue() {
        return this.statue;
    }

    /**
     * 设置圆环进度条的宽度
     *
     * @param widthDp 宽度 dp值
     * @return
     */
    public CircleProgressBar setProgressWidth(int widthDp) {
        this.progressWidth = dp2px(widthDp);
        return this;
    }

    /**
     * 设置圆心中间的背景颜色
     *
     * @param color
     * @return
     */
    public CircleProgressBar setCircleBackgroud(int color) {
        this.circleColor = color;
        return this;
    }

    /**
     * 设置圆相对整个控件的宽度或者高度的占用比例
     *
     * @param scale
     * @return
     */
    public CircleProgressBar setPaddingscale(float scale) {
        this.paddingScale = scale;
        return this;
    }

    /**
     * 设置开始的位置
     * 0 代表在最右边(90°方向) 90 最下方 按照然后顺时针旋转
     *
     * @param startAngle 0~360
     * @return
     */
    public CircleProgressBar setStartAngle(int startAngle) {
        this.startAngle = startAngle;
        return this;
    }

    public interface onProgressListener {
        void onProgress(int value);
    }
}