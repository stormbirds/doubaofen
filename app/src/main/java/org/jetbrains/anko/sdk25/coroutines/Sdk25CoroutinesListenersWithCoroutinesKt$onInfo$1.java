package org.jetbrains.anko.sdk25.coroutines;

import android.media.MediaPlayer;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.coroutines.experimental.intrinsics.IntrinsicsKt;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.BuildersKt;
import kotlinx.coroutines.experimental.CoroutineScope;
import kotlinx.coroutines.experimental.CoroutineStart;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\n¢\u0006\u0002\b\b"}, d2 = {"<anonymous>", "", "mp", "Landroid/media/MediaPlayer;", "kotlin.jvm.PlatformType", "what", "", "extra", "onInfo"}, k = 3, mv = {1, 1, 5})
/* compiled from: ListenersWithCoroutines.kt */
final class Sdk25CoroutinesListenersWithCoroutinesKt$onInfo$1 implements MediaPlayer.OnInfoListener {
    final /* synthetic */ CoroutineContext $context;
    final /* synthetic */ Function5 $handler;
    final /* synthetic */ boolean $returnValue;

    Sdk25CoroutinesListenersWithCoroutinesKt$onInfo$1(CoroutineContext coroutineContext, Function5 function5, boolean z) {
        this.$context = coroutineContext;
        this.$handler = function5;
        this.$returnValue = z;
    }

    @Metadata(bv = {1, 0, 1}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H@ø\u0001\u0000¢\u0006\u0004\b\u0003\u0010\u0004"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/experimental/CoroutineScope;", "invoke", "(Lkotlinx/coroutines/experimental/CoroutineScope;Lkotlin/coroutines/experimental/Continuation;)Ljava/lang/Object;"}, k = 3, mv = {1, 1, 5})
    /* renamed from: org.jetbrains.anko.sdk25.coroutines.Sdk25CoroutinesListenersWithCoroutinesKt$onInfo$1$1  reason: invalid class name */
    /* compiled from: ListenersWithCoroutines.kt */
    static final class AnonymousClass1 extends CoroutineImpl implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        private CoroutineScope p$;
        final /* synthetic */ Sdk25CoroutinesListenersWithCoroutinesKt$onInfo$1 this$0;

        {
            this.this$0 = r1;
        }

        public final Continuation<Unit> create(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            Intrinsics.checkParameterIsNotNull(coroutineScope, "$receiver");
            Intrinsics.checkParameterIsNotNull(continuation, "$continuation");
            AnonymousClass1 r1 = new AnonymousClass1(this.this$0, mediaPlayer2, i3, i4, continuation);
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
                Function5 function5 = this.this$0.$handler;
                MediaPlayer mediaPlayer = mediaPlayer2;
                Integer valueOf = Integer.valueOf(i3);
                Integer valueOf2 = Integer.valueOf(i4);
                this.label = 1;
                if (function5.invoke(coroutineScope, mediaPlayer, valueOf, valueOf2, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                throw th;
            }
            return Unit.INSTANCE;
        }
    }

    public final boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
        final MediaPlayer mediaPlayer2 = mediaPlayer;
        final int i3 = i;
        final int i4 = i2;
        BuildersKt.launch$default(this.$context, (CoroutineStart) null, new AnonymousClass1(this, (Continuation) null), 2, (Object) null);
        return this.$returnValue;
    }
}