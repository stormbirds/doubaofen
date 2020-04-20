package com.yhao.floatwindow;

import android.content.Context;
import android.content.Intent;

class Rom {
    Rom() {
    }

    static boolean isIntentAvailable(Intent intent, Context context) {
        return intent != null && context.getPackageManager().queryIntentActivities(intent, 65536).size() > 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0042 A[SYNTHETIC, Splitter:B:15:0x0042] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x004e A[SYNTHETIC, Splitter:B:23:0x004e] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.lang.String getProp(java.lang.String r4) {
        /*
            r0 = 0
            java.lang.Runtime r1 = java.lang.Runtime.getRuntime()     // Catch:{ IOException -> 0x004b, all -> 0x003f }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x004b, all -> 0x003f }
            r2.<init>()     // Catch:{ IOException -> 0x004b, all -> 0x003f }
            java.lang.String r3 = "getprop "
            r2.append(r3)     // Catch:{ IOException -> 0x004b, all -> 0x003f }
            r2.append(r4)     // Catch:{ IOException -> 0x004b, all -> 0x003f }
            java.lang.String r4 = r2.toString()     // Catch:{ IOException -> 0x004b, all -> 0x003f }
            java.lang.Process r4 = r1.exec(r4)     // Catch:{ IOException -> 0x004b, all -> 0x003f }
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ IOException -> 0x004b, all -> 0x003f }
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x004b, all -> 0x003f }
            java.io.InputStream r4 = r4.getInputStream()     // Catch:{ IOException -> 0x004b, all -> 0x003f }
            r2.<init>(r4)     // Catch:{ IOException -> 0x004b, all -> 0x003f }
            r4 = 1024(0x400, float:1.435E-42)
            r1.<init>(r2, r4)     // Catch:{ IOException -> 0x004b, all -> 0x003f }
            java.lang.String r4 = r1.readLine()     // Catch:{ IOException -> 0x003d, all -> 0x003a }
            r1.close()     // Catch:{ IOException -> 0x003d, all -> 0x003a }
            r1.close()     // Catch:{ IOException -> 0x0035 }
            goto L_0x0039
        L_0x0035:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0039:
            return r4
        L_0x003a:
            r4 = move-exception
            r0 = r1
            goto L_0x0040
        L_0x003d:
            goto L_0x004c
        L_0x003f:
            r4 = move-exception
        L_0x0040:
            if (r0 == 0) goto L_0x004a
            r0.close()     // Catch:{ IOException -> 0x0046 }
            goto L_0x004a
        L_0x0046:
            r0 = move-exception
            r0.printStackTrace()
        L_0x004a:
            throw r4
        L_0x004b:
            r1 = r0
        L_0x004c:
            if (r1 == 0) goto L_0x0056
            r1.close()     // Catch:{ IOException -> 0x0052 }
            goto L_0x0056
        L_0x0052:
            r4 = move-exception
            r4.printStackTrace()
        L_0x0056:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yhao.floatwindow.Rom.getProp(java.lang.String):java.lang.String");
    }
}
