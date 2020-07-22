package org.oz.draw.view;

/**
 * @ProjectName: DrawX
 * @Package: org.oz.draw.view
 * @ClassName: PointUtils
 * @Description:
 * @Author: oz
 * @CreateDate: 2020/7/22 16:05
 * @UpdateUser:
 * @UpdateDate: 2020/7/22 16:05
 * @UpdateRemark:
 * @Version: 1.0
 */
public class PointUtils {

    public static boolean isContainsCircle(float cx, float cy, float radius, float x, float y) {

        double d = Math.pow(cx - x, 2) + Math.pow(cy - y, 2);

        return d <= Math.pow(radius, 2);
    }

}
