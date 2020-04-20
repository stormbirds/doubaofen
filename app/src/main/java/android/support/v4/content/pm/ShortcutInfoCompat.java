package android.support.v4.content.pm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.IconCompat;
import android.text.TextUtils;
import java.util.Arrays;

public class ShortcutInfoCompat {
    /* access modifiers changed from: private */
    public ComponentName mActivity;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public CharSequence mDisabledMessage;
    /* access modifiers changed from: private */
    public IconCompat mIcon;
    /* access modifiers changed from: private */
    public String mId;
    /* access modifiers changed from: private */
    public Intent[] mIntents;
    /* access modifiers changed from: private */
    public CharSequence mLabel;
    /* access modifiers changed from: private */
    public CharSequence mLongLabel;

    private ShortcutInfoCompat() {
    }

    /* access modifiers changed from: package-private */
    public ShortcutInfo toShortcutInfo() {
        ShortcutInfo.Builder intents = new ShortcutInfo.Builder(this.mContext, this.mId).setShortLabel(this.mLabel).setIntents(this.mIntents);
        IconCompat iconCompat = this.mIcon;
        if (iconCompat != null) {
            intents.setIcon(iconCompat.toIcon());
        }
        if (!TextUtils.isEmpty(this.mLongLabel)) {
            intents.setLongLabel(this.mLongLabel);
        }
        if (!TextUtils.isEmpty(this.mDisabledMessage)) {
            intents.setDisabledMessage(this.mDisabledMessage);
        }
        ComponentName componentName = this.mActivity;
        if (componentName != null) {
            intents.setActivity(componentName);
        }
        return intents.build();
    }

    /* access modifiers changed from: package-private */
    public Intent addToIntent(Intent intent) {
        Intent[] intentArr = this.mIntents;
        intent.putExtra("android.intent.extra.shortcut.INTENT", intentArr[intentArr.length - 1]).putExtra("android.intent.extra.shortcut.NAME", this.mLabel.toString());
        IconCompat iconCompat = this.mIcon;
        if (iconCompat != null) {
            iconCompat.addToShortcutIntent(intent);
        }
        return intent;
    }

    public String getId() {
        return this.mId;
    }

    public ComponentName getActivity() {
        return this.mActivity;
    }

    public CharSequence getShortLabel() {
        return this.mLabel;
    }

    public CharSequence getLongLabel() {
        return this.mLongLabel;
    }

    public CharSequence getDisabledMessage() {
        return this.mDisabledMessage;
    }

    public Intent getIntent() {
        Intent[] intentArr = this.mIntents;
        return intentArr[intentArr.length - 1];
    }

    public Intent[] getIntents() {
        Intent[] intentArr = this.mIntents;
        return (Intent[]) Arrays.copyOf(intentArr, intentArr.length);
    }

    public static class Builder {
        private final ShortcutInfoCompat mInfo = new ShortcutInfoCompat();

        public Builder(Context context, String str) {
            Context unused = this.mInfo.mContext = context;
            String unused2 = this.mInfo.mId = str;
        }

        public Builder setShortLabel(CharSequence charSequence) {
            CharSequence unused = this.mInfo.mLabel = charSequence;
            return this;
        }

        public Builder setLongLabel(CharSequence charSequence) {
            CharSequence unused = this.mInfo.mLongLabel = charSequence;
            return this;
        }

        public Builder setDisabledMessage(CharSequence charSequence) {
            CharSequence unused = this.mInfo.mDisabledMessage = charSequence;
            return this;
        }

        public Builder setIntent(Intent intent) {
            return setIntents(new Intent[]{intent});
        }

        public Builder setIntents(Intent[] intentArr) {
            Intent[] unused = this.mInfo.mIntents = intentArr;
            return this;
        }

        public Builder setIcon(Bitmap bitmap) {
            return setIcon(IconCompat.createWithBitmap(bitmap));
        }

        public Builder setIcon(int i) {
            return setIcon(IconCompat.createWithResource(this.mInfo.mContext, i));
        }

        public Builder setIcon(IconCompat iconCompat) {
            IconCompat unused = this.mInfo.mIcon = iconCompat;
            return this;
        }

        public Builder setActivity(ComponentName componentName) {
            ComponentName unused = this.mInfo.mActivity = componentName;
            return this;
        }

        public ShortcutInfoCompat build() {
            if (TextUtils.isEmpty(this.mInfo.mLabel)) {
                throw new IllegalArgumentException("Shortcut much have a non-empty label");
            } else if (this.mInfo.mIntents != null && this.mInfo.mIntents.length != 0) {
                return this.mInfo;
            } else {
                throw new IllegalArgumentException("Shortcut much have an intent");
            }
        }
    }
}
