package cn.hiui.voice;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import cn.hiui.voice.VoiceService;
import java.io.File;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import org.jetbrains.anko.AlertBuilder;
import org.jetbrains.anko.AndroidDialogsKt;
import org.jetbrains.anko.AndroidSelectorsKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\u0010\u0000\u001a\u00020\u00012\u0016\u0010\u0002\u001a\u0012\u0012\u0002\b\u0003 \u0004*\b\u0012\u0002\b\u0003\u0018\u00010\u00030\u00032\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u00010\u00060\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\n¢\u0006\u0002\b\u000b"}, d2 = {"<anonymous>", "", "parent", "Landroid/widget/AdapterView;", "kotlin.jvm.PlatformType", "view", "Landroid/view/View;", "position", "", "id", "", "onItemClick"}, k = 3, mv = {1, 1, 15})
/* compiled from: MainActivity.kt */
final class MainActivity$onCreate$1 implements AdapterView.OnItemClickListener {
    final /* synthetic */ MainActivity this$0;

    MainActivity$onCreate$1(MainActivity mainActivity) {
        this.this$0 = mainActivity;
    }

    public final void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        VoiceService.Companion.DataAdapter adapter = this.this$0.getAdapter();
        if (adapter == null) {
            Intrinsics.throwNpe();
        }
        Object item = adapter.getItem(i);
        if (item != null) {
            final File file = (File) item;
            String tag = MainActivity.Companion.getTAG();
            Log.i(tag, "click " + file.getName());
            AndroidSelectorsKt.selector((Context) this.this$0, (CharSequence) file.getName(), (List<? extends CharSequence>) ArraysKt.toList((T[]) new String[]{"重命名", "删除"}), (Function2<? super DialogInterface, ? super Integer, Unit>) new Function2<DialogInterface, Integer, Unit>(this) {
                final /* synthetic */ MainActivity$onCreate$1 this$0;

                {
                    this.this$0 = r1;
                }

                public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
                    invoke((DialogInterface) obj, ((Number) obj2).intValue());
                    return Unit.INSTANCE;
                }

                public final void invoke(DialogInterface dialogInterface, int i) {
                    Intrinsics.checkParameterIsNotNull(dialogInterface, "dialogInterface");
                    if (i == 0) {
                        final Ref.ObjectRef objectRef = new Ref.ObjectRef();
                        objectRef.element = file.getParent();
                        final EditText editText = new EditText(this.this$0.this$0);
                        editText.setText(file.getName());
                        AndroidDialogsKt.alert((Context) this.this$0.this$0, (Function1<? super AlertBuilder<? extends DialogInterface>, Unit>) new Function1<AlertBuilder<? extends DialogInterface>, Unit>(this) {
                            final /* synthetic */ AnonymousClass1 this$0;

                            {
                                this.this$0 = r1;
                            }

                            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                                invoke((AlertBuilder<? extends DialogInterface>) (AlertBuilder) obj);
                                return Unit.INSTANCE;
                            }

                            public final void invoke(AlertBuilder<? extends DialogInterface> alertBuilder) {
                                Intrinsics.checkParameterIsNotNull(alertBuilder, "$receiver");
                                alertBuilder.setTitle("重命名");
                                alertBuilder.setCustomView(editText);
                                alertBuilder.positiveButton(17039379, (Function1<? super DialogInterface, Unit>) new Function1<DialogInterface, Unit>(this) {
                                    final /* synthetic */ AnonymousClass1 this$0;

                                    {
                                        this.this$0 = r1;
                                    }

                                    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                                        invoke((DialogInterface) obj);
                                        return Unit.INSTANCE;
                                    }

                                    public final void invoke(DialogInterface dialogInterface) {
                                        Intrinsics.checkParameterIsNotNull(dialogInterface, "it");
                                        file.renameTo(new File((String) objectRef.element, editText.getText().toString()));
                                        this.this$0.this$0.this$0.this$0.loadFile();
                                    }
                                });
                                alertBuilder.negativeButton(17039369, (Function1<? super DialogInterface, Unit>) AnonymousClass2.INSTANCE);
                            }
                        }).show();
                    } else if (i == 1) {
                        AndroidDialogsKt.alert((Context) this.this$0.this$0, (Function1<? super AlertBuilder<? extends DialogInterface>, Unit>) new Function1<AlertBuilder<? extends DialogInterface>, Unit>(this) {
                            final /* synthetic */ AnonymousClass1 this$0;

                            {
                                this.this$0 = r1;
                            }

                            public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                                invoke((AlertBuilder<? extends DialogInterface>) (AlertBuilder) obj);
                                return Unit.INSTANCE;
                            }

                            public final void invoke(AlertBuilder<? extends DialogInterface> alertBuilder) {
                                Intrinsics.checkParameterIsNotNull(alertBuilder, "$receiver");
                                alertBuilder.setTitle("确认删除 " + file.getName());
                                alertBuilder.positiveButton(17039379, (Function1<? super DialogInterface, Unit>) new Function1<DialogInterface, Unit>(this) {
                                    final /* synthetic */ AnonymousClass2 this$0;

                                    {
                                        this.this$0 = r1;
                                    }

                                    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                                        invoke((DialogInterface) obj);
                                        return Unit.INSTANCE;
                                    }

                                    public final void invoke(DialogInterface dialogInterface) {
                                        Intrinsics.checkParameterIsNotNull(dialogInterface, "it");
                                        file.delete();
                                        this.this$0.this$0.this$0.this$0.loadFile();
                                    }
                                });
                                alertBuilder.negativeButton(17039369, (Function1<? super DialogInterface, Unit>) AnonymousClass2.INSTANCE);
                            }
                        }).show();
                    }
                }
            });
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type java.io.File");
    }
}
