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

            double t = 0;

            for (int i = 0; i < len; i++) {

                t += (1 / (double) len);

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

            float t = 0;

            for (int i = 0; i < len; i++) {

                t += 0.001;

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
