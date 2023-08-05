package com.besome.sketch.editor.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.beans.ProjectResourceBean;
import com.besome.sketch.beans.ViewBean;
import com.besome.sketch.beans.WidgetCollectionBean;
import com.besome.sketch.editor.view.item.ItemHorizontalScrollView;
import com.besome.sketch.editor.view.item.ItemVerticalScrollView;
import com.besome.sketch.editor.view.palette.IconAdView;
import com.besome.sketch.editor.view.palette.IconBase;
import com.besome.sketch.editor.view.palette.IconLinearHorizontal;
import com.besome.sketch.editor.view.palette.IconLinearVertical;
import com.besome.sketch.editor.view.palette.IconMapView;
import com.besome.sketch.editor.view.palette.PaletteFavorite;
import com.besome.sketch.editor.view.palette.PaletteWidget;
import com.sketchware.remod.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import a.a.a.DB;
import a.a.a.GB;
import a.a.a.Iw;
import a.a.a.Op;
import a.a.a.Rp;
import a.a.a._x;
import a.a.a.aB;
import a.a.a.ay;
import a.a.a.bB;
import a.a.a.cC;
import a.a.a.cy;
import a.a.a.jC;
import a.a.a.oB;
import a.a.a.sy;
import a.a.a.uy;
import a.a.a.wB;
import a.a.a.wq;
import a.a.a.xB;
import mod.hey.studios.editor.view.IdGenerator;
import mod.hey.studios.util.Helper;
import mod.hey.studios.util.ProjectFile;

public class ViewEditor extends RelativeLayout implements View.OnClickListener, View.OnTouchListener {

    private ObjectAnimator A;
    private boolean B = false;
    private boolean C = false;
    private boolean D = false;
    private final int[] G = new int[2];
    private sy H;
    private int I = 50;
    private int J = 30;
    private boolean K;
    private cy L;
    private Iw M;
    private _x N;
    private ay O;
    public boolean P = true;
    private ProjectFileBean projectFileBean;
    private boolean S = true;
    private boolean T = false;
    private LinearLayout U;
    private b V;
    private b W;
    private String a;
    private LinearLayout aa;
    private String b;
    private int ca;
    private boolean da = true;
    private int[] e = new int[20];
    private final Runnable ea = this::e;
    private float f = 0;
    private int g;
    private int h;
    public PaletteWidget i;
    private PaletteFavorite j;
    private LinearLayout k;
    private TextView l;
    private ImageView m;
    private LinearLayout n;
    private ViewPane p;
    private Vibrator q;
    private View r = null;
    private final Handler s = new Handler();
    private boolean t = false;
    private float u = 0;
    private float v = 0;
    private int w = 0;
    private ViewDummy x;
    private ImageView y;
    private ObjectAnimator z;

    enum PaletteGroup {
        BASIC,
        FAVORITE
    }

    public ViewEditor(Context context) {
        super(context);
        initialize(context);
    }

    private void animateUpDown() {
        z = ObjectAnimator.ofFloat(y, "TranslationY", 0.0f);
        z.setDuration(500L);
        z.setInterpolator(new DecelerateInterpolator());
        A = ObjectAnimator.ofFloat(y, "TranslationY", y.getHeight());
        A.setDuration(300L);
        A.setInterpolator(new DecelerateInterpolator());
        B = true;
    }

    private void g() {
        V = new b(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1.0f;
        V.setLayoutParams(layoutParams);
        V.a(PaletteGroup.BASIC);
        V.setSelected(true);
        V.setOnClickListener(v -> {
            showPaletteWidget();
            V.animate().scaleX(1).scaleY(1).alpha(1).start();
            W.animate().scaleX(0.9f).scaleY(0.9f).alpha(0.6f).start();
            V.setSelected(true);
            W.setSelected(false);
        });
        W = new b(getContext());
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams2.weight = 1.0f;
        W.setLayoutParams(layoutParams2);
        W.a(PaletteGroup.FAVORITE);
        W.setSelected(false);
        W.animate().scaleX(0.9f).scaleY(0.9f).alpha(0.6f).start();
        W.setOnClickListener(v -> {
            showPaletteFavorite();
            V.animate().scaleX(0.9f).scaleY(0.9f).alpha(0.6f).start();
            W.animate().scaleX(1).scaleY(1).alpha(1).start();
            V.setSelected(false);
            W.setSelected(true);
        });
        U.addView(V);
        U.addView(W);
    }

    public ProjectFileBean getProjectFile() {
        return projectFileBean;
    }

    public void h() {
        p.setResourceManager(jC.d(a));
    }

    public void i() {
        if (H != null) {
            H.setSelection(false);
            H = null;
        }
        if (L != null) L.a(false, "");
    }

    public void j() {
        p.d();
        l();
        i();
    }

    public void k() {
        p.e();
    }

    public void l() {
        e = new int[99];
    }

    private void m() {
        if (M != null) M.a(b, H.getBean());
    }

    private void showPaletteFavorite() {
        i.setVisibility(View.GONE);
        j.setVisibility(View.VISIBLE);
    }

    private void showPaletteWidget() {
        i.setVisibility(View.VISIBLE);
        j.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        int id2 = view.getId();
        if (id2 == R.id.btn_editproperties) {
            m();
        }
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (P) a();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        _x _xVar;
        String str;
        int actionMasked = motionEvent.getActionMasked();
        if (motionEvent.getPointerId(motionEvent.getActionIndex()) > 0) {
            return true;
        }
        if (view == p) {
            if (actionMasked == MotionEvent.ACTION_DOWN) {
                i();
                r = null;
            }
            return true;
        } else if (actionMasked == MotionEvent.ACTION_DOWN) {
            t = false;
            u = motionEvent.getRawX();
            v = motionEvent.getRawY();
            r = view;
            if ((view instanceof sy) && ((sy) view).getFixed()) {
                return true;
            }
            if (b(view) && (_xVar = N) != null) {
                _xVar.b();
            }
            s.postDelayed(ea, ViewConfiguration.getLongPressTimeout() / 2);
            return true;
        } else if (actionMasked != MotionEvent.ACTION_UP) {
            if (actionMasked != MotionEvent.ACTION_MOVE) {
                if (actionMasked == MotionEvent.ACTION_CANCEL || actionMasked == MotionEvent.ACTION_SCROLL) {
                    i.setScrollEnabled(true);
                    j.setScrollEnabled(true);
                    if (N != null) {
                        N.d();
                    }
                    b(false);
                    x.setDummyVisibility(View.GONE);
                    p.b();
                    s.removeCallbacks(ea);
                    t = false;
                    return true;
                }
                return true;
            } else if (!t) {
                if (Math.abs(u - motionEvent.getRawX()) >= w || Math.abs(v - motionEvent.getRawY()) >= w) {
                    r = null;
                    s.removeCallbacks(ea);
                    return true;
                }
                return true;
            } else {
                s.removeCallbacks(ea);
                x.a(view, motionEvent.getRawX(), motionEvent.getRawY(), u, v);
                if (a(motionEvent.getRawX(), motionEvent.getRawY())) {
                    x.setAllow(true);
                    updateDeleteIcon(true);
                    return true;
                }
                if (D) updateDeleteIcon(false);
                if (b(motionEvent.getRawX(), motionEvent.getRawY())) {
                    x.setAllow(true);
                    boolean isNotIcon = !a(r);
                    int i = isNotIcon ? r.getWidth() : (r instanceof IconLinearHorizontal ?
                            ViewGroup.LayoutParams.MATCH_PARENT : I);
                    int i2 = isNotIcon ? r.getHeight() : (r instanceof IconLinearVertical ?
                            ViewGroup.LayoutParams.MATCH_PARENT : J);
                    p.a((int) motionEvent.getRawX(), (int) motionEvent.getRawY(), i, i2);
                } else {
                    x.setAllow(false);
                    p.a(true);
                }
                return true;
            }
        } else if (!t) {
            if (r instanceof sy sy) {
                a(sy, true);
            }
            if (N != null) {
                N.d();
            }
            x.setDummyVisibility(View.GONE);
            r = null;
            p.b();
            s.removeCallbacks(ea);
            return true;
        } else {
            lol:
            if (x.getAllow()) {
                if (D) {
                    if (r instanceof sy sy) {
                        ArrayList<ViewBean> b2 = jC.a(a).b(b, sy.getBean());
                        for (int size = b2.size() - 1; size >= 0; size--) {
                            jC.a(a).a(projectFileBean, b2.get(size));
                        }
                        b(b2, true);
                        break lol;
                    }
                }
                if (D) {
                    if (r instanceof uy uy) {
                        deleteWidgetFromCollection(uy.getName());
                        break lol;
                    }
                }
                p.a(false);
                if (r instanceof uy uyVar) {
                    ArrayList<ViewBean> arrayList = new ArrayList<>();
                    oB oBVar = new oB();
                    boolean z = false;
                    for (int i3 = 0; i3 < uyVar.getData().size(); i3++) {
                        ViewBean viewBean = uyVar.getData().get(i3);
                        if (c(viewBean)) {
                            arrayList.add(viewBean.clone());
                            String str2 = viewBean.layout.backgroundResource;
                            String str3 = viewBean.image.resName;
                            if (!jC.d(a).l(str2) && Op.g().b(str2)) {
                                ProjectResourceBean a2 = Op.g().a(str2);
                                try {
                                    oBVar.a(wq.a() + File.separator + "image" + File.separator + "data" + File.separator + a2.resFullName, wq.g() + File.separator + a + File.separator + a2.resFullName);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                jC.d(a).b.add(a2);
                                z = true;
                            }
                            if (!jC.d(a).l(str3) && Op.g().b(str3)) {
                                ProjectResourceBean a3 = Op.g().a(str3);
                                try {
                                    oBVar.a(wq.a() + File.separator + "image" + File.separator + "data" + File.separator + a3.resFullName, wq.g() + File.separator + a + File.separator + a3.resFullName);
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                }
                                jC.d(a).b.add(a3);
                                z = true;
                            }
                        }
                    }
                    if (z) {
                        bB.a(getContext(), xB.b().a(getContext(), R.string.view_widget_favorites_image_auto_added), bB.TOAST_NORMAL).show();
                    }
                    if (arrayList.size() > 0) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        p.a(arrayList.get(0), (int) motionEvent.getRawX(), (int) motionEvent.getRawY());
                        for (ViewBean next : arrayList) {
                            if (jC.a(a).h(projectFileBean.getXmlName(), next.id)) {
                                hashMap.put(next.id, a(next.type));
                            } else {
                                hashMap.put(next.id, next.id);
                            }
                            next.id = hashMap.get(next.id);
                            if (arrayList.indexOf(next) != 0 && (str = next.parent) != null && str.length() > 0) {
                                next.parent = hashMap.get(next.parent);
                            }
                            jC.a(a).a(b, next);
                        }
                        a(a(arrayList, true), true);
                    }
                } else if (r instanceof IconBase icon) {
                    ViewBean bean = icon.getBean();
                    bean.id = IdGenerator.getId(this, bean.type, bean);
                    p.a(bean, (int) motionEvent.getRawX(), (int) motionEvent.getRawY());
                    jC.a(a).a(b, bean);
                    if (bean.type == 3 && projectFileBean.fileType == ProjectFileBean.PROJECT_FILE_TYPE_ACTIVITY) {
                        jC.a(a).a(projectFileBean.getJavaName(), 1, bean.type, bean.id, "onClick");
                    }
                    a(a(bean, true), true);
                } else if (r instanceof sy sy) {
                    ViewBean bean = sy.getBean();
                    p.a(bean, (int) motionEvent.getRawX(), (int) motionEvent.getRawY());
                    a(b(bean, true), true);
                }
            } else {
                if (r instanceof sy) {
                    r.setVisibility(View.VISIBLE);
                }
            }
            i.setScrollEnabled(true);
            j.setScrollEnabled(true);
            if (N != null) {
                N.d();
            }
            b(false);
            x.setDummyVisibility(View.GONE);
            r = null;
            p.b();
            s.removeCallbacks(ea);
            t = false;
            return true;
        }
    }

    public void setFavoriteData(ArrayList<WidgetCollectionBean> arrayList) {
        clearCollectionWidget();
        for (WidgetCollectionBean next : arrayList) {
            addFavoriteViews(next.name, next.widgets);
        }
    }

    public void setIsAdLoaded(boolean z) {
        da = z;
    }

    public void setOnDraggingListener(_x _xVar) {
        N = _xVar;
    }

    public void setOnHistoryChangeListener(ay ayVar) {
        O = ayVar;
    }

    public void setOnPropertyClickListener(Iw iw) {
        M = iw;
    }

    public void setOnWidgetSelectedListener(cy cyVar) {
        L = cyVar;
    }

    public void setPaletteLayoutVisible(int i) {
        this.i.setLayoutVisible(i);
    }

    public void setScreenType(int i) {
        if (i == 1) {
            ca = 0;
        } else {
            ca = 1;
        }
    }

    private void initialize(Context context) {
        wB.a(context, this, R.layout.view_editor);
        i = findViewById(R.id.palette_widget);
        j = findViewById(R.id.palette_favorite);
        x = findViewById(R.id.dummy);
        y = findViewById(R.id.icon_delete);
        FrameLayout f1 = findViewById(R.id.shape);
        U = findViewById(R.id.palette_group);
        g();
        findViewById(R.id.btn_editproperties).setOnClickListener(this);
        findViewById(R.id.img_close).setOnClickListener(this);
        f = wB.a(context, 1.0f);
        I = (int) (I * f);
        J = (int) (J * f);
        g = getResources().getDisplayMetrics().widthPixels;
        h = getResources().getDisplayMetrics().heightPixels;
        aa = new LinearLayout(context);
        aa.setOrientation(LinearLayout.VERTICAL);
        aa.setGravity(Gravity.CENTER);
        aa.setLayoutParams(new FrameLayout.LayoutParams(g, h));
        f1.addView(aa);
        k = new LinearLayout(context);
        k.setBackgroundColor(0xff0084c2);
        k.setOrientation(LinearLayout.HORIZONTAL);
        k.setGravity(Gravity.CENTER_VERTICAL);
        k.setLayoutParams(new FrameLayout.LayoutParams(g, (int) (f * 25.0f)));
        l = new TextView(context);
        l.setTextColor(Color.WHITE);
        l.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        l.setPadding((int) (f * 8.0f), 0, 0, 0);
        l.setGravity(Gravity.CENTER_VERTICAL);
        k.addView(l);
        m = new ImageView(context);
        m.setImageResource(R.drawable.phone_bg_top);
        m.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        m.setScaleType(ImageView.ScaleType.FIT_END);
        k.addView(m);
        f1.addView(k);
        n = new LinearLayout(context);
        n.setBackgroundColor(0xff008dcd);
        n.setOrientation(LinearLayout.HORIZONTAL);
        n.setGravity(Gravity.CENTER_VERTICAL);
        n.setLayoutParams(new FrameLayout.LayoutParams(g, (int) (f * 48.0f)));
        TextView o = new TextView(context);
        o.setTextColor(Color.WHITE);
        o.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        o.setPadding((int) (f * 16.0f), 0, 0, 0);
        o.setGravity(Gravity.CENTER_VERTICAL);
        o.setTextSize(15.0f);
        o.setText("Toolbar");
        o.setTypeface(null, Typeface.BOLD);
        n.addView(o);
        f1.addView(n);
        p = new ViewPane(getContext());
        p.setLayoutParams(new FrameLayout.LayoutParams(g, h));
        f1.addView(p);
        p.setOnTouchListener(this);
        q = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        K = new DB(context, "P12").a("P12I0", true);
        w = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void b(ArrayList<ViewBean> arrayList, boolean z) {
        if (z) {
            cC.c(a).b(projectFileBean.getXmlName(), arrayList);
            if (O != null) {
                O.a();
            }
        }
        int size = arrayList.size();
        while (true) {
            size--;
            if (size < 0) {
                return;
            }
            d(arrayList.get(size));
        }
    }

    private void clearCollectionWidget() {
        j.a();
    }

    public void d() {
        i.a();
        i.b();
    }

    public sy e(ViewBean viewBean) {
        sy g = p.g(viewBean);
        L.a();
        L.a(viewBean.id);
        return g;
    }

    class b extends LinearLayout implements View.OnClickListener {

        private ImageView c;

        public b(Context context) {
            super(context);
            initialize(context);
        }

        private void initialize(Context context) {
            wB.a(context, this, R.layout.palette_group_item);
            c = findViewById(R.id.img_group);
        }

        @Override
        public void onClick(View view) {
        }

        public void a(PaletteGroup group) {
            c.setImageResource(group == PaletteGroup.BASIC ?
                    R.drawable.selector_palette_tab_ic_sketchware :
                    R.drawable.selector_palette_tab_ic_bookmark);
            setOnClickListener(this);
        }
    }

    private boolean c(ViewBean viewBean) {
        int i;
        int i2 = projectFileBean.fileType;
        if (i2 == 1) {
            int i3 = viewBean.type;
            if (i3 != 0 && i3 != 4 && i3 != 5 && i3 != 3 && i3 != 6 && i3 != 11 && i3 != 13 && i3 != 14 && i3 == 8) {
                return true;
            }
        } else if (i2 == 2 && (i = viewBean.type) != 0 && i != 12 && i != 2 && i != 4 && i != 5 && i != 3 && i != 6 && i != 11 && i != 13 && i != 14 && i == 8) {
            return true;
        }
        return true;
    }

    public void d(ViewBean viewBean) {
        p.f(viewBean);
    }

    private void e() {
        if (r == null) {
            return;
        }
        if (a(r)) {
            if (r instanceof uy uyVar) {
                boolean isAdViewUsed = false;
                for (ViewBean view : uyVar.getData()) {
                    if (view.type == ViewBean.VIEW_TYPE_WIDGET_ADVIEW) {
                        isAdViewUsed = true;
                        break;
                    }
                }
                if (isAdViewUsed && !N.a()) {
                    bB.b(getContext(), xB.b().a(getContext(), R.string.design_library_guide_setup_first), bB.TOAST_NORMAL).show();
                    return;
                }

                boolean isMapViewUsed = false;
                for (ViewBean view : uyVar.getData()) {
                    if (view.type == ViewBean.VIEW_TYPE_WIDGET_MAPVIEW) {
                        isMapViewUsed = true;
                        break;
                    }
                }
                if (isMapViewUsed && !N.c()) {
                    bB.b(getContext(), xB.b().a(getContext(), R.string.design_library_guide_setup_first), bB.TOAST_NORMAL).show();
                    return;
                }
            } else if ((r instanceof IconAdView) && !N.a()) {
                bB.b(getContext(), xB.b().a(getContext(), R.string.design_library_guide_setup_first), bB.TOAST_NORMAL).show();
                return;
            } else if ((r instanceof IconMapView) && !N.c()) {
                bB.b(getContext(), xB.b().a(getContext(), R.string.design_library_guide_setup_first), bB.TOAST_NORMAL).show();
                return;
            }
        }
        i.setScrollEnabled(false);
        j.setScrollEnabled(false);
        if (N != null) N.b();
        if (K) q.vibrate(100L);
        t = true;
        x.b(r);
        x.bringToFront();
        i();
        x.a(r, u, v, u, v);
        x.a(G);
        if (a(r)) {
            if (r instanceof uy) {
                b(true);
                p.e(null);
            } else {
                b(false);
                p.e(null);
            }
        } else {
            r.setVisibility(View.GONE);
            b(true);
            p.e(((sy) r).getBean());
        }
        if (b(u, v)) {
            x.setAllow(true);
            boolean isNotIcon = !a(r);
            int i = isNotIcon ? r.getWidth() : (r instanceof IconLinearHorizontal ?
                    ViewGroup.LayoutParams.MATCH_PARENT : I);
            int i2 = isNotIcon ? r.getHeight() : (r instanceof IconLinearVertical ?
                    ViewGroup.LayoutParams.MATCH_PARENT : J);
            p.a((int) u, (int) v, i, i2);
            return;
        }
        x.setAllow(false);
        p.a(true);
    }

    public sy b(ViewBean viewBean, boolean z) {
        if (z) {
            cC.c(a).b(projectFileBean.getXmlName(), viewBean);
            if (O != null) {
                O.a();
            }
        }
        return p.d(viewBean);
    }

    public sy b(ViewBean viewBean) {
        View b2 = p.b(viewBean);
        p.a(b2);
        String b3 = wq.b(viewBean.type);
        if (viewBean.id.indexOf(b3) == 0 && viewBean.id.length() > b3.length()) {
            try {
                int intValue = Integer.valueOf(viewBean.id.substring(b3.length())).intValue();
                if (e[viewBean.type] < intValue) {
                    e[viewBean.type] = intValue;
                }
            } catch (NumberFormatException ignored) {
            }
        }
        b2.setOnTouchListener(this);
        return (sy) b2;
    }

    private boolean b(View view) {
        for (ViewParent parent = view.getParent(); parent != null && parent != this; parent = parent.getParent()) {
            if ((parent instanceof ItemVerticalScrollView) || (parent instanceof ItemHorizontalScrollView)) {
                return true;
            }
        }
        return false;
    }

    private boolean b(float f, float f2) {
        int[] iArr = new int[2];
        p.getLocationOnScreen(iArr);
        return f > ((float) iArr[0]) && f < ((float) iArr[0]) + (((float) p.getWidth()) * p.getScaleX()) && f2 > ((float) iArr[1]) && f2 < ((float) iArr[1]) + (((float) p.getHeight()) * p.getScaleY());
    }

    public ViewEditor(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(context);
    }

    private void deleteWidgetFromCollection(String str) {
        aB aBVar = new aB((Activity) getContext());
        aBVar.b(xB.b().a(getContext(), R.string.view_widget_favorites_delete_title));
        aBVar.a(R.drawable.high_priority_96_red);
        aBVar.a(xB.b().a(getContext(), R.string.view_widget_favorites_delete_message));
        aBVar.b(xB.b().a(getContext(), R.string.common_word_delete), v -> {
            Rp.h().a(str, true);
            setFavoriteData(Rp.h().f());
            aBVar.dismiss();
        });
        aBVar.a(xB.b().a(getContext(), R.string.common_word_cancel), Helper.getDialogDismissListener(aBVar));
        aBVar.show();
    }

    private void cancelAnimation() {
        if (z.isRunning()) z.cancel();
        if (A.isRunning()) A.cancel();
    }

    private void setPreviewColors(String str) {
        k.setBackgroundColor(ProjectFile.getColor(str, "color_primary_dark"));
        m.setBackgroundColor(ProjectFile.getColor(str, "color_primary_dark"));
        n.setBackgroundColor(ProjectFile.getColor(str, "color_primary"));
    }

    private void b(boolean z) {
        y.bringToFront();
        if (!B) {
            animateUpDown();
        }
        if (C == z) {
            return;
        }
        C = z;
        cancelAnimation();
        if (z) {
            this.z.start();
        } else {
            A.start();
        }
    }

    public void a(String str, ProjectFileBean projectFileBean) {
        a = str;
        setPreviewColors(str);
        this.projectFileBean = projectFileBean;
        b = projectFileBean.getXmlName();
        if (projectFileBean.fileType == ProjectFileBean.PROJECT_FILE_TYPE_DRAWER) {
            l.setText(projectFileBean.fileName.substring(1));
        } else {
            l.setText(projectFileBean.getXmlName());
        }
        k();
        if (projectFileBean.fileType == ProjectFileBean.PROJECT_FILE_TYPE_ACTIVITY) {
            S = projectFileBean.hasActivityOption(ProjectFileBean.OPTION_ACTIVITY_TOOLBAR);
            T = projectFileBean.hasActivityOption(ProjectFileBean.OPTION_ACTIVITY_FULLSCREEN);
            if (projectFileBean.hasActivityOption(ProjectFileBean.OPTION_ACTIVITY_FAB)) {
                a(jC.a(str).h(projectFileBean.getXmlName()));
            }
        } else {
            S = false;
            T = false;
        }
        P = true;
        if (p != null) {
            p.setScId(str);
        }
    }

    public void a(String str) {
        sy syVar;
        sy a2 = p.a(str);
        if (a2 == null || (syVar = H) == a2) {
            return;
        }
        if (syVar != null) {
            syVar.setSelection(false);
        }
        a2.setSelection(true);
        H = a2;
    }

    private void a() {
        n.setVisibility(S ? View.VISIBLE : View.GONE);
        k.setVisibility(T ? View.GONE : View.VISIBLE);

        p.setVisibility(View.VISIBLE);
        g = getResources().getDisplayMetrics().widthPixels;
        h = getResources().getDisplayMetrics().heightPixels;
        boolean var2 = g > h;
        int var4 = (int) (f * (!var2 ? 12.0F : 24.0F));
        int var5 = (int) (f * (!var2 ? 20.0F : 10.0F));
        int var6 = GB.f(getContext());
        int var7 = GB.a(getContext());
        int var9 = g - (int) (120.0F * f);
        int var8 = h - var6 - var7 - (int) (f * 48.0F) - (int) (f * 48.0F);
        if (ca == 0) {
            if (da) {
                var8 -= (int) (f * 56.0F);
            }
        }

        float var11 = Math.min((float) var9 / (float) g, (float) var8 / (float) h);
        float var3 = Math.min((float) (var9 - var4 * 2) / (float) g, (float) (var8 - var5 * 2) / (float) h);
        if (!var2) {
            aa.setBackgroundResource(R.drawable.new_view_pane_background_port);
        } else {
            aa.setBackgroundResource(R.drawable.new_view_pane_background_land);
        }

        aa.setLayoutParams(new FrameLayout.LayoutParams(g, h));
        aa.setScaleX(var11);
        aa.setScaleY(var11);
        aa.setX((float) -((int) (((float) g - (float) g * var11) / 2.0F)));
        aa.setY((float) -((int) (((float) h - (float) h * var11) / 2.0F)));
        int var10 = var4 - (int) (((float) g - (float) g * var3) / 2.0F);
        int var13 = var5;
        if (k.getVisibility() == View.VISIBLE) {
            k.setLayoutParams(new FrameLayout.LayoutParams(g, var6));
            k.setScaleX(var3);
            k.setScaleY(var3);
            var11 = (float) var6;
            float var12 = var11 * var3;
            k.setX((float) var10);
            k.setY((float) (var5 - (int) ((var11 - var12) / 2.0F)));
            var13 = var5 + (int) var12;
        }

        var8 = var13;
        if (n.getVisibility() == View.VISIBLE) {
            n.setLayoutParams(new FrameLayout.LayoutParams(g, var7));
            n.setScaleX(var3);
            n.setScaleY(var3);
            float var12 = (float) var7;
            var11 = var12 * var3;
            n.setX((float) var10);
            n.setY((float) (var13 - (int) ((var12 - var11) / 2.0F)));
            var8 = var13 + (int) var11;
        }

        var13 = h;
        if (k.getVisibility() == View.VISIBLE) {
            var13 = h - var6;
        }

        var5 = var13;
        if (n.getVisibility() == View.VISIBLE) {
            var5 = var13 - var7;
        }

        p.setLayoutParams(new FrameLayout.LayoutParams(g, var5));
        p.setScaleX(var3);
        p.setScaleY(var3);
        var11 = (float) var5;
        p.setX((float) var10);
        p.setY((float) (var8 - (int) ((var11 - var3 * var11) / 2.0F)));
        P = false;
    }

    public void a(PaletteWidget.a aVar, String str) {
        View a2 = i.a(aVar, str);
        a2.setClickable(true);
        a2.setOnTouchListener(this);
    }

    public void extraWidgetLayout(String str, String str2) {
        View extraWidgetLayout = i.extraWidgetLayout(str, str2);
        extraWidgetLayout.setClickable(true);
        extraWidgetLayout.setOnTouchListener(this);
    }

    public void a(PaletteWidget.b bVar, String str, String str2, String str3) {
        View a2 = i.a(bVar, str, str2, str3);
        a2.setClickable(true);
        a2.setOnTouchListener(this);
    }

    public void extraWidget(String str, String str2, String str3) {
        View extraWidget = i.extraWidget(str, str2, str3);
        extraWidget.setClickable(true);
        extraWidget.setOnTouchListener(this);
    }

    private boolean a(View view) {
        return view instanceof IconBase;
    }

    private void addFavoriteViews(String str, ArrayList<ViewBean> arrayList) {
        View a2 = j.a(str, arrayList);
        a2.setClickable(true);
        a2.setOnTouchListener(this);
    }

    public final String a(int i) {
        String b2 = wq.b(i);
        StringBuilder sb = new StringBuilder();
        sb.append(b2);
        int i2 = e[i] + 1;
        e[i] = i2;
        sb.append(i2);
        String sb2 = sb.toString();
        ArrayList<ViewBean> d = jC.a(a).d(b);
        while (true) {
            boolean isIdUsed = false;
            for (ViewBean view : d) {
                if (sb2.equals(view.id)) {
                    isIdUsed = true;
                    break;
                }
            }
            if (!isIdUsed) {
                return sb2;
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append(b2);
            int i3 = e[i] + 1;
            e[i] = i3;
            sb3.append(i3);
            sb2 = sb3.toString();
        }
    }

    public sy a(ArrayList<ViewBean> arrayList, boolean z) {
        if (z) {
            cC.c(a).a(projectFileBean.getXmlName(), arrayList);
            if (O != null) {
                O.a();
            }
        }
        sy syVar = null;
        for (ViewBean view : arrayList) {
            if (arrayList.indexOf(view) == 0) {
                syVar = b(view);
            } else {
                b(view);
            }
        }
        return syVar;
    }

    public sy a(ViewBean viewBean, boolean z) {
        if (z) {
            cC.c(a).a(projectFileBean.getXmlName(), viewBean);
            if (O != null) {
                O.a();
            }
        }
        return b(viewBean);
    }

    public void a(ArrayList<ViewBean> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return;
        }
        for (ViewBean view : arrayList) {
            b(view);
        }
    }

    public void a(ViewBean viewBean) {
        p.a(viewBean).setOnTouchListener(this);
    }

    public void a(sy syVar, boolean z) {
        if (H != null) {
            H.setSelection(false);
        }
        H = syVar;
        H.setSelection(true);
        if (L != null) {
            L.a(z, H.getBean().id);
        }
    }

    private boolean a(float x, float y) {
        int[] locationOnScreen = new int[2];
        this.y.getLocationOnScreen(locationOnScreen);
        return x > ((float) locationOnScreen[0]) && x < ((float) (locationOnScreen[0] + this.y.getWidth())) && y > ((float) locationOnScreen[1]) && y < ((float) (locationOnScreen[1] + this.y.getHeight()));
    }

    private void updateDeleteIcon(boolean z) {
        if (D == z) return;
        D = z;
        y.setImageResource(D ? R.drawable.icon_delete_active : R.drawable.icon_delete);
    }
}
