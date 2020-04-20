package org.jetbrains.anko.sdk25.coroutines;

import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.media.MediaPlayer;
import android.media.tv.TvView;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowInsets;
import android.widget.AbsListView;
import android.widget.ActionMenuView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CalendarView;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toolbar;
import android.widget.VideoView;
import android.widget.ZoomControls;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.experimental.Continuation;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function11;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.functions.Function6;
import kotlin.jvm.functions.Function7;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.experimental.CoroutineScope;
import kotlinx.coroutines.experimental.android.HandlerContextKt;

@Metadata(bv = {1, 0, 1}, d1 = {"\u0000®\u0003\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u001a{\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062U\u0010\u0007\u001aQ\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u00010\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0015\u0012\u0013\u0018\u00010\u0006¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\r\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\b¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010\u0011\u001a-\u0010\u0012\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0017\u0010\u0013\u001a\u0013\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00010\u0014¢\u0006\u0002\b\u0010\u001aq\u0010\u0016\u001a\u00020\u0001*\u00020\u00172\b\b\u0002\u0010\u0003\u001a\u00020\u00042S\u0010\u0007\u001aO\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u00010\u0017¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\u0018\u0012\u0013\u0012\u00110\u0019¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\b¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010\u001b\u001aq\u0010\u0016\u001a\u00020\u0001*\u00020\u001c2\b\b\u0002\u0010\u0003\u001a\u00020\u00042S\u0010\u0007\u001aO\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u00010\u001c¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\u001d\u0012\u0013\u0012\u00110\u001e¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\u001f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\b¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010 \u001a¾\u0001\u0010!\u001a\u00020\u0001*\u00020\"2\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00192\u0001\u0010\u0007\u001a\u0001\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u00010\"¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b($\u0012\u0015\u0012\u0013\u0018\u00010\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\u001e¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(%\u0012\u0013\u0012\u00110\u001e¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(&\u0012\u0013\u0012\u00110'¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b((\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0#¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010)\u001a\\\u0010*\u001a\u00020\u0001*\u00020+2\b\b\u0002\u0010\u0003\u001a\u00020\u00042>\u0010\u0007\u001a:\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u00010+¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(-\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0,¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010.\u001a\\\u0010/\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042>\u0010\u0007\u001a:\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u00010\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0,¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u00100\u001aO\u00101\u001a\u00020\u0001*\u0002022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00192'\u0010\u0007\u001a#\b\u0001\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f03¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u00104\u001a\\\u00105\u001a\u00020\u0001*\u0002062\b\b\u0002\u0010\u0003\u001a\u00020\u00042>\u0010\u0007\u001a:\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u000107¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(8\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0,¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u00109\u001af\u0010:\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00192>\u0010\u0007\u001a:\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u00010\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0,¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010;\u001a\u0001\u0010<\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042l\u0010\u0007\u001ah\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u00010>¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(?\u0012\u0015\u0012\u0013\u0018\u00010\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0015\u0012\u0013\u0018\u00010@¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(A\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0=¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010B\u001a\u0001\u0010C\u001a\u00020\u0001*\u00020D2\b\b\u0002\u0010\u0003\u001a\u00020\u00042}\u0010\u0007\u001ay\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u00010D¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(F\u0012\u0013\u0012\u00110\u001e¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(G\u0012\u0013\u0012\u00110\u001e¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(H\u0012\u0013\u0012\u00110\u001e¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(I\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0E¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010J\u001aE\u0010K\u001a\u00020\u0001*\u00020L2\b\b\u0002\u0010\u0003\u001a\u00020\u00042'\u0010\u0007\u001a#\b\u0001\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f03¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010M\u001ay\u0010N\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00192Q\u0010\u0007\u001aM\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110O¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(P\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\b¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010Q\u001aE\u0010R\u001a\u00020\u0001*\u00020S2\b\b\u0002\u0010\u0003\u001a\u00020\u00042'\u0010\u0007\u001a#\b\u0001\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f03¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010T\u001aE\u0010U\u001a\u00020\u0001*\u00020S2\b\b\u0002\u0010\u0003\u001a\u00020\u00042'\u0010\u0007\u001a#\b\u0001\u0012\u0004\u0012\u00020\t\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f03¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010T\u001a-\u0010V\u001a\u00020\u0001*\u00020S2\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0017\u0010\u0013\u001a\u0013\u0012\u0004\u0012\u00020W\u0012\u0004\u0012\u00020\u00010\u0014¢\u0006\u0002\b\u0010\u001a\u0001\u0010X\u001a\u00020\u0001*\u00020Y2\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00192j\u0010\u0007\u001af\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u00010Y¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\u001e¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(Z\u0012\u0015\u0012\u0013\u0018\u00010[¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(P\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0=¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010\\\u001a\u0001\u0010]\u001a\u00020\u0001*\u0002062\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00192h\u0010\u0007\u001ad\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u000107¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(8\u0012\u0013\u0012\u00110\u001e¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(^\u0012\u0013\u0012\u00110\u001e¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(_\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0=¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010`\u001ao\u0010a\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042Q\u0010\u0007\u001aM\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\u0019¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\b¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010c\u001ay\u0010d\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00192Q\u0010\u0007\u001aM\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110e¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(P\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\b¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010Q\u001a-\u0010f\u001a\u00020\u0001*\u00020g2\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0017\u0010\u0013\u001a\u0013\u0012\u0004\u0012\u00020h\u0012\u0004\u0012\u00020\u00010\u0014¢\u0006\u0002\b\u0010\u001as\u0010i\u001a\u00020\u0001*\u00020g2\b\b\u0002\u0010\u0003\u001a\u00020\u00042U\u0010\u0007\u001aQ\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u00010g¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(j\u0012\u0015\u0012\u0013\u0018\u00010k¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(l\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\b¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010m\u001a-\u0010n\u001a\u00020\u0001*\u00020g2\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0017\u0010\u0013\u001a\u0013\u0012\u0004\u0012\u00020o\u0012\u0004\u0012\u00020\u00010\u0014¢\u0006\u0002\b\u0010\u001a§\u0001\u0010p\u001a\u00020\u0001*\u00020\"2\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00192\u0010\u0007\u001a{\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u00010\"¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b($\u0012\u0015\u0012\u0013\u0018\u00010\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\u001e¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(%\u0012\u0013\u0012\u00110'¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b((\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0E¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010q\u001aZ\u0010r\u001a\u00020\u0001*\u00020\"2\b\b\u0002\u0010\u0003\u001a\u00020\u00042<\u0010\u0007\u001a8\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0013\u0012\u00110\u001e¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(%\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0,¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010s\u001aZ\u0010t\u001a\u00020\u0001*\u00020\"2\b\b\u0002\u0010\u0003\u001a\u00020\u00042<\u0010\u0007\u001a8\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0013\u0012\u00110\u001e¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(%\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0,¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010s\u001a-\u0010u\u001a\u00020\u0001*\u00020v2\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0017\u0010\u0013\u001a\u0013\u0012\u0004\u0012\u00020w\u0012\u0004\u0012\u00020\u00010\u0014¢\u0006\u0002\b\u0010\u001ay\u0010x\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00192Q\u0010\u0007\u001aM\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110e¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(P\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\b¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010Q\u001as\u0010y\u001a\u00020\u0001*\u00020z2\b\b\u0002\u0010\u0003\u001a\u00020\u00042U\u0010\u0007\u001aQ\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u00010z¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b({\u0012\u0015\u0012\u0013\u0018\u00010\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(|\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\b¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010}\u001a\u0001\u0010~\u001a\u00020\u0001*\u0002062\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00192h\u0010\u0007\u001ad\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u000107¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(8\u0012\u0013\u0012\u00110\u001e¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(^\u0012\u0013\u0012\u00110\u001e¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(_\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0=¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010`\u001a³\u0001\u0010\u001a\u00020\u0001*\f\u0012\u0007\b\u0001\u0012\u00030\u00010\u00012\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0001\u0010\u0007\u001a\u0001\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u001b\u0012\u0019\u0012\u0002\b\u0003\u0018\u00010\u0001¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(\u0001\u0012\u0016\u0012\u0014\u0018\u00010\u0002¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(\u0001\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(\u0001\u0012\u0014\u0012\u00120'¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0E¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0003\u0010\u0001\u001a¾\u0001\u0010\u0001\u001a\u00020\u0001*\f\u0012\u0007\b\u0001\u0012\u00030\u00010\u00012\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00192\u0001\u0010\u0007\u001a\u0001\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u001b\u0012\u0019\u0012\u0002\b\u0003\u0018\u00010\u0001¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(\u0001\u0012\u0016\u0012\u0014\u0018\u00010\u0002¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(\u0001\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(\u0001\u0012\u0014\u0012\u00120'¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0E¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0003\u0010\u0001\u001a9\u0010\u0001\u001a\u00020\u0001*\f\u0012\u0007\b\u0001\u0012\u00030\u00010\u00012\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0018\u0010\u0013\u001a\u0014\u0012\u0005\u0012\u00030\u0001\u0012\u0004\u0012\u00020\u00010\u0014¢\u0006\u0002\b\u0010\u001a\u0001\u0010\u0001\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00192i\u0010\u0007\u001ae\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(\u0001\u0012\u0015\u0012\u0013\u0018\u00010[¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(P\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0=¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0003\u0010\u0001\u001a\u0002\u0010\u0001\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042ð\u0001\u0010\u0007\u001aë\u0001\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u00010\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(\u0001\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(\u0001\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(\u0001\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(\u0001\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(\u0001\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(\u0001\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(\u0001\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\u0001¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0003\u0010\u0001\u001ag\u0010\u0001\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00192>\u0010\u0007\u001a:\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u00010\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0,¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010;\u001ak\u0010\u0001\u001a\u00020\u0001*\u00030\u00012\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00192@\u0010\u0007\u001a<\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0017\u0012\u0015\u0018\u00010\u0001¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0,¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0003\u0010\u0001\u001ak\u0010\u0001\u001a\u00020\u0001*\u00030\u00012\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00192@\u0010\u0007\u001a<\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0017\u0012\u0015\u0018\u00010\u0001¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0,¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0003\u0010 \u0001\u001a]\u0010¡\u0001\u001a\u00020\u0001*\u0002062\b\b\u0002\u0010\u0003\u001a\u00020\u00042>\u0010\u0007\u001a:\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u000107¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(8\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0,¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u00109\u001aq\u0010¢\u0001\u001a\u00020\u0001*\u0002022\b\b\u0002\u0010\u0003\u001a\u00020\u00042Q\u0010\u0007\u001aM\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110\u0019¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\b¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0003\u0010£\u0001\u001a/\u0010¤\u0001\u001a\u00020\u0001*\u0002022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0018\u0010\u0013\u001a\u0014\u0012\u0005\u0012\u00030¥\u0001\u0012\u0004\u0012\u00020\u00010\u0014¢\u0006\u0002\b\u0010\u001a\u0001\u0010¦\u0001\u001a\u00020\u0001*\u00030§\u00012\b\b\u0002\u0010\u0003\u001a\u00020\u00042m\u0010\u0007\u001ai\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0017\u0012\u0015\u0018\u00010§\u0001¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(¨\u0001\u0012\u0015\u0012\u00130©\u0001¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(ª\u0001\u0012\u0014\u0012\u00120\u0019¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(«\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0=¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0003\u0010¬\u0001\u001av\u0010­\u0001\u001a\u00020\u0001*\u00030®\u00012\b\b\u0002\u0010\u0003\u001a\u00020\u00042U\u0010\u0007\u001aQ\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0016\u0012\u0014\u0018\u00010®\u0001¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(F\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(¯\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\b¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0003\u0010°\u0001\u001a¸\u0001\u0010±\u0001\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0001\u0010\u0007\u001a\u0001\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u00010\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(²\u0001\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(³\u0001\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(´\u0001\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(µ\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0#¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0003\u0010¶\u0001\u001a0\u0010·\u0001\u001a\u00020\u0001*\u00030¸\u00012\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0018\u0010\u0013\u001a\u0014\u0012\u0005\u0012\u00030¹\u0001\u0012\u0004\u0012\u00020\u00010\u0014¢\u0006\u0002\b\u0010\u001a^\u0010º\u0001\u001a\u00020\u0001*\u0002022\b\b\u0002\u0010\u0003\u001a\u00020\u00042>\u0010\u0007\u001a:\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u00010\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0,¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0003\u0010»\u0001\u001a0\u0010¼\u0001\u001a\u00020\u0001*\u00030½\u00012\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0018\u0010\u0013\u001a\u0014\u0012\u0005\u0012\u00030¾\u0001\u0012\u0004\u0012\u00020\u00010\u0014¢\u0006\u0002\b\u0010\u001a/\u0010¿\u0001\u001a\u00020\u0001*\u0002022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0018\u0010\u0013\u001a\u0014\u0012\u0005\u0012\u00030À\u0001\u0012\u0004\u0012\u00020\u00010\u0014¢\u0006\u0002\b\u0010\u001a\\\u0010Á\u0001\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042=\u0010\u0007\u001a9\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(Â\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0,¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u00100\u001aa\u0010Ã\u0001\u001a\u00020\u0001*\u00030Ä\u00012\b\b\u0002\u0010\u0003\u001a\u00020\u00042@\u0010\u0007\u001a<\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0017\u0012\u0015\u0018\u00010Å\u0001¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(Æ\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0,¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0003\u0010Ç\u0001\u001a\u0001\u0010È\u0001\u001a\u00020\u0001*\u00030É\u00012\b\b\u0002\u0010\u0003\u001a\u00020\u00042k\u0010\u0007\u001ag\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0016\u0012\u0014\u0018\u00010É\u0001¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(F\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(Ê\u0001\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(Ë\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0=¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0003\u0010Ì\u0001\u001az\u0010Í\u0001\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00192Q\u0010\u0007\u001aM\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0013\u0012\u00110\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\u0013\u0012\u00110e¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(P\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\b¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0002\u0010Q\u001aj\u0010Î\u0001\u001a\u00020\u0001*\u00030Ï\u00012\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00192?\u0010\u0007\u001a;\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0016\u0012\u0014\u0018\u00010Ð\u0001¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(P\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0,¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0003\u0010Ñ\u0001\u001a\u0001\u0010Ò\u0001\u001a\u00020\u0001*\u00030®\u00012\b\b\u0002\u0010\u0003\u001a\u00020\u00042l\u0010\u0007\u001ah\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0017\u0012\u0015\u0018\u00010®\u0001¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(Ó\u0001\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(Ô\u0001\u0012\u0014\u0012\u00120\u001e¢\u0006\r\b\n\u0012\t\b\u000b\u0012\u0005\b\b(Õ\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0=¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0003\u0010Ö\u0001\u001a_\u0010×\u0001\u001a\u00020\u0001*\u00030Ø\u00012\b\b\u0002\u0010\u0003\u001a\u00020\u00042>\u0010\u0007\u001a:\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u00010\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0,¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0003\u0010Ù\u0001\u001a_\u0010Ú\u0001\u001a\u00020\u0001*\u00030Ø\u00012\b\b\u0002\u0010\u0003\u001a\u00020\u00042>\u0010\u0007\u001a:\b\u0001\u0012\u0004\u0012\u00020\t\u0012\u0015\u0012\u0013\u0018\u00010\u0002¢\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\f\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u000f0,¢\u0006\u0002\b\u0010ø\u0001\u0000¢\u0006\u0003\u0010Ù\u0001\u001a/\u0010Û\u0001\u001a\u00020\u0001*\u00020Y2\b\b\u0002\u0010\u0003\u001a\u00020\u00042\u0018\u0010\u0013\u001a\u0014\u0012\u0005\u0012\u00030Ü\u0001\u0012\u0004\u0012\u00020\u00010\u0014¢\u0006\u0002\b\u0010\u0002\u0004\n\u0002\b\t¨\u0006Ý\u0001"}, d2 = {"onApplyWindowInsets", "", "Landroid/view/View;", "context", "Lkotlin/coroutines/experimental/CoroutineContext;", "returnValue", "Landroid/view/WindowInsets;", "handler", "Lkotlin/Function4;", "Lkotlinx/coroutines/experimental/CoroutineScope;", "Lkotlin/ParameterName;", "name", "v", "insets", "Lkotlin/coroutines/experimental/Continuation;", "", "Lkotlin/ExtensionFunctionType;", "(Landroid/view/View;Lkotlin/coroutines/experimental/CoroutineContext;Landroid/view/WindowInsets;Lkotlin/jvm/functions/Function4;)V", "onAttachStateChangeListener", "init", "Lkotlin/Function1;", "Lorg/jetbrains/anko/sdk25/coroutines/__View_OnAttachStateChangeListener;", "onCheckedChange", "Landroid/widget/CompoundButton;", "buttonView", "", "isChecked", "(Landroid/widget/CompoundButton;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function4;)V", "Landroid/widget/RadioGroup;", "group", "", "checkedId", "(Landroid/widget/RadioGroup;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function4;)V", "onChildClick", "Landroid/widget/ExpandableListView;", "Lkotlin/Function7;", "parent", "groupPosition", "childPosition", "", "id", "(Landroid/widget/ExpandableListView;Lkotlin/coroutines/experimental/CoroutineContext;ZLkotlin/jvm/functions/Function7;)V", "onChronometerTick", "Landroid/widget/Chronometer;", "Lkotlin/Function3;", "chronometer", "(Landroid/widget/Chronometer;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function3;)V", "onClick", "(Landroid/view/View;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function3;)V", "onClose", "Landroid/widget/SearchView;", "Lkotlin/Function2;", "(Landroid/widget/SearchView;Lkotlin/coroutines/experimental/CoroutineContext;ZLkotlin/jvm/functions/Function2;)V", "onCompletion", "Landroid/widget/VideoView;", "Landroid/media/MediaPlayer;", "mp", "(Landroid/widget/VideoView;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function3;)V", "onContextClick", "(Landroid/view/View;Lkotlin/coroutines/experimental/CoroutineContext;ZLkotlin/jvm/functions/Function3;)V", "onCreateContextMenu", "Lkotlin/Function5;", "Landroid/view/ContextMenu;", "menu", "Landroid/view/ContextMenu$ContextMenuInfo;", "menuInfo", "(Landroid/view/View;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function5;)V", "onDateChange", "Landroid/widget/CalendarView;", "Lkotlin/Function6;", "view", "year", "month", "dayOfMonth", "(Landroid/widget/CalendarView;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function6;)V", "onDismiss", "Landroid/widget/AutoCompleteTextView;", "(Landroid/widget/AutoCompleteTextView;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function2;)V", "onDrag", "Landroid/view/DragEvent;", "event", "(Landroid/view/View;Lkotlin/coroutines/experimental/CoroutineContext;ZLkotlin/jvm/functions/Function4;)V", "onDrawerClose", "Landroid/widget/SlidingDrawer;", "(Landroid/widget/SlidingDrawer;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function2;)V", "onDrawerOpen", "onDrawerScrollListener", "Lorg/jetbrains/anko/sdk25/coroutines/__SlidingDrawer_OnDrawerScrollListener;", "onEditorAction", "Landroid/widget/TextView;", "actionId", "Landroid/view/KeyEvent;", "(Landroid/widget/TextView;Lkotlin/coroutines/experimental/CoroutineContext;ZLkotlin/jvm/functions/Function5;)V", "onError", "what", "extra", "(Landroid/widget/VideoView;Lkotlin/coroutines/experimental/CoroutineContext;ZLkotlin/jvm/functions/Function5;)V", "onFocusChange", "hasFocus", "(Landroid/view/View;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function4;)V", "onGenericMotion", "Landroid/view/MotionEvent;", "onGestureListener", "Landroid/gesture/GestureOverlayView;", "Lorg/jetbrains/anko/sdk25/coroutines/__GestureOverlayView_OnGestureListener;", "onGesturePerformed", "overlay", "Landroid/gesture/Gesture;", "gesture", "(Landroid/gesture/GestureOverlayView;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function4;)V", "onGesturingListener", "Lorg/jetbrains/anko/sdk25/coroutines/__GestureOverlayView_OnGesturingListener;", "onGroupClick", "(Landroid/widget/ExpandableListView;Lkotlin/coroutines/experimental/CoroutineContext;ZLkotlin/jvm/functions/Function6;)V", "onGroupCollapse", "(Landroid/widget/ExpandableListView;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function3;)V", "onGroupExpand", "onHierarchyChangeListener", "Landroid/view/ViewGroup;", "Lorg/jetbrains/anko/sdk25/coroutines/__ViewGroup_OnHierarchyChangeListener;", "onHover", "onInflate", "Landroid/view/ViewStub;", "stub", "inflated", "(Landroid/view/ViewStub;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function4;)V", "onInfo", "onItemClick", "Landroid/widget/AdapterView;", "Landroid/widget/Adapter;", "p0", "p1", "p2", "p3", "(Landroid/widget/AdapterView;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function6;)V", "onItemLongClick", "(Landroid/widget/AdapterView;Lkotlin/coroutines/experimental/CoroutineContext;ZLkotlin/jvm/functions/Function6;)V", "onItemSelectedListener", "Lorg/jetbrains/anko/sdk25/coroutines/__AdapterView_OnItemSelectedListener;", "onKey", "keyCode", "(Landroid/view/View;Lkotlin/coroutines/experimental/CoroutineContext;ZLkotlin/jvm/functions/Function5;)V", "onLayoutChange", "Lkotlin/Function11;", "left", "top", "right", "bottom", "oldLeft", "oldTop", "oldRight", "oldBottom", "(Landroid/view/View;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function11;)V", "onLongClick", "onMenuItemClick", "Landroid/widget/ActionMenuView;", "Landroid/view/MenuItem;", "item", "(Landroid/widget/ActionMenuView;Lkotlin/coroutines/experimental/CoroutineContext;ZLkotlin/jvm/functions/Function3;)V", "Landroid/widget/Toolbar;", "(Landroid/widget/Toolbar;Lkotlin/coroutines/experimental/CoroutineContext;ZLkotlin/jvm/functions/Function3;)V", "onPrepared", "onQueryTextFocusChange", "(Landroid/widget/SearchView;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function4;)V", "onQueryTextListener", "Lorg/jetbrains/anko/sdk25/coroutines/__SearchView_OnQueryTextListener;", "onRatingBarChange", "Landroid/widget/RatingBar;", "ratingBar", "", "rating", "fromUser", "(Landroid/widget/RatingBar;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function5;)V", "onScroll", "Landroid/widget/NumberPicker;", "scrollState", "(Landroid/widget/NumberPicker;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function4;)V", "onScrollChange", "scrollX", "scrollY", "oldScrollX", "oldScrollY", "(Landroid/view/View;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function7;)V", "onScrollListener", "Landroid/widget/AbsListView;", "Lorg/jetbrains/anko/sdk25/coroutines/__AbsListView_OnScrollListener;", "onSearchClick", "(Landroid/widget/SearchView;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function3;)V", "onSeekBarChangeListener", "Landroid/widget/SeekBar;", "Lorg/jetbrains/anko/sdk25/coroutines/__SeekBar_OnSeekBarChangeListener;", "onSuggestionListener", "Lorg/jetbrains/anko/sdk25/coroutines/__SearchView_OnSuggestionListener;", "onSystemUiVisibilityChange", "visibility", "onTabChanged", "Landroid/widget/TabHost;", "", "tabId", "(Landroid/widget/TabHost;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function3;)V", "onTimeChanged", "Landroid/widget/TimePicker;", "hourOfDay", "minute", "(Landroid/widget/TimePicker;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function5;)V", "onTouch", "onUnhandledInputEvent", "Landroid/media/tv/TvView;", "Landroid/view/InputEvent;", "(Landroid/media/tv/TvView;Lkotlin/coroutines/experimental/CoroutineContext;ZLkotlin/jvm/functions/Function3;)V", "onValueChanged", "picker", "oldVal", "newVal", "(Landroid/widget/NumberPicker;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function5;)V", "onZoomInClick", "Landroid/widget/ZoomControls;", "(Landroid/widget/ZoomControls;Lkotlin/coroutines/experimental/CoroutineContext;Lkotlin/jvm/functions/Function3;)V", "onZoomOutClick", "textChangedListener", "Lorg/jetbrains/anko/sdk25/coroutines/__TextWatcher;", "anko-sdk25-coroutines_release"}, k = 2, mv = {1, 1, 5})
/* compiled from: ListenersWithCoroutines.kt */
public final class Sdk25CoroutinesListenersWithCoroutinesKt {
    public static /* bridge */ /* synthetic */ void onLayoutChange$default(View view, CoroutineContext coroutineContext, Function11 function11, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onLayoutChange(view, coroutineContext, function11);
    }

    public static final void onLayoutChange(View view, CoroutineContext coroutineContext, Function11<? super CoroutineScope, ? super View, ? super Integer, ? super Integer, ? super Integer, ? super Integer, ? super Integer, ? super Integer, ? super Integer, ? super Integer, ? super Continuation<? super Unit>, ? extends Object> function11) {
        Intrinsics.checkParameterIsNotNull(view, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function11, "handler");
        view.addOnLayoutChangeListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onLayoutChange$1(coroutineContext, function11));
    }

    public static /* bridge */ /* synthetic */ void onAttachStateChangeListener$default(View view, CoroutineContext coroutineContext, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onAttachStateChangeListener(view, coroutineContext, function1);
    }

    public static final void onAttachStateChangeListener(View view, CoroutineContext coroutineContext, Function1<? super __View_OnAttachStateChangeListener, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(view, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function1, "init");
        __View_OnAttachStateChangeListener __view_onattachstatechangelistener = new __View_OnAttachStateChangeListener(coroutineContext);
        function1.invoke(__view_onattachstatechangelistener);
        view.addOnAttachStateChangeListener(__view_onattachstatechangelistener);
    }

    public static /* bridge */ /* synthetic */ void textChangedListener$default(TextView textView, CoroutineContext coroutineContext, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        textChangedListener(textView, coroutineContext, function1);
    }

    public static final void textChangedListener(TextView textView, CoroutineContext coroutineContext, Function1<? super __TextWatcher, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(textView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function1, "init");
        __TextWatcher __textwatcher = new __TextWatcher(coroutineContext);
        function1.invoke(__textwatcher);
        textView.addTextChangedListener(__textwatcher);
    }

    public static /* bridge */ /* synthetic */ void onGestureListener$default(GestureOverlayView gestureOverlayView, CoroutineContext coroutineContext, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onGestureListener(gestureOverlayView, coroutineContext, function1);
    }

    public static final void onGestureListener(GestureOverlayView gestureOverlayView, CoroutineContext coroutineContext, Function1<? super __GestureOverlayView_OnGestureListener, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(gestureOverlayView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function1, "init");
        __GestureOverlayView_OnGestureListener __gestureoverlayview_ongesturelistener = new __GestureOverlayView_OnGestureListener(coroutineContext);
        function1.invoke(__gestureoverlayview_ongesturelistener);
        gestureOverlayView.addOnGestureListener(__gestureoverlayview_ongesturelistener);
    }

    public static /* bridge */ /* synthetic */ void onGesturePerformed$default(GestureOverlayView gestureOverlayView, CoroutineContext coroutineContext, Function4 function4, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onGesturePerformed(gestureOverlayView, coroutineContext, function4);
    }

    public static final void onGesturePerformed(GestureOverlayView gestureOverlayView, CoroutineContext coroutineContext, Function4<? super CoroutineScope, ? super GestureOverlayView, ? super Gesture, ? super Continuation<? super Unit>, ? extends Object> function4) {
        Intrinsics.checkParameterIsNotNull(gestureOverlayView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function4, "handler");
        gestureOverlayView.addOnGesturePerformedListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onGesturePerformed$1(coroutineContext, function4));
    }

    public static /* bridge */ /* synthetic */ void onGesturingListener$default(GestureOverlayView gestureOverlayView, CoroutineContext coroutineContext, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onGesturingListener(gestureOverlayView, coroutineContext, function1);
    }

    public static final void onGesturingListener(GestureOverlayView gestureOverlayView, CoroutineContext coroutineContext, Function1<? super __GestureOverlayView_OnGesturingListener, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(gestureOverlayView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function1, "init");
        __GestureOverlayView_OnGesturingListener __gestureoverlayview_ongesturinglistener = new __GestureOverlayView_OnGesturingListener(coroutineContext);
        function1.invoke(__gestureoverlayview_ongesturinglistener);
        gestureOverlayView.addOnGesturingListener(__gestureoverlayview_ongesturinglistener);
    }

    public static /* bridge */ /* synthetic */ void onUnhandledInputEvent$default(TvView tvView, CoroutineContext coroutineContext, boolean z, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        if ((i & 2) != 0) {
            z = false;
        }
        onUnhandledInputEvent(tvView, coroutineContext, z, function3);
    }

    public static final void onUnhandledInputEvent(TvView tvView, CoroutineContext coroutineContext, boolean z, Function3<? super CoroutineScope, ? super InputEvent, ? super Continuation<? super Unit>, ? extends Object> function3) {
        Intrinsics.checkParameterIsNotNull(tvView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function3, "handler");
        tvView.setOnUnhandledInputEventListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onUnhandledInputEvent$1(coroutineContext, function3, z));
    }

    public static /* bridge */ /* synthetic */ void onApplyWindowInsets$default(View view, CoroutineContext coroutineContext, WindowInsets windowInsets, Function4 function4, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onApplyWindowInsets(view, coroutineContext, windowInsets, function4);
    }

    public static final void onApplyWindowInsets(View view, CoroutineContext coroutineContext, WindowInsets windowInsets, Function4<? super CoroutineScope, ? super View, ? super WindowInsets, ? super Continuation<? super Unit>, ? extends Object> function4) {
        Intrinsics.checkParameterIsNotNull(view, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(windowInsets, "returnValue");
        Intrinsics.checkParameterIsNotNull(function4, "handler");
        view.setOnApplyWindowInsetsListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onApplyWindowInsets$1(coroutineContext, function4, windowInsets));
    }

    public static /* bridge */ /* synthetic */ void onClick$default(View view, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onClick(view, coroutineContext, function3);
    }

    public static final void onClick(View view, CoroutineContext coroutineContext, Function3<? super CoroutineScope, ? super View, ? super Continuation<? super Unit>, ? extends Object> function3) {
        Intrinsics.checkParameterIsNotNull(view, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function3, "handler");
        view.setOnClickListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onClick$1(coroutineContext, function3));
    }

    public static /* bridge */ /* synthetic */ void onContextClick$default(View view, CoroutineContext coroutineContext, boolean z, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        if ((i & 2) != 0) {
            z = false;
        }
        onContextClick(view, coroutineContext, z, function3);
    }

    public static final void onContextClick(View view, CoroutineContext coroutineContext, boolean z, Function3<? super CoroutineScope, ? super View, ? super Continuation<? super Unit>, ? extends Object> function3) {
        Intrinsics.checkParameterIsNotNull(view, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function3, "handler");
        view.setOnContextClickListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onContextClick$1(coroutineContext, function3, z));
    }

    public static /* bridge */ /* synthetic */ void onCreateContextMenu$default(View view, CoroutineContext coroutineContext, Function5 function5, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onCreateContextMenu(view, coroutineContext, function5);
    }

    public static final void onCreateContextMenu(View view, CoroutineContext coroutineContext, Function5<? super CoroutineScope, ? super ContextMenu, ? super View, ? super ContextMenu.ContextMenuInfo, ? super Continuation<? super Unit>, ? extends Object> function5) {
        Intrinsics.checkParameterIsNotNull(view, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function5, "handler");
        view.setOnCreateContextMenuListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onCreateContextMenu$1(coroutineContext, function5));
    }

    public static /* bridge */ /* synthetic */ void onDrag$default(View view, CoroutineContext coroutineContext, boolean z, Function4 function4, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        if ((i & 2) != 0) {
            z = false;
        }
        onDrag(view, coroutineContext, z, function4);
    }

    public static final void onDrag(View view, CoroutineContext coroutineContext, boolean z, Function4<? super CoroutineScope, ? super View, ? super DragEvent, ? super Continuation<? super Unit>, ? extends Object> function4) {
        Intrinsics.checkParameterIsNotNull(view, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function4, "handler");
        view.setOnDragListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onDrag$1(coroutineContext, function4, z));
    }

    public static /* bridge */ /* synthetic */ void onFocusChange$default(View view, CoroutineContext coroutineContext, Function4 function4, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onFocusChange(view, coroutineContext, function4);
    }

    public static final void onFocusChange(View view, CoroutineContext coroutineContext, Function4<? super CoroutineScope, ? super View, ? super Boolean, ? super Continuation<? super Unit>, ? extends Object> function4) {
        Intrinsics.checkParameterIsNotNull(view, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function4, "handler");
        view.setOnFocusChangeListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onFocusChange$1(coroutineContext, function4));
    }

    public static /* bridge */ /* synthetic */ void onGenericMotion$default(View view, CoroutineContext coroutineContext, boolean z, Function4 function4, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        if ((i & 2) != 0) {
            z = false;
        }
        onGenericMotion(view, coroutineContext, z, function4);
    }

    public static final void onGenericMotion(View view, CoroutineContext coroutineContext, boolean z, Function4<? super CoroutineScope, ? super View, ? super MotionEvent, ? super Continuation<? super Unit>, ? extends Object> function4) {
        Intrinsics.checkParameterIsNotNull(view, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function4, "handler");
        view.setOnGenericMotionListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onGenericMotion$1(coroutineContext, function4, z));
    }

    public static /* bridge */ /* synthetic */ void onHover$default(View view, CoroutineContext coroutineContext, boolean z, Function4 function4, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        if ((i & 2) != 0) {
            z = false;
        }
        onHover(view, coroutineContext, z, function4);
    }

    public static final void onHover(View view, CoroutineContext coroutineContext, boolean z, Function4<? super CoroutineScope, ? super View, ? super MotionEvent, ? super Continuation<? super Unit>, ? extends Object> function4) {
        Intrinsics.checkParameterIsNotNull(view, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function4, "handler");
        view.setOnHoverListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onHover$1(coroutineContext, function4, z));
    }

    public static /* bridge */ /* synthetic */ void onKey$default(View view, CoroutineContext coroutineContext, boolean z, Function5 function5, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        if ((i & 2) != 0) {
            z = false;
        }
        onKey(view, coroutineContext, z, function5);
    }

    public static final void onKey(View view, CoroutineContext coroutineContext, boolean z, Function5<? super CoroutineScope, ? super View, ? super Integer, ? super KeyEvent, ? super Continuation<? super Unit>, ? extends Object> function5) {
        Intrinsics.checkParameterIsNotNull(view, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function5, "handler");
        view.setOnKeyListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onKey$1(coroutineContext, function5, z));
    }

    public static /* bridge */ /* synthetic */ void onLongClick$default(View view, CoroutineContext coroutineContext, boolean z, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        if ((i & 2) != 0) {
            z = false;
        }
        onLongClick(view, coroutineContext, z, function3);
    }

    public static final void onLongClick(View view, CoroutineContext coroutineContext, boolean z, Function3<? super CoroutineScope, ? super View, ? super Continuation<? super Unit>, ? extends Object> function3) {
        Intrinsics.checkParameterIsNotNull(view, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function3, "handler");
        view.setOnLongClickListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onLongClick$1(coroutineContext, function3, z));
    }

    public static /* bridge */ /* synthetic */ void onScrollChange$default(View view, CoroutineContext coroutineContext, Function7 function7, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onScrollChange(view, coroutineContext, function7);
    }

    public static final void onScrollChange(View view, CoroutineContext coroutineContext, Function7<? super CoroutineScope, ? super View, ? super Integer, ? super Integer, ? super Integer, ? super Integer, ? super Continuation<? super Unit>, ? extends Object> function7) {
        Intrinsics.checkParameterIsNotNull(view, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function7, "handler");
        view.setOnScrollChangeListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onScrollChange$1(coroutineContext, function7));
    }

    public static /* bridge */ /* synthetic */ void onSystemUiVisibilityChange$default(View view, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onSystemUiVisibilityChange(view, coroutineContext, function3);
    }

    public static final void onSystemUiVisibilityChange(View view, CoroutineContext coroutineContext, Function3<? super CoroutineScope, ? super Integer, ? super Continuation<? super Unit>, ? extends Object> function3) {
        Intrinsics.checkParameterIsNotNull(view, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function3, "handler");
        view.setOnSystemUiVisibilityChangeListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onSystemUiVisibilityChange$1(coroutineContext, function3));
    }

    public static /* bridge */ /* synthetic */ void onTouch$default(View view, CoroutineContext coroutineContext, boolean z, Function4 function4, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        if ((i & 2) != 0) {
            z = false;
        }
        onTouch(view, coroutineContext, z, function4);
    }

    public static final void onTouch(View view, CoroutineContext coroutineContext, boolean z, Function4<? super CoroutineScope, ? super View, ? super MotionEvent, ? super Continuation<? super Unit>, ? extends Object> function4) {
        Intrinsics.checkParameterIsNotNull(view, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function4, "handler");
        view.setOnTouchListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onTouch$1(coroutineContext, function4, z));
    }

    public static /* bridge */ /* synthetic */ void onHierarchyChangeListener$default(ViewGroup viewGroup, CoroutineContext coroutineContext, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onHierarchyChangeListener(viewGroup, coroutineContext, function1);
    }

    public static final void onHierarchyChangeListener(ViewGroup viewGroup, CoroutineContext coroutineContext, Function1<? super __ViewGroup_OnHierarchyChangeListener, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(viewGroup, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function1, "init");
        __ViewGroup_OnHierarchyChangeListener __viewgroup_onhierarchychangelistener = new __ViewGroup_OnHierarchyChangeListener(coroutineContext);
        function1.invoke(__viewgroup_onhierarchychangelistener);
        viewGroup.setOnHierarchyChangeListener(__viewgroup_onhierarchychangelistener);
    }

    public static /* bridge */ /* synthetic */ void onInflate$default(ViewStub viewStub, CoroutineContext coroutineContext, Function4 function4, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onInflate(viewStub, coroutineContext, function4);
    }

    public static final void onInflate(ViewStub viewStub, CoroutineContext coroutineContext, Function4<? super CoroutineScope, ? super ViewStub, ? super View, ? super Continuation<? super Unit>, ? extends Object> function4) {
        Intrinsics.checkParameterIsNotNull(viewStub, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function4, "handler");
        viewStub.setOnInflateListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onInflate$1(coroutineContext, function4));
    }

    public static /* bridge */ /* synthetic */ void onScrollListener$default(AbsListView absListView, CoroutineContext coroutineContext, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onScrollListener(absListView, coroutineContext, function1);
    }

    public static final void onScrollListener(AbsListView absListView, CoroutineContext coroutineContext, Function1<? super __AbsListView_OnScrollListener, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(absListView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function1, "init");
        __AbsListView_OnScrollListener __abslistview_onscrolllistener = new __AbsListView_OnScrollListener(coroutineContext);
        function1.invoke(__abslistview_onscrolllistener);
        absListView.setOnScrollListener(__abslistview_onscrolllistener);
    }

    public static /* bridge */ /* synthetic */ void onMenuItemClick$default(ActionMenuView actionMenuView, CoroutineContext coroutineContext, boolean z, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        if ((i & 2) != 0) {
            z = false;
        }
        onMenuItemClick(actionMenuView, coroutineContext, z, (Function3<? super CoroutineScope, ? super MenuItem, ? super Continuation<? super Unit>, ? extends Object>) function3);
    }

    public static final void onMenuItemClick(ActionMenuView actionMenuView, CoroutineContext coroutineContext, boolean z, Function3<? super CoroutineScope, ? super MenuItem, ? super Continuation<? super Unit>, ? extends Object> function3) {
        Intrinsics.checkParameterIsNotNull(actionMenuView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function3, "handler");
        actionMenuView.setOnMenuItemClickListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onMenuItemClick$1(coroutineContext, function3, z));
    }

    public static /* bridge */ /* synthetic */ void onItemClick$default(AdapterView adapterView, CoroutineContext coroutineContext, Function6 function6, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onItemClick(adapterView, coroutineContext, function6);
    }

    public static final void onItemClick(AdapterView<? extends Adapter> adapterView, CoroutineContext coroutineContext, Function6<? super CoroutineScope, ? super AdapterView<?>, ? super View, ? super Integer, ? super Long, ? super Continuation<? super Unit>, ? extends Object> function6) {
        Intrinsics.checkParameterIsNotNull(adapterView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function6, "handler");
        adapterView.setOnItemClickListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onItemClick$1(coroutineContext, function6));
    }

    public static /* bridge */ /* synthetic */ void onItemLongClick$default(AdapterView adapterView, CoroutineContext coroutineContext, boolean z, Function6 function6, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        if ((i & 2) != 0) {
            z = false;
        }
        onItemLongClick(adapterView, coroutineContext, z, function6);
    }

    public static final void onItemLongClick(AdapterView<? extends Adapter> adapterView, CoroutineContext coroutineContext, boolean z, Function6<? super CoroutineScope, ? super AdapterView<?>, ? super View, ? super Integer, ? super Long, ? super Continuation<? super Unit>, ? extends Object> function6) {
        Intrinsics.checkParameterIsNotNull(adapterView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function6, "handler");
        adapterView.setOnItemLongClickListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onItemLongClick$1(coroutineContext, function6, z));
    }

    public static /* bridge */ /* synthetic */ void onItemSelectedListener$default(AdapterView adapterView, CoroutineContext coroutineContext, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onItemSelectedListener(adapterView, coroutineContext, function1);
    }

    public static final void onItemSelectedListener(AdapterView<? extends Adapter> adapterView, CoroutineContext coroutineContext, Function1<? super __AdapterView_OnItemSelectedListener, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(adapterView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function1, "init");
        __AdapterView_OnItemSelectedListener __adapterview_onitemselectedlistener = new __AdapterView_OnItemSelectedListener(coroutineContext);
        function1.invoke(__adapterview_onitemselectedlistener);
        adapterView.setOnItemSelectedListener(__adapterview_onitemselectedlistener);
    }

    public static /* bridge */ /* synthetic */ void onDismiss$default(AutoCompleteTextView autoCompleteTextView, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onDismiss(autoCompleteTextView, coroutineContext, function2);
    }

    public static final void onDismiss(AutoCompleteTextView autoCompleteTextView, CoroutineContext coroutineContext, Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(autoCompleteTextView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function2, "handler");
        autoCompleteTextView.setOnDismissListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onDismiss$1(coroutineContext, function2));
    }

    public static /* bridge */ /* synthetic */ void onDateChange$default(CalendarView calendarView, CoroutineContext coroutineContext, Function6 function6, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onDateChange(calendarView, coroutineContext, function6);
    }

    public static final void onDateChange(CalendarView calendarView, CoroutineContext coroutineContext, Function6<? super CoroutineScope, ? super CalendarView, ? super Integer, ? super Integer, ? super Integer, ? super Continuation<? super Unit>, ? extends Object> function6) {
        Intrinsics.checkParameterIsNotNull(calendarView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function6, "handler");
        calendarView.setOnDateChangeListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onDateChange$1(coroutineContext, function6));
    }

    public static /* bridge */ /* synthetic */ void onChronometerTick$default(Chronometer chronometer, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onChronometerTick(chronometer, coroutineContext, function3);
    }

    public static final void onChronometerTick(Chronometer chronometer, CoroutineContext coroutineContext, Function3<? super CoroutineScope, ? super Chronometer, ? super Continuation<? super Unit>, ? extends Object> function3) {
        Intrinsics.checkParameterIsNotNull(chronometer, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function3, "handler");
        chronometer.setOnChronometerTickListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onChronometerTick$1(coroutineContext, function3));
    }

    public static /* bridge */ /* synthetic */ void onCheckedChange$default(CompoundButton compoundButton, CoroutineContext coroutineContext, Function4 function4, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onCheckedChange(compoundButton, coroutineContext, (Function4<? super CoroutineScope, ? super CompoundButton, ? super Boolean, ? super Continuation<? super Unit>, ? extends Object>) function4);
    }

    public static final void onCheckedChange(CompoundButton compoundButton, CoroutineContext coroutineContext, Function4<? super CoroutineScope, ? super CompoundButton, ? super Boolean, ? super Continuation<? super Unit>, ? extends Object> function4) {
        Intrinsics.checkParameterIsNotNull(compoundButton, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function4, "handler");
        compoundButton.setOnCheckedChangeListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onCheckedChange$1(coroutineContext, function4));
    }

    public static /* bridge */ /* synthetic */ void onChildClick$default(ExpandableListView expandableListView, CoroutineContext coroutineContext, boolean z, Function7 function7, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        if ((i & 2) != 0) {
            z = false;
        }
        onChildClick(expandableListView, coroutineContext, z, function7);
    }

    public static final void onChildClick(ExpandableListView expandableListView, CoroutineContext coroutineContext, boolean z, Function7<? super CoroutineScope, ? super ExpandableListView, ? super View, ? super Integer, ? super Integer, ? super Long, ? super Continuation<? super Unit>, ? extends Object> function7) {
        Intrinsics.checkParameterIsNotNull(expandableListView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function7, "handler");
        expandableListView.setOnChildClickListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onChildClick$1(coroutineContext, function7, z));
    }

    public static /* bridge */ /* synthetic */ void onGroupClick$default(ExpandableListView expandableListView, CoroutineContext coroutineContext, boolean z, Function6 function6, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        if ((i & 2) != 0) {
            z = false;
        }
        onGroupClick(expandableListView, coroutineContext, z, function6);
    }

    public static final void onGroupClick(ExpandableListView expandableListView, CoroutineContext coroutineContext, boolean z, Function6<? super CoroutineScope, ? super ExpandableListView, ? super View, ? super Integer, ? super Long, ? super Continuation<? super Unit>, ? extends Object> function6) {
        Intrinsics.checkParameterIsNotNull(expandableListView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function6, "handler");
        expandableListView.setOnGroupClickListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onGroupClick$1(coroutineContext, function6, z));
    }

    public static /* bridge */ /* synthetic */ void onGroupCollapse$default(ExpandableListView expandableListView, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onGroupCollapse(expandableListView, coroutineContext, function3);
    }

    public static final void onGroupCollapse(ExpandableListView expandableListView, CoroutineContext coroutineContext, Function3<? super CoroutineScope, ? super Integer, ? super Continuation<? super Unit>, ? extends Object> function3) {
        Intrinsics.checkParameterIsNotNull(expandableListView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function3, "handler");
        expandableListView.setOnGroupCollapseListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onGroupCollapse$1(coroutineContext, function3));
    }

    public static /* bridge */ /* synthetic */ void onGroupExpand$default(ExpandableListView expandableListView, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onGroupExpand(expandableListView, coroutineContext, function3);
    }

    public static final void onGroupExpand(ExpandableListView expandableListView, CoroutineContext coroutineContext, Function3<? super CoroutineScope, ? super Integer, ? super Continuation<? super Unit>, ? extends Object> function3) {
        Intrinsics.checkParameterIsNotNull(expandableListView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function3, "handler");
        expandableListView.setOnGroupExpandListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onGroupExpand$1(coroutineContext, function3));
    }

    public static /* bridge */ /* synthetic */ void onScroll$default(NumberPicker numberPicker, CoroutineContext coroutineContext, Function4 function4, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onScroll(numberPicker, coroutineContext, function4);
    }

    public static final void onScroll(NumberPicker numberPicker, CoroutineContext coroutineContext, Function4<? super CoroutineScope, ? super NumberPicker, ? super Integer, ? super Continuation<? super Unit>, ? extends Object> function4) {
        Intrinsics.checkParameterIsNotNull(numberPicker, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function4, "handler");
        numberPicker.setOnScrollListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onScroll$1(coroutineContext, function4));
    }

    public static /* bridge */ /* synthetic */ void onValueChanged$default(NumberPicker numberPicker, CoroutineContext coroutineContext, Function5 function5, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onValueChanged(numberPicker, coroutineContext, function5);
    }

    public static final void onValueChanged(NumberPicker numberPicker, CoroutineContext coroutineContext, Function5<? super CoroutineScope, ? super NumberPicker, ? super Integer, ? super Integer, ? super Continuation<? super Unit>, ? extends Object> function5) {
        Intrinsics.checkParameterIsNotNull(numberPicker, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function5, "handler");
        numberPicker.setOnValueChangedListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onValueChanged$1(coroutineContext, function5));
    }

    public static /* bridge */ /* synthetic */ void onCheckedChange$default(RadioGroup radioGroup, CoroutineContext coroutineContext, Function4 function4, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onCheckedChange(radioGroup, coroutineContext, (Function4<? super CoroutineScope, ? super RadioGroup, ? super Integer, ? super Continuation<? super Unit>, ? extends Object>) function4);
    }

    public static final void onCheckedChange(RadioGroup radioGroup, CoroutineContext coroutineContext, Function4<? super CoroutineScope, ? super RadioGroup, ? super Integer, ? super Continuation<? super Unit>, ? extends Object> function4) {
        Intrinsics.checkParameterIsNotNull(radioGroup, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function4, "handler");
        radioGroup.setOnCheckedChangeListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onCheckedChange$2(coroutineContext, function4));
    }

    public static /* bridge */ /* synthetic */ void onRatingBarChange$default(RatingBar ratingBar, CoroutineContext coroutineContext, Function5 function5, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onRatingBarChange(ratingBar, coroutineContext, function5);
    }

    public static final void onRatingBarChange(RatingBar ratingBar, CoroutineContext coroutineContext, Function5<? super CoroutineScope, ? super RatingBar, ? super Float, ? super Boolean, ? super Continuation<? super Unit>, ? extends Object> function5) {
        Intrinsics.checkParameterIsNotNull(ratingBar, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function5, "handler");
        ratingBar.setOnRatingBarChangeListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onRatingBarChange$1(coroutineContext, function5));
    }

    public static /* bridge */ /* synthetic */ void onClose$default(SearchView searchView, CoroutineContext coroutineContext, boolean z, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        if ((i & 2) != 0) {
            z = false;
        }
        onClose(searchView, coroutineContext, z, function2);
    }

    public static final void onClose(SearchView searchView, CoroutineContext coroutineContext, boolean z, Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(searchView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function2, "handler");
        searchView.setOnCloseListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onClose$1(coroutineContext, function2, z));
    }

    public static /* bridge */ /* synthetic */ void onQueryTextFocusChange$default(SearchView searchView, CoroutineContext coroutineContext, Function4 function4, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onQueryTextFocusChange(searchView, coroutineContext, function4);
    }

    public static final void onQueryTextFocusChange(SearchView searchView, CoroutineContext coroutineContext, Function4<? super CoroutineScope, ? super View, ? super Boolean, ? super Continuation<? super Unit>, ? extends Object> function4) {
        Intrinsics.checkParameterIsNotNull(searchView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function4, "handler");
        searchView.setOnQueryTextFocusChangeListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onQueryTextFocusChange$1(coroutineContext, function4));
    }

    public static /* bridge */ /* synthetic */ void onQueryTextListener$default(SearchView searchView, CoroutineContext coroutineContext, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onQueryTextListener(searchView, coroutineContext, function1);
    }

    public static final void onQueryTextListener(SearchView searchView, CoroutineContext coroutineContext, Function1<? super __SearchView_OnQueryTextListener, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(searchView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function1, "init");
        __SearchView_OnQueryTextListener __searchview_onquerytextlistener = new __SearchView_OnQueryTextListener(coroutineContext);
        function1.invoke(__searchview_onquerytextlistener);
        searchView.setOnQueryTextListener(__searchview_onquerytextlistener);
    }

    public static /* bridge */ /* synthetic */ void onSearchClick$default(SearchView searchView, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onSearchClick(searchView, coroutineContext, function3);
    }

    public static final void onSearchClick(SearchView searchView, CoroutineContext coroutineContext, Function3<? super CoroutineScope, ? super View, ? super Continuation<? super Unit>, ? extends Object> function3) {
        Intrinsics.checkParameterIsNotNull(searchView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function3, "handler");
        searchView.setOnSearchClickListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onSearchClick$1(coroutineContext, function3));
    }

    public static /* bridge */ /* synthetic */ void onSuggestionListener$default(SearchView searchView, CoroutineContext coroutineContext, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onSuggestionListener(searchView, coroutineContext, function1);
    }

    public static final void onSuggestionListener(SearchView searchView, CoroutineContext coroutineContext, Function1<? super __SearchView_OnSuggestionListener, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(searchView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function1, "init");
        __SearchView_OnSuggestionListener __searchview_onsuggestionlistener = new __SearchView_OnSuggestionListener(coroutineContext);
        function1.invoke(__searchview_onsuggestionlistener);
        searchView.setOnSuggestionListener(__searchview_onsuggestionlistener);
    }

    public static /* bridge */ /* synthetic */ void onSeekBarChangeListener$default(SeekBar seekBar, CoroutineContext coroutineContext, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onSeekBarChangeListener(seekBar, coroutineContext, function1);
    }

    public static final void onSeekBarChangeListener(SeekBar seekBar, CoroutineContext coroutineContext, Function1<? super __SeekBar_OnSeekBarChangeListener, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(seekBar, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function1, "init");
        __SeekBar_OnSeekBarChangeListener __seekbar_onseekbarchangelistener = new __SeekBar_OnSeekBarChangeListener(coroutineContext);
        function1.invoke(__seekbar_onseekbarchangelistener);
        seekBar.setOnSeekBarChangeListener(__seekbar_onseekbarchangelistener);
    }

    public static /* bridge */ /* synthetic */ void onDrawerClose$default(SlidingDrawer slidingDrawer, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onDrawerClose(slidingDrawer, coroutineContext, function2);
    }

    public static final void onDrawerClose(SlidingDrawer slidingDrawer, CoroutineContext coroutineContext, Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(slidingDrawer, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function2, "handler");
        slidingDrawer.setOnDrawerCloseListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onDrawerClose$1(coroutineContext, function2));
    }

    public static /* bridge */ /* synthetic */ void onDrawerOpen$default(SlidingDrawer slidingDrawer, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onDrawerOpen(slidingDrawer, coroutineContext, function2);
    }

    public static final void onDrawerOpen(SlidingDrawer slidingDrawer, CoroutineContext coroutineContext, Function2<? super CoroutineScope, ? super Continuation<? super Unit>, ? extends Object> function2) {
        Intrinsics.checkParameterIsNotNull(slidingDrawer, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function2, "handler");
        slidingDrawer.setOnDrawerOpenListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onDrawerOpen$1(coroutineContext, function2));
    }

    public static /* bridge */ /* synthetic */ void onDrawerScrollListener$default(SlidingDrawer slidingDrawer, CoroutineContext coroutineContext, Function1 function1, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onDrawerScrollListener(slidingDrawer, coroutineContext, function1);
    }

    public static final void onDrawerScrollListener(SlidingDrawer slidingDrawer, CoroutineContext coroutineContext, Function1<? super __SlidingDrawer_OnDrawerScrollListener, Unit> function1) {
        Intrinsics.checkParameterIsNotNull(slidingDrawer, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function1, "init");
        __SlidingDrawer_OnDrawerScrollListener __slidingdrawer_ondrawerscrolllistener = new __SlidingDrawer_OnDrawerScrollListener(coroutineContext);
        function1.invoke(__slidingdrawer_ondrawerscrolllistener);
        slidingDrawer.setOnDrawerScrollListener(__slidingdrawer_ondrawerscrolllistener);
    }

    public static /* bridge */ /* synthetic */ void onTabChanged$default(TabHost tabHost, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onTabChanged(tabHost, coroutineContext, function3);
    }

    public static final void onTabChanged(TabHost tabHost, CoroutineContext coroutineContext, Function3<? super CoroutineScope, ? super String, ? super Continuation<? super Unit>, ? extends Object> function3) {
        Intrinsics.checkParameterIsNotNull(tabHost, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function3, "handler");
        tabHost.setOnTabChangedListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onTabChanged$1(coroutineContext, function3));
    }

    public static /* bridge */ /* synthetic */ void onEditorAction$default(TextView textView, CoroutineContext coroutineContext, boolean z, Function5 function5, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        if ((i & 2) != 0) {
            z = false;
        }
        onEditorAction(textView, coroutineContext, z, function5);
    }

    public static final void onEditorAction(TextView textView, CoroutineContext coroutineContext, boolean z, Function5<? super CoroutineScope, ? super TextView, ? super Integer, ? super KeyEvent, ? super Continuation<? super Unit>, ? extends Object> function5) {
        Intrinsics.checkParameterIsNotNull(textView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function5, "handler");
        textView.setOnEditorActionListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onEditorAction$1(coroutineContext, function5, z));
    }

    public static /* bridge */ /* synthetic */ void onTimeChanged$default(TimePicker timePicker, CoroutineContext coroutineContext, Function5 function5, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onTimeChanged(timePicker, coroutineContext, function5);
    }

    public static final void onTimeChanged(TimePicker timePicker, CoroutineContext coroutineContext, Function5<? super CoroutineScope, ? super TimePicker, ? super Integer, ? super Integer, ? super Continuation<? super Unit>, ? extends Object> function5) {
        Intrinsics.checkParameterIsNotNull(timePicker, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function5, "handler");
        timePicker.setOnTimeChangedListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onTimeChanged$1(coroutineContext, function5));
    }

    public static /* bridge */ /* synthetic */ void onMenuItemClick$default(Toolbar toolbar, CoroutineContext coroutineContext, boolean z, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        if ((i & 2) != 0) {
            z = false;
        }
        onMenuItemClick(toolbar, coroutineContext, z, (Function3<? super CoroutineScope, ? super MenuItem, ? super Continuation<? super Unit>, ? extends Object>) function3);
    }

    public static final void onMenuItemClick(Toolbar toolbar, CoroutineContext coroutineContext, boolean z, Function3<? super CoroutineScope, ? super MenuItem, ? super Continuation<? super Unit>, ? extends Object> function3) {
        Intrinsics.checkParameterIsNotNull(toolbar, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function3, "handler");
        toolbar.setOnMenuItemClickListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onMenuItemClick$2(coroutineContext, function3, z));
    }

    public static /* bridge */ /* synthetic */ void onCompletion$default(VideoView videoView, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onCompletion(videoView, coroutineContext, function3);
    }

    public static final void onCompletion(VideoView videoView, CoroutineContext coroutineContext, Function3<? super CoroutineScope, ? super MediaPlayer, ? super Continuation<? super Unit>, ? extends Object> function3) {
        Intrinsics.checkParameterIsNotNull(videoView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function3, "handler");
        videoView.setOnCompletionListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onCompletion$1(coroutineContext, function3));
    }

    public static /* bridge */ /* synthetic */ void onError$default(VideoView videoView, CoroutineContext coroutineContext, boolean z, Function5 function5, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        if ((i & 2) != 0) {
            z = false;
        }
        onError(videoView, coroutineContext, z, function5);
    }

    public static final void onError(VideoView videoView, CoroutineContext coroutineContext, boolean z, Function5<? super CoroutineScope, ? super MediaPlayer, ? super Integer, ? super Integer, ? super Continuation<? super Unit>, ? extends Object> function5) {
        Intrinsics.checkParameterIsNotNull(videoView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function5, "handler");
        videoView.setOnErrorListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onError$1(coroutineContext, function5, z));
    }

    public static /* bridge */ /* synthetic */ void onInfo$default(VideoView videoView, CoroutineContext coroutineContext, boolean z, Function5 function5, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        if ((i & 2) != 0) {
            z = false;
        }
        onInfo(videoView, coroutineContext, z, function5);
    }

    public static final void onInfo(VideoView videoView, CoroutineContext coroutineContext, boolean z, Function5<? super CoroutineScope, ? super MediaPlayer, ? super Integer, ? super Integer, ? super Continuation<? super Unit>, ? extends Object> function5) {
        Intrinsics.checkParameterIsNotNull(videoView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function5, "handler");
        videoView.setOnInfoListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onInfo$1(coroutineContext, function5, z));
    }

    public static /* bridge */ /* synthetic */ void onPrepared$default(VideoView videoView, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onPrepared(videoView, coroutineContext, function3);
    }

    public static final void onPrepared(VideoView videoView, CoroutineContext coroutineContext, Function3<? super CoroutineScope, ? super MediaPlayer, ? super Continuation<? super Unit>, ? extends Object> function3) {
        Intrinsics.checkParameterIsNotNull(videoView, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function3, "handler");
        videoView.setOnPreparedListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onPrepared$1(coroutineContext, function3));
    }

    public static /* bridge */ /* synthetic */ void onZoomInClick$default(ZoomControls zoomControls, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onZoomInClick(zoomControls, coroutineContext, function3);
    }

    public static final void onZoomInClick(ZoomControls zoomControls, CoroutineContext coroutineContext, Function3<? super CoroutineScope, ? super View, ? super Continuation<? super Unit>, ? extends Object> function3) {
        Intrinsics.checkParameterIsNotNull(zoomControls, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function3, "handler");
        zoomControls.setOnZoomInClickListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onZoomInClick$1(coroutineContext, function3));
    }

    public static /* bridge */ /* synthetic */ void onZoomOutClick$default(ZoomControls zoomControls, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = HandlerContextKt.getUI();
        }
        onZoomOutClick(zoomControls, coroutineContext, function3);
    }

    public static final void onZoomOutClick(ZoomControls zoomControls, CoroutineContext coroutineContext, Function3<? super CoroutineScope, ? super View, ? super Continuation<? super Unit>, ? extends Object> function3) {
        Intrinsics.checkParameterIsNotNull(zoomControls, "$receiver");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "context");
        Intrinsics.checkParameterIsNotNull(function3, "handler");
        zoomControls.setOnZoomOutClickListener(new Sdk25CoroutinesListenersWithCoroutinesKt$onZoomOutClick$1(coroutineContext, function3));
    }
}
