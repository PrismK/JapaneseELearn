package com.prismk.japaneseelearn.widgets.elearncamera.util;

public class AngleUtil {
    public static int nowAngle = 0;

    public static int getSensorAngle(float x, float y) {
        if (Math.abs(x) > Math.abs(y)) {
            /**
             * 横屏倾斜角度比较大
             */
            if (x > 4) {
                /**
                 * 左边倾斜
                 */
                return 270;
            } else if (x < -4) {
                /**
                 * 右边倾斜
                 */
                return 90;
            } else {
                /**
                 * 倾斜角度不够大
                 */
                return 0;
            }
        } else {
            if (y > 7) {
                /**
                 * 左边倾斜
                 */
                return 0;
            } else if (y < -7) {
                /**
                 * 右边倾斜
                 */
                return 180;
            } else {
                /**
                 * 倾斜角度不够大
                 */
                return 0;
            }
        }
    }

    public static int getSensorAngleSlowly(float x, float y) {
        if (y > 8) {
            nowAngle = 0;
            return 0;
        }
        if (x < -8) {
            nowAngle = 90;
            return 90;
        }
        if (y < -8) {
            nowAngle = 180;
            return 180;
        }
        if (x > 8) {
            nowAngle = 270;
            return 270;
        }
        return nowAngle;
    }
}
