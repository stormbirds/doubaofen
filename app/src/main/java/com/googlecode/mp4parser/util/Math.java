package com.googlecode.mp4parser.util;

public class Math {
    public static long gcd(long j, long j2) {
        while (true) {
            long j3 = j;
            j = j2;
            long j4 = j3;
            if (j <= 0) {
                return j4;
            }
            j2 = j4 % j;
        }
    }

    public static int gcd(int i, int i2) {
        while (true) {
            int i3 = i2;
            int i4 = i;
            i = i3;
            if (i <= 0) {
                return i4;
            }
            i2 = i4 % i;
        }
    }

    public static long lcm(long j, long j2) {
        return j * (j2 / gcd(j, j2));
    }

    public static long lcm(long[] jArr) {
        long j = jArr[0];
        for (int i = 1; i < jArr.length; i++) {
            j = lcm(j, jArr[i]);
        }
        return j;
    }

    public static int lcm(int i, int i2) {
        return i * (i2 / gcd(i, i2));
    }
}
