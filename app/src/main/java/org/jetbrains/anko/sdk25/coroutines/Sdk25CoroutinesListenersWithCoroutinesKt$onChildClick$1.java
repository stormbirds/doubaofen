package org.jetbrains.anko.sdk25.coroutines;

import android.view.View;
import android.widget.ExpandableListView;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.intrinsics.IntrinsicsKt;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function7;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.BuildersKt;
import kotlinx.coroutines.experimental.CoroutineScope;
import kotlinx.coroutines.experimental.CoroutineStart;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000$\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u00010\u00060\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000bH\n¢\u0006\u0002\b\f"}, d2 = {"<anonymous>", "", "parent", "Landroid/widget/ExpandableListView;", "kotlin.jvm.PlatformType", "v", "Landroid/view/View;", "groupPosition", "", "childPosition", "id", "", "onChildClick"}, k = 3, mv = {1, 1, 5})
/* compiled from: ListenersWithCoroutines.kt */
final class Sdk25CoroutinesListenersWithCoroutinesKt$onChildClick$1 implements ExpandableListView.OnChildClickListener {
    final /* synthetic */ CoroutineContext $context;
    final /* synthetic */ Function7 $handler;
    final /* synthetic */ boolean $returnValue;

    Sdk25CoroutinesListenersWithCoroutinesKt$onChildClick$1(CoroutineContext coroutineContext, Function7 function7, boolean z) {
        this.$context = coroutineContext;
        this.$handler = function7;
        this.$returnValue = z;
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H@ø\u0001\u0000¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/experimental/CoroutineScope;", "invoke", "(Lkotlinx/coroutines/experimental/CoroutineScope;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 5})
    /* renamed from: org.jetbrains.anko.sdk25.coroutines.Sdk25CoroutinesListenersWithCoroutinesKt$onChildClick$1$1  reason: invalid class name */
    /* compiled from: ListenersWithCoroutines.kt */
    static final class AnonymousClass1 extends CoroutineImpl implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        private CoroutineScope p$;
        final /* synthetic */ Sdk25CoroutinesListenersWithCoroutinesKt$onChildClick$1 this$0;

        {
            this.this$0 = r1;
        }

        public final Continuation<Unit> create(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            Intrinsics.checkParameterIsNotNull(coroutineScope, "$receiver");
            Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
            AnonymousClass1 r1 = new AnonymousClass1(this.this$0, expandableListView2, view2, i3, i4, j2, continuation);
            r1.p$ = coroutineScope;
            return r1;
        }

        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            Intrinsics.checkParameterIsNotNull(coroutineScope, "$receiver");
            Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
            return ((AnonymousClass1) create(coroutineScope, continuation)).doResume(Unit.INSTANCE, (Throwable) null);
        }

        public final Object doResume(Object obj, Throwable th) {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i != 0) {
                if (i != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                } else if (th != null) {
                    throw th;
                }
            } else if (th == null) {
                CoroutineScope coroutineScope = this.p$;
                Function7 function7 = this.this$0.$handler;
                ExpandableListView expandableListView = expandableListView2;
                View view = view2;
                Integer valueOf = Integer.valueOf(i3);
                Integer valueOf2 = Integer.valueOf(i4);
                Long valueOf3 = Long.valueOf(j2);
                this.label = 1;
                if (function7.invoke(coroutineScope, expandableListView, view, valueOf, valueOf2, valueOf3, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                throw th;
            }
            return Unit.INSTANCE;
        }
    }

    public final boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long j) {
        final ExpandableListView expandableListView2 = expandableListView;
        final View view2 = view;
        final int i3 = i;
        final int i4 = i2;
        final long j2 = j;
        BuildersKt.launch$default(this.$context, (CoroutineStart) null, new AnonymousClass1(this, (Continuation) null), 2, (Object) null);
        return this.$returnValue;
    }
}
