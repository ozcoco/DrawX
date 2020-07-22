package org.oz.draw.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.oz.draw.R;


public class DrawView extends View {

    //
    private int axisColor = Color.WHITE;
    private int lineColor = Color.WHITE;
    private int shapeColor = Color.WHITE;
    private boolean isAxisMove = false;

    ///
    private int bx, by;

    ////
    private TextPaint axisPaint;
    private Paint linePaint;
    private Paint shapePaint;
    private Paint pointColor;

    public DrawView(Context context) {
        super(context);
        init(null, 0);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DrawView, defStyle, 0);

        axisColor = a.getColor(R.styleable.DrawView_axisColor, Color.WHITE);
        lineColor = a.getColor(R.styleable.DrawView_lineColor, Color.WHITE);
        isAxisMove = a.getBoolean(R.styleable.DrawView_isAxisMove, isAxisMove);

        a.recycle();

        initPaint();

    }


    private void initPaint() {

        axisPaint = new TextPaint();
        axisPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        axisPaint.setDither(true);
        axisPaint.setAntiAlias(true);
        axisPaint.setStyle(Paint.Style.STROKE);
        axisPaint.setTextAlign(Paint.Align.LEFT);
        axisPaint.setColor(axisColor);

        linePaint = new Paint();
        linePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        linePaint.setDither(true);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(6);

        shapePaint = new Paint();
        shapePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        shapePaint.setDither(true);
        shapePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        shapePaint.setAntiAlias(true);
        shapePaint.setColor(shapeColor);

        pointColor = new Paint();
        pointColor.setFlags(Paint.ANTI_ALIAS_FLAG);
        pointColor.setDither(true);
        pointColor.setStyle(Paint.Style.FILL_AND_STROKE);
        pointColor.setAntiAlias(true);
        pointColor.setColor(Color.BLACK);
        pointColor.setStrokeWidth(20f);

    }

    private int w, h;
    private int wp, hp;
    private int rawX, rawY;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int mW = getMeasuredWidth();
        int mH = getMeasuredHeight();

        w = mW;
        h = mH;

        wp = w / 2;
        hp = h / 2;

    }


    //
    private final static int SEL_NONE = 0x0;
    private final static int SEL_C1 = 0x1;
    private final static int SEL_C2 = 0x2;

    private int sel = SEL_NONE;
    private int xDown, yDown;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            xDown = (int) event.getX();
            yDown = (int) event.getY();

            if (PointUtils.isContainsCircle(x1, y1, radius, xDown, yDown)) {

                sel = SEL_C1;

                x1 = xDown;
                y1 = yDown;

            } else if (PointUtils.isContainsCircle(x2, y2, radius, xDown, yDown)) {

                sel = SEL_C2;

                x2 = xDown;
                y2 = yDown;

            }

            postInvalidate();

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

            float x, y;
            x = event.getX();
            y = event.getY();

            bx = (int) (x - xDown);
            by = (int) (y - yDown);

            if (sel == SEL_C1) {

                x1 = x;
                y1 = y;

            } else if (sel == SEL_C2) {

                x2 = x;
                y2 = y;

            }

            postInvalidate();

        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            sel = SEL_NONE;
        }

        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawAxis(canvas);
        drawLine(canvas);
        drawShape(canvas);
        drawPoint(canvas);

    }

    /****************************************** draw ******************************************/

    //
    private void drawAxis(Canvas canvas) {

        if (isAxisMove) {
            canvas.drawLine(bx, hp + by, w + bx, hp + by, axisPaint);
            canvas.drawLine(wp + bx, by + bx, wp + bx, h + by, axisPaint);

        } else {

            canvas.drawLine(0, hp, w, hp, axisPaint);
            canvas.drawLine(wp, 0, wp, h, axisPaint);
        }

    }


    ///
    private float x0, y0;
    private float x1 = Float.MIN_VALUE, y1 = Float.MIN_VALUE;
    private float x2 = Float.MIN_VALUE, y2 = Float.MIN_VALUE;
    private float x3, y3;

    private float xn1, yn1;
    private float xn2, yn2;
    private float xn3, yn3;

    private void drawLine(Canvas canvas) {

        x0 = wp + rawX;
        y0 = hp + rawY;

        x3 = w + rawX - 100;
        y3 = rawY + 100;

        if (x1 == Float.MIN_VALUE && y1 == Float.MIN_VALUE) {
            x1 = x0;
            y1 = y0;
        }

        if (x2 == Float.MIN_VALUE && y2 == Float.MIN_VALUE) {
            x2 = x3;
            y2 = y3;
        }

        xn1 = w - x1;
        yn1 = h - y1;

        xn2 = w - x2;
        yn2 = h - y2;

        xn3 = w - x3;
        yn3 = h - y3;


        int rawColor = linePaint.getColor();

        float rawStrokeWidth = linePaint.getStrokeWidth();

        PathEffect rawPathEffect = linePaint.getPathEffect();

        //slash
        linePaint.setPathEffect(new DashPathEffect(new float[]{8, 4}, 0));
        linePaint.setStrokeWidth(2);
        linePaint.setColor(Color.parseColor("#22333333"));
        canvas.drawLine(x3, y3, xn3, yn3, linePaint);

        linePaint.setPathEffect(rawPathEffect);
        linePaint.setStrokeWidth(rawStrokeWidth);
        linePaint.setColor(rawColor);

        Path path = new Path();
        path.moveTo(x0, y0);
        path.cubicTo(x1, y1, x2, y2, x3, y3);
        canvas.drawPath(path, linePaint);

        Path path2 = new Path();
        path2.moveTo(x0, y0);
        path2.cubicTo(xn1, yn1, xn2, yn2, xn3, yn3);
        canvas.drawPath(path2, linePaint);

        //c l
        linePaint.setColor(Color.MAGENTA);
        canvas.drawLine(x0, y0, x1, y1, linePaint);
        canvas.drawLine(x3, y3, x2, y2, linePaint);

        //adjust show
        drawLineAdjust(canvas);

        linePaint.setColor(rawColor);

    }


    private float adx = Float.MIN_VALUE, ady = Float.MIN_VALUE;

    private void drawLineAdjust(Canvas canvas) {

        if (adx == Float.MIN_VALUE && ady == Float.MIN_VALUE) {
            adx = x0;
            ady = y0;
        }

        int rawColor = linePaint.getColor();

        linePaint.setColor(Color.DKGRAY);

        canvas.drawLine(adx, ady, adx, y0, linePaint);

        linePaint.setColor(rawColor);

    }


    ////
    private final static int radius = 30;
    private final static int rawRadius = 15;

    private void drawShape(Canvas canvas) {

        int rawColor = shapePaint.getColor();

        //raw
        shapePaint.setColor(Color.parseColor("#009688"));
        canvas.drawCircle(x0, y0, rawRadius, shapePaint);

        //ad
        shapePaint.setColor(Color.CYAN);
        canvas.drawCircle(adx, ady, rawRadius, shapePaint);

        //c1
        shapePaint.setColor(Color.parseColor("#8BC34A"));
        canvas.drawCircle(x1, y1, radius, shapePaint);

        //c2
        shapePaint.setColor(Color.parseColor("#03A9F4"));
        canvas.drawCircle(x2, y2, radius, shapePaint);

        shapePaint.setColor(rawColor);

    }


    /////
    private void drawPoint(Canvas canvas) {


    }


    /****************************************** property ******************************************/

    public int getAxisColor() {
        return axisColor;
    }

    public void setAxisColor(int axisColor) {
        this.axisColor = axisColor;
    }

    public int getLineColor() {
        return lineColor;
    }


    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public boolean isAxisMove() {
        return isAxisMove;
    }

    public void setAxisMove(boolean axisMove) {
        isAxisMove = axisMove;
    }

    public int getShapeColor() {
        return shapeColor;
    }

    public void setShapeColor(int shapeColor) {
        this.shapeColor = shapeColor;
    }

    public float getAdx() {
        return adx;
    }

    public void setAd(float adx, float ady) {
        this.adx = adx;
        this.ady = ady;
        postInvalidate();
    }

    public float getAdy() {
        return ady;
    }

    public float getX0() {
        return x0;
    }

    public void setX0(float x0) {
        this.x0 = x0;
    }

    public float getY0() {
        return y0;
    }

    public void setY0(float y0) {
        this.y0 = y0;
    }

    public float getX1() {
        return x1;
    }

    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public float getX2() {
        return x2;
    }

    public void setX2(float x2) {
        this.x2 = x2;
    }

    public float getY2() {
        return y2;
    }

    public void setY2(float y2) {
        this.y2 = y2;
    }

    public float getX3() {
        return x3;
    }

    public void setX3(float x3) {
        this.x3 = x3;
    }

    public float getY3() {
        return y3;
    }

    public void setY3(float y3) {
        this.y3 = y3;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public int getWp() {
        return wp;
    }

    public int getHp() {
        return hp;
    }
}


