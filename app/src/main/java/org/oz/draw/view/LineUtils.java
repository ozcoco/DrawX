package org.oz.draw.view;

import android.util.Log;

/**
 * @ProjectName: DrawX
 * @Package: org.oz.draw.view
 * @ClassName: LineUtils
 * @Description:
 * @Author: oz
 * @CreateDate: 2020/7/22 16:05
 * @UpdateUser:
 * @UpdateDate: 2020/7/22 16:05
 * @UpdateRemark:
 * @Version: 1.0
 */
public class LineUtils {


    public static class QuadBezier {

        private int max = 1080;

        private float x0, y0;
        private float x1, y1;
        private float x2, y2;

        //
        private float _x02, _y02;

        public QuadBezier(float x0, float y0, float x1, float y1, float x2, float y2) {
            this.x0 = x0;
            this.y0 = y0;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        //p01
        private float p01x(float t) {
            return (1 - t) * x0 + t * x1;
        }

        private float p01y(float t) {
            return (1 - t) * y0 + t * y1;
        }

        //p11
        private float p11x(float t) {
            return (1 - t) * x1 + t * x2;
        }

        private float p11y(float t) {
            return (1 - t) * y1 + t * y2;
        }

        //p02 ---->x
        private float p02x(float t) {
            return (1 - t) * p01x(t) + t * p11x(t);
        }

        // ---->y
        private float p02y(float t) {
            return (1 - t) * p01y(t) + t * p11y(t);
        }


        private float x(float t) {
            return p02x(t);
        }

        private float y(float t) {
            return p02y(t);
        }


        public float[] p(float t) {

            return new float[]{x(t), y(t)};
        }


        private float bX() {
            return x1 - 2 * x0;

        }

        private float bY() {
            return y1 - 2 * y0;
        }

        private float aX() {
            return x0 - 2 * x1 + x2;
        }

        private float aY() {
            return y0 - 2 * y1 + y2;
        }

        private float cX() {
            return x0 - _x02;
        }

        private float cY() {
            return y0 - _y02;
        }

        private double deltaX() {
            return Math.pow(bX(), 2) - 4 * aX() * cX();
        }

        private double deltaY() {
            return Math.pow(bY(), 2) - 4 * aY() * cY();
        }


        private float x2t1(float x) {

            _x02 = x;

            return (float) ((-bX() + Math.pow(deltaX(), 0.5)) / (2 * aX()));
        }

        private float x2t2(float x) {

            _x02 = x;

            return (float) ((-bX() - Math.pow(deltaX(), 0.5)) / (2 * aX()));
        }

        private float y2t1(float y) {

            _y02 = y;

            return (float) ((-bY() + Math.pow(deltaY(), 0.5)) / (2 * aY()));
        }

        private float y2t2(float y) {

            _y02 = y;

            return (float) ((-bY() - Math.pow(deltaY(), 0.5)) / (2 * aY()));
        }

        private float[] x2t(float x) {

            return new float[]{x2t1(x), x2t2(x)};
        }


        private float[] y2t(float y) {

            return new float[]{y2t1(y), y2t2(y)};
        }


        public float y2x(float y) {

            float t;

            float[] ts = y2t(y);

            if (ts[0] >= 0 && ts[0] <= 1) {
                t = ts[0];
            } else {

                t = ts[1];
            }

            return x(t);

        }

        public float x2y(float x) {

            float t;

            float[] ts = x2t(x);

            Log.d("QuadBezier", "------ts-->(" + ts[0] + "," + ts[1] + ")");

            if (ts[0] >= 0 && ts[0] <= 1) {
                t = ts[0];
            } else {

                t = ts[1];
            }

            return y(t);
        }


        public float[] xToy(float x) {

            float[] ts = ot(x0, x1, x2, x);

            if (ts.length > 1) {

                return new float[]{y(ts[0]), y(ts[1])};

            } else {

                return new float[]{y(ts[0])};
            }

        }


        private float[] ot(float z0, float z1, float z2, float zp) {
            float a = z0 - z1 * 2 + z2,
                    b = 2 * (z1 - z0),
                    c = z0 - zp;
            float tt = 0;
            if (a == 0 && b != 0) {
                tt = -c / b;
            } else {
                float sq = (float) Math.sqrt(b * b - 4 * a * c);
                float tt1 = (sq - b) / (2 * a),
                        tt2 = (-sq - b) / (2 * a);

                if ((tt1 <= 1 && tt1 >= 0) && (tt2 <= 1 && tt2 >= 0)) {
                    return new float[]{tt1, tt2};
                } else if (tt1 <= 1 && tt1 >= 0) {
                    tt = tt1;
                } else {
                    tt = tt2;
                }
            }

            return new float[]{tt};
        }


        public float findXByY(float y) {

            final int len = max;

            float gap = 1 / (float) len;

            float t = 0;

            for (int i = 0; i < len; i++) {

                t += gap;

                float[] p = p(t);

                Log.d("QuadBezier", "----QuadBezier--->t = " + t);
                Log.d("QuadBezier", "----QuadBezier--->len = " + len);
                Log.d("QuadBezier", "----QuadBezier--->x = " + y);
                Log.d("QuadBezier", "----QuadBezier--->p(" + p[0] + "," + p[1] + ")");

                if (((int) p[1]) == y) {

                    return p[0];
                }
            }


            return 0;
        }

        public float findYByX(float x) {

            final int len = max;

            float gap = 1 / (float) len;

            float t = 0;

            for (int i = 0; i < len; i++) {

                t += gap;

                float[] p = p(t);

                Log.d("QuadBezier", "----QuadBezier--->t = " + t);
                Log.d("QuadBezier", "----QuadBezier--->len = " + len);
                Log.d("QuadBezier", "----QuadBezier--->x = " + x);
                Log.d("QuadBezier", "----QuadBezier--->p(" + p[0] + "," + p[1] + ")");

                if (((int) p[0]) == x) {

                    return p[1];
                }
            }

            return 0;
        }

        public void setMax(int max) {
            this.max = max;
        }
    }


    public static class CubicBezier {

        private int max = 1080;

        private double t; //t[0,1]

        private float x0, y0;
        private float x1, y1;
        private float x2, y2;
        private float x3, y3;

        public CubicBezier(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3) {
            this.x0 = x0;
            this.y0 = y0;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.x3 = x3;
            this.y3 = y3;
        }

        //p01
        private double p01x(double t) {
            return (1 - t) * x0 + t * x1;
        }

        private double p01y(double t) {
            return (1 - t) * y0 + t * y1;
        }

        //p11
        private double p11x(double t) {
            return (1 - t) * x1 + t * x2;
        }

        private double p11y(double t) {
            return (1 - t) * y1 + t * y2;
        }

        //p21
        private double p21x(double t) {
            return (1 - t) * x2 + t * x3;
        }

        private double p21y(double t) {
            return (1 - t) * y2 + t * y3;
        }

        //p02
        private double p02x(double t) {
            return (1 - t) * p01x(t) + t * p11x(t);
        }

        private double p02y(double t) {
            return (1 - t) * p01y(t) + t * p11y(t);
        }

        //p12
        private double p12x(double t) {
            return (1 - t) * p11x(t) + t * p21x(t);
        }

        private double p12y(double t) {
            return (1 - t) * p11y(t) + t * p21y(t);
        }

        //p03
        //p03x---> x
        private double p03x(double t) {
            return (1 - t) * p02x(t) + t * p12x(t);
        }

        //p03y---> y
        private double p03y(double t) {
            return (1 - t) * p02y(t) + t * p12y(t);
        }

        //t
        @Deprecated
        private double tByP03X(float p03x) {

//            return (p03x - );
            return 0;
        }

        @Deprecated
        private double tByP03Y(float p03y) {


            return 0f;

        }

        public float y(float x) {

            final int len = max;

            double gap = 1 / (double) len;

            double t = 0;

            for (int i = 0; i < len; i++) {

                t += gap;

                float[] p = p(t);

                Log.d("CubicBezier", "----CubicBezier--->t = " + t);
                Log.d("CubicBezier", "----CubicBezier--->len = " + len);
                Log.d("CubicBezier", "----CubicBezier--->x = " + x);
                Log.d("CubicBezier", "----CubicBezier--->p(" + p[0] + "," + p[1] + ")");

                if (((int) p[0]) == x) {

                    return p[1];
                }
            }

            return 0f;
        }


        public float x(float y) {

            final int len = max;

            double gap = 1 / (double) len;

            float t = 0;

            for (int i = 0; i < len; i++) {

                t += gap;

                float[] p = p(t);

                if (p[1] == y) {

                    return p[0];
                }
            }

            return 0f;
        }


        public float[] p(double t) {

            return new float[]{(float) p03x(t), (float) p03y(t)};
        }


        public void setMax(int max) {
            this.max = max;
        }
    }


}
