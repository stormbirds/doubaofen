package android.support.constraint.solver.widgets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConstraintWidgetGroup {
    public List<ConstraintWidget> mConstrainedGroup;
    public final int[] mGroupDimensions = {this.mGroupWidth, this.mGroupHeight};
    int mGroupHeight = -1;
    int mGroupWidth = -1;
    public boolean mSkipSolver = false;
    List<ConstraintWidget> mStartHorizontalWidgets = new ArrayList();
    List<ConstraintWidget> mStartVerticalWidgets = new ArrayList();
    List<ConstraintWidget> mUnresolvedWidgets = new ArrayList();
    HashSet<ConstraintWidget> mWidgetsToSetHorizontal = new HashSet<>();
    HashSet<ConstraintWidget> mWidgetsToSetVertical = new HashSet<>();
    List<ConstraintWidget> mWidgetsToSolve = new ArrayList();

    ConstraintWidgetGroup(List<ConstraintWidget> list) {
        this.mConstrainedGroup = list;
    }

    ConstraintWidgetGroup(List<ConstraintWidget> list, boolean z) {
        this.mConstrainedGroup = list;
        this.mSkipSolver = z;
    }

    public List<ConstraintWidget> getStartWidgets(int i) {
        if (i == 0) {
            return this.mStartHorizontalWidgets;
        }
        if (i == 1) {
            return this.mStartVerticalWidgets;
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public Set<ConstraintWidget> getWidgetsToSet(int i) {
        if (i == 0) {
            return this.mWidgetsToSetHorizontal;
        }
        if (i == 1) {
            return this.mWidgetsToSetVertical;
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void addWidgetsToSet(ConstraintWidget constraintWidget, int i) {
        if (i == 0) {
            this.mWidgetsToSetHorizontal.add(constraintWidget);
        } else if (i == 1) {
            this.mWidgetsToSetVertical.add(constraintWidget);
        }
    }

    /* access modifiers changed from: package-private */
    public List<ConstraintWidget> getWidgetsToSolve() {
        if (!this.mWidgetsToSolve.isEmpty()) {
            return this.mWidgetsToSolve;
        }
        int size = this.mConstrainedGroup.size();
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = this.mConstrainedGroup.get(i);
            if (!constraintWidget.mOptimizerMeasurable) {
                getWidgetsToSolveTraversal((ArrayList) this.mWidgetsToSolve, constraintWidget);
            }
        }
        this.mUnresolvedWidgets.clear();
        this.mUnresolvedWidgets.addAll(this.mConstrainedGroup);
        this.mUnresolvedWidgets.removeAll(this.mWidgetsToSolve);
        return this.mWidgetsToSolve;
    }

    private void getWidgetsToSolveTraversal(ArrayList<ConstraintWidget> arrayList, ConstraintWidget constraintWidget) {
        if (!constraintWidget.mGroupsToSolver) {
            arrayList.add(constraintWidget);
            constraintWidget.mGroupsToSolver = true;
            if (!constraintWidget.isFullyResolved()) {
                if (constraintWidget instanceof Helper) {
                    Helper helper = (Helper) constraintWidget;
                    int i = helper.mWidgetsCount;
                    for (int i2 = 0; i2 < i; i2++) {
                        getWidgetsToSolveTraversal(arrayList, helper.mWidgets[i2]);
                    }
                }
                for (ConstraintAnchor constraintAnchor : constraintWidget.mListAnchors) {
                    ConstraintAnchor constraintAnchor2 = constraintAnchor.mTarget;
                    if (constraintAnchor2 != null) {
                        ConstraintWidget constraintWidget2 = constraintAnchor2.mOwner;
                        if (!(constraintAnchor2 == null || constraintWidget2 == constraintWidget.getParent())) {
                            getWidgetsToSolveTraversal(arrayList, constraintWidget2);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void updateUnresolvedWidgets() {
        int size = this.mUnresolvedWidgets.size();
        for (int i = 0; i < size; i++) {
            updateResolvedDimension(this.mUnresolvedWidgets.get(i));
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x004e  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0056  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0071  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0095  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateResolvedDimension(android.support.constraint.solver.widgets.ConstraintWidget r7) {
        /*
            r6 = this;
            boolean r0 = r7.mOptimizerMeasurable
            if (r0 == 0) goto L_0x00f1
            boolean r0 = r7.isFullyResolved()
            if (r0 == 0) goto L_0x000b
            return
        L_0x000b:
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r7.mRight
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r0.mTarget
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x0015
            r0 = 1
            goto L_0x0016
        L_0x0015:
            r0 = 0
        L_0x0016:
            if (r0 == 0) goto L_0x001d
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r7.mRight
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.mTarget
            goto L_0x0021
        L_0x001d:
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r7.mLeft
            android.support.constraint.solver.widgets.ConstraintAnchor r3 = r3.mTarget
        L_0x0021:
            if (r3 == 0) goto L_0x004b
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r3.mOwner
            boolean r4 = r4.mOptimizerMeasured
            if (r4 != 0) goto L_0x002e
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r3.mOwner
            r6.updateResolvedDimension(r4)
        L_0x002e:
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r4 = r3.mType
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r5 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.RIGHT
            if (r4 != r5) goto L_0x0040
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r3.mOwner
            int r4 = r4.mX
            android.support.constraint.solver.widgets.ConstraintWidget r3 = r3.mOwner
            int r3 = r3.getWidth()
            int r3 = r3 + r4
            goto L_0x004c
        L_0x0040:
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r4 = r3.mType
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r5 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.LEFT
            if (r4 != r5) goto L_0x004b
            android.support.constraint.solver.widgets.ConstraintWidget r3 = r3.mOwner
            int r3 = r3.mX
            goto L_0x004c
        L_0x004b:
            r3 = 0
        L_0x004c:
            if (r0 == 0) goto L_0x0056
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r7.mRight
            int r0 = r0.getMargin()
            int r3 = r3 - r0
            goto L_0x0062
        L_0x0056:
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r7.mLeft
            int r0 = r0.getMargin()
            int r4 = r7.getWidth()
            int r0 = r0 + r4
            int r3 = r3 + r0
        L_0x0062:
            int r0 = r7.getWidth()
            int r0 = r3 - r0
            r7.setHorizontalDimension(r0, r3)
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r7.mBaseline
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r0.mTarget
            if (r0 == 0) goto L_0x0095
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r7.mBaseline
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r0.mTarget
            android.support.constraint.solver.widgets.ConstraintWidget r1 = r0.mOwner
            boolean r1 = r1.mOptimizerMeasured
            if (r1 != 0) goto L_0x0080
            android.support.constraint.solver.widgets.ConstraintWidget r1 = r0.mOwner
            r6.updateResolvedDimension(r1)
        L_0x0080:
            android.support.constraint.solver.widgets.ConstraintWidget r1 = r0.mOwner
            int r1 = r1.mY
            android.support.constraint.solver.widgets.ConstraintWidget r0 = r0.mOwner
            int r0 = r0.mBaselineDistance
            int r1 = r1 + r0
            int r0 = r7.mBaselineDistance
            int r1 = r1 - r0
            int r0 = r7.mHeight
            int r0 = r0 + r1
            r7.setVerticalDimension(r1, r0)
            r7.mOptimizerMeasured = r2
            return
        L_0x0095:
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r7.mBottom
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r0.mTarget
            if (r0 == 0) goto L_0x009c
            r1 = 1
        L_0x009c:
            if (r1 == 0) goto L_0x00a3
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r7.mBottom
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r0.mTarget
            goto L_0x00a7
        L_0x00a3:
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r7.mTop
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r0.mTarget
        L_0x00a7:
            if (r0 == 0) goto L_0x00d0
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r0.mOwner
            boolean r4 = r4.mOptimizerMeasured
            if (r4 != 0) goto L_0x00b4
            android.support.constraint.solver.widgets.ConstraintWidget r4 = r0.mOwner
            r6.updateResolvedDimension(r4)
        L_0x00b4:
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r4 = r0.mType
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r5 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.BOTTOM
            if (r4 != r5) goto L_0x00c6
            android.support.constraint.solver.widgets.ConstraintWidget r3 = r0.mOwner
            int r3 = r3.mY
            android.support.constraint.solver.widgets.ConstraintWidget r0 = r0.mOwner
            int r0 = r0.getHeight()
            int r3 = r3 + r0
            goto L_0x00d0
        L_0x00c6:
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r4 = r0.mType
            android.support.constraint.solver.widgets.ConstraintAnchor$Type r5 = android.support.constraint.solver.widgets.ConstraintAnchor.Type.TOP
            if (r4 != r5) goto L_0x00d0
            android.support.constraint.solver.widgets.ConstraintWidget r0 = r0.mOwner
            int r3 = r0.mY
        L_0x00d0:
            if (r1 == 0) goto L_0x00da
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r7.mBottom
            int r0 = r0.getMargin()
            int r3 = r3 - r0
            goto L_0x00e6
        L_0x00da:
            android.support.constraint.solver.widgets.ConstraintAnchor r0 = r7.mTop
            int r0 = r0.getMargin()
            int r1 = r7.getHeight()
            int r0 = r0 + r1
            int r3 = r3 + r0
        L_0x00e6:
            int r0 = r7.getHeight()
            int r0 = r3 - r0
            r7.setVerticalDimension(r0, r3)
            r7.mOptimizerMeasured = r2
        L_0x00f1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.widgets.ConstraintWidgetGroup.updateResolvedDimension(android.support.constraint.solver.widgets.ConstraintWidget):void");
    }
}
