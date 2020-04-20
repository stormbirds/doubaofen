package okhttp3.internal.publicsuffix;

import java.net.IDN;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.UByte;
import okhttp3.internal.Util;

public final class PublicSuffixDatabase {
    private static final String[] EMPTY_RULE = new String[0];
    private static final byte EXCEPTION_MARKER = 33;
    private static final String[] PREVAILING_RULE = {"*"};
    public static final String PUBLIC_SUFFIX_RESOURCE = "publicsuffixes.gz";
    private static final byte[] WILDCARD_LABEL = {42};
    private static final PublicSuffixDatabase instance = new PublicSuffixDatabase();
    private final AtomicBoolean listRead = new AtomicBoolean(false);
    private byte[] publicSuffixExceptionListBytes;
    private byte[] publicSuffixListBytes;
    private final CountDownLatch readCompleteLatch = new CountDownLatch(1);

    public static PublicSuffixDatabase get() {
        return instance;
    }

    public String getEffectiveTldPlusOne(String str) {
        int i;
        int i2;
        if (str != null) {
            String[] split = IDN.toUnicode(str).split("\\.");
            String[] findMatchingRule = findMatchingRule(split);
            if (split.length == findMatchingRule.length && findMatchingRule[0].charAt(0) != '!') {
                return null;
            }
            if (findMatchingRule[0].charAt(0) == '!') {
                i2 = split.length;
                i = findMatchingRule.length;
            } else {
                i2 = split.length;
                i = findMatchingRule.length + 1;
            }
            StringBuilder sb = new StringBuilder();
            String[] split2 = str.split("\\.");
            for (int i3 = i2 - i; i3 < split2.length; i3++) {
                sb.append(split2[i3]);
                sb.append('.');
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
        throw new NullPointerException("domain == null");
    }

    /* JADX WARNING: Removed duplicated region for block: B:34:0x0068 A[LOOP:3: B:34:0x0068->B:38:0x0075, LOOP_START, PHI: r1 
      PHI: (r1v3 int) = (r1v0 int), (r1v4 int) binds: [B:33:0x0066, B:38:0x0075] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x007b  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0093  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String[] findMatchingRule(java.lang.String[] r8) {
        /*
            r7 = this;
            java.util.concurrent.atomic.AtomicBoolean r0 = r7.listRead
            boolean r0 = r0.get()
            r1 = 0
            r2 = 1
            if (r0 != 0) goto L_0x0016
            java.util.concurrent.atomic.AtomicBoolean r0 = r7.listRead
            boolean r0 = r0.compareAndSet(r1, r2)
            if (r0 == 0) goto L_0x0016
            r7.readTheList()
            goto L_0x001b
        L_0x0016:
            java.util.concurrent.CountDownLatch r0 = r7.readCompleteLatch     // Catch:{ InterruptedException -> 0x001b }
            r0.await()     // Catch:{ InterruptedException -> 0x001b }
        L_0x001b:
            monitor-enter(r7)
            byte[] r0 = r7.publicSuffixListBytes     // Catch:{ all -> 0x00bf }
            if (r0 == 0) goto L_0x00b7
            monitor-exit(r7)     // Catch:{ all -> 0x00bf }
            int r0 = r8.length
            byte[][] r0 = new byte[r0][]
            r3 = 0
        L_0x0025:
            int r4 = r8.length
            if (r3 >= r4) goto L_0x0035
            r4 = r8[r3]
            java.nio.charset.Charset r5 = okhttp3.internal.Util.UTF_8
            byte[] r4 = r4.getBytes(r5)
            r0[r3] = r4
            int r3 = r3 + 1
            goto L_0x0025
        L_0x0035:
            r8 = 0
        L_0x0036:
            int r3 = r0.length
            r4 = 0
            if (r8 >= r3) goto L_0x0046
            byte[] r3 = r7.publicSuffixListBytes
            java.lang.String r3 = binarySearchBytes(r3, r0, r8)
            if (r3 == 0) goto L_0x0043
            goto L_0x0047
        L_0x0043:
            int r8 = r8 + 1
            goto L_0x0036
        L_0x0046:
            r3 = r4
        L_0x0047:
            int r8 = r0.length
            if (r8 <= r2) goto L_0x0065
            java.lang.Object r8 = r0.clone()
            byte[][] r8 = (byte[][]) r8
            r5 = 0
        L_0x0051:
            int r6 = r8.length
            int r6 = r6 - r2
            if (r5 >= r6) goto L_0x0065
            byte[] r6 = WILDCARD_LABEL
            r8[r5] = r6
            byte[] r6 = r7.publicSuffixListBytes
            java.lang.String r6 = binarySearchBytes(r6, r8, r5)
            if (r6 == 0) goto L_0x0062
            goto L_0x0066
        L_0x0062:
            int r5 = r5 + 1
            goto L_0x0051
        L_0x0065:
            r6 = r4
        L_0x0066:
            if (r6 == 0) goto L_0x0078
        L_0x0068:
            int r8 = r0.length
            int r8 = r8 - r2
            if (r1 >= r8) goto L_0x0078
            byte[] r8 = r7.publicSuffixExceptionListBytes
            java.lang.String r8 = binarySearchBytes(r8, r0, r1)
            if (r8 == 0) goto L_0x0075
            goto L_0x0079
        L_0x0075:
            int r1 = r1 + 1
            goto L_0x0068
        L_0x0078:
            r8 = r4
        L_0x0079:
            if (r8 == 0) goto L_0x0093
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "!"
            r0.append(r1)
            r0.append(r8)
            java.lang.String r8 = r0.toString()
            java.lang.String r0 = "\\."
            java.lang.String[] r8 = r8.split(r0)
            return r8
        L_0x0093:
            if (r3 != 0) goto L_0x009a
            if (r6 != 0) goto L_0x009a
            java.lang.String[] r8 = PREVAILING_RULE
            return r8
        L_0x009a:
            if (r3 == 0) goto L_0x00a3
            java.lang.String r8 = "\\."
            java.lang.String[] r8 = r3.split(r8)
            goto L_0x00a5
        L_0x00a3:
            java.lang.String[] r8 = EMPTY_RULE
        L_0x00a5:
            if (r6 == 0) goto L_0x00ae
            java.lang.String r0 = "\\."
            java.lang.String[] r0 = r6.split(r0)
            goto L_0x00b0
        L_0x00ae:
            java.lang.String[] r0 = EMPTY_RULE
        L_0x00b0:
            int r1 = r8.length
            int r2 = r0.length
            if (r1 <= r2) goto L_0x00b5
            goto L_0x00b6
        L_0x00b5:
            r8 = r0
        L_0x00b6:
            return r8
        L_0x00b7:
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException     // Catch:{ all -> 0x00bf }
            java.lang.String r0 = "Unable to load publicsuffixes.gz resource from the classpath."
            r8.<init>(r0)     // Catch:{ all -> 0x00bf }
            throw r8     // Catch:{ all -> 0x00bf }
        L_0x00bf:
            r8 = move-exception
            monitor-exit(r7)     // Catch:{ all -> 0x00bf }
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.publicsuffix.PublicSuffixDatabase.findMatchingRule(java.lang.String[]):java.lang.String[]");
    }

    private static String binarySearchBytes(byte[] bArr, byte[][] bArr2, int i) {
        int i2;
        byte b;
        int i3;
        byte[] bArr3 = bArr;
        byte[][] bArr4 = bArr2;
        int length = bArr3.length;
        int i4 = 0;
        while (i4 < length) {
            int i5 = (i4 + length) / 2;
            while (i5 > -1 && bArr3[i5] != 10) {
                i5--;
            }
            int i6 = i5 + 1;
            int i7 = 1;
            while (true) {
                i2 = i6 + i7;
                if (bArr3[i2] == 10) {
                    break;
                }
                i7++;
            }
            int i8 = i2 - i6;
            int i9 = i;
            boolean z = false;
            int i10 = 0;
            int i11 = 0;
            while (true) {
                if (z) {
                    z = false;
                    b = 46;
                } else {
                    b = bArr4[i9][i10] & UByte.MAX_VALUE;
                }
                i3 = b - (bArr3[i6 + i11] & UByte.MAX_VALUE);
                if (i3 == 0) {
                    i11++;
                    i10++;
                    if (i11 == i8) {
                        break;
                    } else if (bArr4[i9].length == i10) {
                        if (i9 == bArr4.length - 1) {
                            break;
                        }
                        i9++;
                        z = true;
                        i10 = -1;
                    }
                } else {
                    break;
                }
            }
            if (i3 >= 0) {
                if (i3 <= 0) {
                    int i12 = i8 - i11;
                    int length2 = bArr4[i9].length - i10;
                    while (true) {
                        i9++;
                        if (i9 >= bArr4.length) {
                            break;
                        }
                        length2 += bArr4[i9].length;
                    }
                    if (length2 >= i12) {
                        if (length2 <= i12) {
                            return new String(bArr3, i6, i8, Util.UTF_8);
                        }
                    }
                }
                i4 = i2 + 1;
            }
            length = i6 - 1;
        }
        return null;
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        	at java.util.ArrayList.rangeCheck(ArrayList.java:657)
        	at java.util.ArrayList.get(ArrayList.java:433)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    private void readTheList() {
        /*
            r6 = this;
            java.lang.Class<okhttp3.internal.publicsuffix.PublicSuffixDatabase> r0 = okhttp3.internal.publicsuffix.PublicSuffixDatabase.class
            java.lang.ClassLoader r0 = r0.getClassLoader()
            java.lang.String r1 = "publicsuffixes.gz"
            java.io.InputStream r0 = r0.getResourceAsStream(r1)
            r1 = 0
            if (r0 == 0) goto L_0x0048
            okio.GzipSource r2 = new okio.GzipSource
            okio.Source r0 = okio.Okio.source((java.io.InputStream) r0)
            r2.<init>(r0)
            okio.BufferedSource r0 = okio.Okio.buffer((okio.Source) r2)
            int r2 = r0.readInt()     // Catch:{ IOException -> 0x0035 }
            byte[] r2 = new byte[r2]     // Catch:{ IOException -> 0x0035 }
            r0.readFully(r2)     // Catch:{ IOException -> 0x0035 }
            int r3 = r0.readInt()     // Catch:{ IOException -> 0x0035 }
            byte[] r3 = new byte[r3]     // Catch:{ IOException -> 0x0035 }
            r0.readFully(r3)     // Catch:{ IOException -> 0x0035 }
            okhttp3.internal.Util.closeQuietly((java.io.Closeable) r0)
            r1 = r2
            goto L_0x0049
        L_0x0033:
            r1 = move-exception
            goto L_0x0044
        L_0x0035:
            r2 = move-exception
            okhttp3.internal.platform.Platform r3 = okhttp3.internal.platform.Platform.get()     // Catch:{ all -> 0x0033 }
            r4 = 5
            java.lang.String r5 = "Failed to read public suffix list"
            r3.log(r4, r5, r2)     // Catch:{ all -> 0x0033 }
            okhttp3.internal.Util.closeQuietly((java.io.Closeable) r0)
            goto L_0x0048
        L_0x0044:
            okhttp3.internal.Util.closeQuietly((java.io.Closeable) r0)
            throw r1
        L_0x0048:
            r3 = r1
        L_0x0049:
            monitor-enter(r6)
            r6.publicSuffixListBytes = r1     // Catch:{ all -> 0x0055 }
            r6.publicSuffixExceptionListBytes = r3     // Catch:{ all -> 0x0055 }
            monitor-exit(r6)     // Catch:{ all -> 0x0055 }
            java.util.concurrent.CountDownLatch r0 = r6.readCompleteLatch
            r0.countDown()
            return
        L_0x0055:
            r0 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x0055 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.publicsuffix.PublicSuffixDatabase.readTheList():void");
    }

    /* access modifiers changed from: package-private */
    public void setListBytes(byte[] bArr, byte[] bArr2) {
        this.publicSuffixListBytes = bArr;
        this.publicSuffixExceptionListBytes = bArr2;
        this.listRead.set(true);
        this.readCompleteLatch.countDown();
    }
}
