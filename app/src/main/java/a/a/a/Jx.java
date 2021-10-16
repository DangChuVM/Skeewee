package a.a.a;

import android.util.Pair;

import com.besome.sketch.beans.BlockBean;
import com.besome.sketch.beans.ComponentBean;
import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.beans.ViewBean;

import java.util.ArrayList;
import java.util.Map;

import mod.agus.jcoderz.beans.ViewBeans;
import mod.agus.jcoderz.editor.manage.library.locallibrary.ManageLocalLibrary;
import mod.agus.jcoderz.handle.code.CodeResult;
import mod.hey.studios.build.BuildSettings;
import mod.hey.studios.project.ProjectSettings;
import mod.hilal.saif.android_manifest.AndroidManifestInjector;
import mod.hilal.saif.blocks.CommandBlock;
import mod.hilal.saif.events.LogicHandler;
import mod.jbk.util.LogUtil;
import mod.w3wide.control.logic.PermissionManager;
import mod.w3wide.control.logic.SourceHandler;

public class Jx {

    public static String a = "\r\n";
    private final ProjectSettings settings;
    private final PermissionManager permissionManager;
    private final SourceHandler sourceHandler;
    /**
     * Currently generating class' package name,
     * e.g. com.jbk.internal.demo
     */
    public String b;
    public ProjectFileBean c;
    public eC d;
    public Hx e;
    public jq f;
    /**
     * Imports to be added to the currently generating class,
     * e.g. {"com.google.firebase.FirebaseApp"}
     */
    public ArrayList<String> g = new ArrayList<>();
    /**
     * Fields with static initializer that added Components need,
     * e.g. {"private Timer _timer = new Timer();"}
     */
    public ArrayList<String> h = new ArrayList<>();
    /**
     * Fields of the currently generating class,
     * e.g. {"private FloatingActionBar _fab;"}
     */
    public ArrayList<String> i = new ArrayList<>();
    public ArrayList<String> j = new ArrayList<>();
    /**
     * Views declared in this generated class
     */
    public ArrayList<String> k = new ArrayList<>();
    /**
     * Field declarations from components. Can include static initializer, but doesn't have to.
     */
    public ArrayList<String> l = new ArrayList<>();
    /**
     * Statements to be added to initialize(Bundle),
     * e.g. {"_drawer.addDrawerListener(_toggle);"}
     */
    public ArrayList<String> m = new ArrayList<>();
    public ManageLocalLibrary mll;
    /**
     * Component initializer lines which get added to <code>_initialize(Bundle)</code>
     */
    public ArrayList<String> n = new ArrayList<>();
    /**
     * Content of <code>_initializeLogic()</code>
     */
    public String o = "";
    /**
     * Code of More Blocks that have been created
     */
    public ArrayList<String> p = new ArrayList<>();
    /**
     * Code of inner Adapter classes, used for Widgets like ListView or RecyclerView
     */
    public ArrayList<String> q = new ArrayList<>();
    /**
     * (Currently) filled with request code constants for FilePicker components
     */
    public ArrayList<String> r = new ArrayList<>();
    public Lx.AccessModifier s;

    public Jx(jq jqVar, ProjectFileBean projectFileBean, eC eCVar) {
        b = jqVar.a;
        c = projectFileBean;
        d = eCVar;
        f = jqVar;
        mll = new ManageLocalLibrary(eCVar.a);
        settings = new ProjectSettings(eCVar.a);
        permissionManager = new PermissionManager(eCVar.a, projectFileBean.getJavaName());
        sourceHandler = new SourceHandler(eCVar.a, projectFileBean.getJavaName());
    }

    private void extraVariables() {
        i.addAll(sourceHandler.customVariables());
        m.addAll(sourceHandler.viewBinds());
    }

    /**
     * Removes duplicate imports in {@link Jx#g}.
     */
    private void removeExtraImports() {
        ArrayList<String> newImports = new ArrayList<>();
        for (String value : g) {
            if (!newImports.contains(value) && !value.trim().isEmpty()) {
                newImports.add(value);
            }
        }
        g = newImports;
    }

    /**
     * @return Import to be added to the currently generating class
     * (includes import of default launcher activity)
     */
    private String getLauncherActivity(String packageName) {
        String theImport = "";

        String activityName = ProjectFileBean.getActivityName(AndroidManifestInjector.getLauncherActivity(d.a));
        if (!activityName.equals("MainActivity")) {
            theImport = "import " + packageName + "." + activityName + ";" + a;
        }

        return theImport;
    }

    /**
     * @return Generated Java code of the current View (not Widget)
     */
    public String a() {
        boolean isDialogFragment = c.fileName.contains("_dialog_fragment");
        boolean isBottomDialogFragment = c.fileName.contains("_bottomdialog_fragment");
        boolean isFragment = c.fileName.contains("_fragment");

        extraVariables();
        handleAppCompat();
        i();
        g();
        e();
        d();
        c();
        h();
        f();
        j();

        StringBuilder sb = new StringBuilder(8192);
        sb.append("package ").append(b).append(";").append(a)
                .append(a);
        if (c.getActivityName().equals("MainActivity")) {
            sb.append(getLauncherActivity(b));
        }
        removeExtraImports();

        for (String anImport : g) {
            sb.append("import ").append(anImport).append(";").append(a);
        }
        if (f.g) {
            sb.append("import androidx.fragment.app.Fragment;").append(a);
            sb.append("import androidx.fragment.app.FragmentManager;").append(a);
            sb.append("import androidx.fragment.app.DialogFragment;").append(a);
            if (isBottomDialogFragment) {
                sb.append("import com.google.android.material.bottomsheet.BottomSheetDialogFragment;").append(a);
            }
        } else {
            sb.append("import android.app.Fragment;").append(a);
            sb.append("import android.app.FragmentManager;").append(a);
            sb.append("import android.app.DialogFragment;").append(a);
        }
        if (permissionManager.hasNewPermission() || f.a(c.getActivityName()).a()) {
            if (f.g) {
                sb.append("import androidx.core.content.ContextCompat;").append(a);
                sb.append("import androidx.core.app.ActivityCompat;").append(a);
            }
            sb.append("import android.Manifest;").append(a);
            sb.append("import android.content.pm.PackageManager;").append(a);
        }
        String importsAddedByImportBlocks = LogicHandler.imports(e.b());
        if (!importsAddedByImportBlocks.isEmpty()) {
            sb.append(importsAddedByImportBlocks).append(a);
        }
        sb.append(a);

        sb.append("public class ").append(c.getActivityName()).append(" extends ");
        if (f.g) {
            if (isBottomDialogFragment) {
                sb.append("BottomSheetDialogFragment");
            } else if (isDialogFragment) {
                sb.append("DialogFragment");
            } else if (isFragment) {
                sb.append("Fragment");
            } else {
                sb.append("AppCompatActivity");
            }
        } else {
            if (isBottomDialogFragment) {
                sb.append("/* Enable AppCompat to use it */");
            } else if (isDialogFragment) {
                sb.append("DialogFragment");
            } else if (isFragment) {
                sb.append("Fragment");
            } else {
                sb.append("Activity");
            }
        }
        sb.append(" {").append(a);

        boolean activityHasFields = false;

        for (String constant : r) {
            if (constant.length() > 0) {
                activityHasFields = true;
                sb.append(a);
                sb.append(constant);
            }
        }

        if (h.size() > 0) {
            if (activityHasFields) sb.append(a);
            activityHasFields = true;

            for (String componentFieldDeclaration : h) {
                if (componentFieldDeclaration.length() > 0) {
                    sb.append(a);
                    sb.append(componentFieldDeclaration);
                }
            }
        }

        if (i.size() > 0) {
            if (activityHasFields) sb.append(a);
            activityHasFields = true;

            for (String field : i) {
                if (field.length() > 0) {
                    sb.append(a);
                    sb.append(field);
                }
            }
        }

        if (j.size() > 0) {
            if (activityHasFields) sb.append(a);
            activityHasFields = true;

            for (String value : j) {
                if (value.length() > 0) {
                    sb.append(a);
                    sb.append(value);
                }
            }
        }

        if (k.size() > 0) {
            if (activityHasFields) sb.append(a);
            activityHasFields = true;

            for (String viewDeclaration : k) {
                if (viewDeclaration.length() > 0) {
                    sb.append(a);
                    sb.append(viewDeclaration);
                }
            }
        }

        if (l.size() > 0) {
            if (activityHasFields) sb.append(a);
            activityHasFields = true;

            for (String componentFieldDeclaration : l) {
                if (componentFieldDeclaration.length() > 0) {
                    sb.append(a);
                    sb.append(componentFieldDeclaration);
                }
            }
        }

        if (activityHasFields) {
            sb.append(a);
        }
        sb.append(a);
        if (isFragment) {
            if (f.g) {
                sb.append("@NonNull").append(a);
                sb.append("@Override").append(a);
                sb.append("public View onCreateView(@NonNull LayoutInflater _inflater, " +
                        "@Nullable ViewGroup _container, @Nullable Bundle _savedInstanceState) {").append(a);
            } else {
                sb.append("@Override").append(a);
                sb.append("public View onCreateView(LayoutInflater _inflater, ViewGroup _container, " +
                        "Bundle _savedInstanceState) {").append(a);
            }
            sb.append("View _view = _inflater.inflate(R.layout.").append(c.fileName).append(", _container, false);").append(a);
            sb.append("initialize(_savedInstanceState, _view);");
        } else {
            sb.append("@Override").append(a);
            sb.append("protected void onCreate(Bundle _savedInstanceState) {").append(a);
            sb.append("super.onCreate(_savedInstanceState);").append(a);
            sb.append("setContentView(R.layout.").append(c.fileName).append(");").append(a);
            sb.append("initialize(_savedInstanceState);");
        }
        sb.append(a);
        if (f.h) {
            if (isFragment) {
                sb.append("com.google.firebase.FirebaseApp.initializeApp(getContext());");
            } else {
                sb.append("com.google.firebase.FirebaseApp.initializeApp(this);");
            }
            sb.append(a);
        }

        if (f.l && !isFragment) {
            if (!f.h) {
                sb.append(a);
            }
            sb.append("com.google.android.gms.ads.MobileAds.initialize(this);");
            sb.append(a);
            if (h.contains(Lx.d("InterstitialAd"))) {
	            sb.append("_ad_unit_id = \"").append(f.f ? "ca-app-pub-3940256099942544/1033173712" : f.s).append("\";");
            }

            if (f.f) {
                StringBuilder testDevicesListCode = new StringBuilder("List<String> testDeviceIds = Arrays.asList(");
                ArrayList<String> testDevices = f.t;
                for (int j = 0, testDevicesSize = testDevices.size(); j < testDevicesSize; j++) {
                    String testDeviceId = testDevices.get(j);

                    testDevicesListCode.append("\"").append(testDeviceId).append("\"");
                    if (j != testDevicesSize - 1) {
                        testDevicesListCode.append(", ");
                    }
                }
                testDevicesListCode.append(");").append(a);

                sb.append(a);
                sb.append(testDevicesListCode);
                sb.append("com.google.android.gms.ads.MobileAds.setRequestConfiguration(new com.google.android.gms.ads.RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build());");
            }

            sb.append(a);
        }

        if (!isFragment) {
            // Adds initializeLogic() call too, don't worry
            sb.append(permissionManager.writePermission(f.g, f.a(c.getActivityName()).c));
        } else {
            sb.append("initializeLogic();").append(a)
                    .append("return _view;").append(a);
        }
        sb.append("}").append(a);

        if (permissionManager.hasPermission && !isFragment) {
            sb.append(a);
            sb.append("@Override").append(a);
            sb.append("public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {").append(a);
            sb.append("super.onRequestPermissionsResult(requestCode, permissions, grantResults);").append(a);
            sb.append("if (requestCode == 1000) {").append(a);
            sb.append("initializeLogic();").append(a);
            sb.append("}").append(a);
            sb.append("}").append(a);
        }
        sb.append(a);
        if (isFragment) {
            sb.append("private void initialize(Bundle _savedInstanceState, View _view) {");
        } else {
            sb.append("private void initialize(Bundle _savedInstanceState) {");
        }
        sb.append(sourceHandler.initializeLogic(f, c.getActivityName()));

        for (String value : m) {
            if (value.length() > 0) {
                sb.append(a);
                sb.append(value);
            }
        }

        for (String componentInitializer : n) {
            if (componentInitializer.length() > 0) {
                sb.append(a);
                sb.append(componentInitializer);
            }
        }

        String hxG = e.g();
        if (hxG.length() > 0) {
            sb.append(a);
            sb.append(a);
            sb.append(hxG);
        }

        String hxC = e.c();
        if (hxC.length() > 0) {
            sb.append(a);
            sb.append(a);
            sb.append(hxC);
        }

        String hxD = e.d();
        if (hxD.length() > 0) {
            sb.append(a);
            sb.append(a);
            sb.append(hxD);
        }

        String hxF = e.f();
        if (hxF.length() > 0) {
            sb.append(a);
            sb.append(a);
            sb.append(hxF);
        }

        sb.append(a);
        sb.append("}").append(a);
        sb.append(a);
        sb.append("private void initializeLogic() {").append(a);
        if (o.length() > 0) {
            sb.append(o).append(a);
        }
        sb.append("}").append(a);

        String agusComponentsOnActivityResultCode = CodeResult.c(f.x);
        String onActivityResultLogic = sourceHandler.activityResult(f, c.getActivityName());
        String onActivityResultSwitchLogic = e.a();
        if (!agusComponentsOnActivityResultCode.isEmpty() || !onActivityResultLogic.isEmpty() || !onActivityResultSwitchLogic.isEmpty()) {
            sb.append(a);
            sb.append("@Override").append(a);
            if (isFragment) {
                sb.append("public");
            } else {
                sb.append("protected");
            }
            sb.append(" void onActivityResult(int _requestCode, int _resultCode, Intent _data) {").append(a);
            sb.append(agusComponentsOnActivityResultCode);
            sb.append("super.onActivityResult(_requestCode, _resultCode, _data);").append(a);
            sb.append(onActivityResultLogic).append(a);
            sb.append("switch (_requestCode) {").append(a);
            sb.append(onActivityResultSwitchLogic).append(a);
            sb.append("default:").append(a);
            sb.append("break;").append(a);
            sb.append("}").append(a);
            sb.append("}").append(a);
        }

        if (c.hasActivityOption(ProjectFileBean.OPTION_ACTIVITY_DRAWER)) {
            e.a("onBackPressed", "DrawerLayout", "_drawer");
        }

        ArrayList<ViewBean> beans = d.d(c.getXmlName());
        for (ViewBean next : beans) {
            if (next.type == ViewBean.VIEW_TYPE_WIDGET_MAPVIEW) {
                e.a("onStart", "MapView", next.id);
                e.a("onResume", "MapView", next.id);
                e.a("onPause", "MapView", next.id);
                e.a("onStop", "MapView", next.id);
                e.a("onDestroy", "MapView", next.id);
            }
            if (next.type == ViewBean.VIEW_TYPE_WIDGET_ADVIEW) {
            	e.a("onResume", "AdView", next.id);
                e.a("onPause", "AdView", next.id);
                e.a("onDestroy", "AdView", next.id);
            }
        }
        if (e.k.length() > 0) {
            sb.append(a);
            sb.append(e.k).append(a);
        }
        if (e.l.length() > 0) {
            sb.append(a);
            sb.append(e.l);
            sb.append(a);
        }

        String base = LogicHandler.base(e.b());
        if (base.length() > 0) {
            sb.append(a);
            sb.append(base);
        }

        for (String moreBlocksCode : p) {
            sb.append(a);
            sb.append(moreBlocksCode).append(a);
        }

        sb.append(a);
        for (int i1 = 0, qSize = q.size(); i1 < qSize; i1++) {
            String adapterCode = q.get(i1);

            if (base.contains("public CharSequence onTabLayoutNewTabAdded(int _position) {")
                    || !adapterCode.contains("return onTabLayoutNewTabAdded(pos);")) {
                sb.append(adapterCode);
            } else {
                sb.append(adapterCode.replace("return onTabLayoutNewTabAdded(pos);",
                        "// Use the Activity Event (onTabLayoutNewTabAdded) in order to use this method\r\n" +
                                "return \"page \" + String.valueOf(pos);"));
            }
            if (i1 != qSize - 1) {
                sb.append(a);
            }
        }
        if (!isFragment && !settings.getValue(ProjectSettings.SETTING_DISABLE_OLD_METHODS, BuildSettings.SETTING_GENERIC_VALUE_FALSE)
                .equals(BuildSettings.SETTING_GENERIC_VALUE_TRUE)) {
            deprecatedMethods(sb);
        }
        sb.append("}").append(a);
        String code = sb.toString();

        if (isFragment) {
            code = code.replaceAll("getApplicationContext\\(\\)", "getContext().getApplicationContext()")
                    .replaceAll("getBaseContext\\(\\)", "getActivity().getBaseContext()")
                    .replaceAll("\\(ClipboardManager\\) getSystemService", "(ClipboardManager) getContext().getSystemService")
                    .replaceAll("\\(Vibrator\\) getSystemService", "(Vibrator) getContext().getSystemService")
                    .replaceAll("\\(SensorManager\\) getSystemService", "(SensorManager) getContext().getSystemService")
                    .replaceAll("Typeface.createFromAsset\\(getAssets\\(\\)", "Typeface.createFromAsset(getContext().getAssets()")
                    .replaceAll("= getAssets\\(\\).open", "= getContext().getAssets().open")
                    .replaceAll("getSharedPreferences", "getContext().getSharedPreferences")
                    .replaceAll("AlertDialog.Builder\\(this\\);", "AlertDialog.Builder(getContext());")
                    .replaceAll("SpeechRecognizer.createSpeechRecognizer\\(this\\);", "SpeechRecognizer.createSpeechRecognizer(getContext());")
                    .replaceAll("new RequestNetwork\\(this\\);", "new RequestNetwork((Activity) getContext());")
                    .replaceAll("new BluetoothConnect\\(this\\);", "new BluetoothConnect((Activity) getContext());")
                    .replaceAll("MobileAds.getRewardedVideoAdInstance\\(this\\);", "MobileAds.getRewardedVideoAdInstance(getContext());")
                    .replaceAll("runOnUiThread\\(new", "getActivity().runOnUiThread(new")
                    .replaceAll(".setLayoutManager\\(new LinearLayoutManager\\(this", ".setLayoutManager(new LinearLayoutManager(getContext()")
                    .replaceAll("getLayoutInflater\\(\\)", "getActivity().getLayoutInflater()");
        }
        if (f.g) {
            code = code.replaceAll("getFragmentManager", "getSupportFragmentManager");
        }

        LogUtil.log("Jx", "Dump of La/a/a/Jx;:",
                "Logging a dump of La/a/a/Jx; over multiple lines because of length.",
                LogUtil.dump(this));
        return CommandBlock.CB(Lx.j(code));
    }

    public final String a(int i2, String str) {
        String b2 = mq.b(i2);
        a(mq.c(b2));
        return Lx.a(b2, str, Lx.AccessModifier.PRIVATE);
    }

    public final String a(ComponentBean componentBean) {
        String typeName = mq.a(componentBean.type);
        a(mq.c(typeName));
        return Lx.a(typeName, componentBean.componentId, Lx.AccessModifier.PRIVATE, componentBean.param1, componentBean.param2, componentBean.param3);
    }

    public final String a(ViewBean viewBean) {
        String replaceAll = viewBean.convert.replaceAll("\\w*\\..*\\.", "");
        if (replaceAll.equals("")) {
            replaceAll = viewBean.getClassInfo().a();
        }
        a(mq.c(replaceAll));
        return Lx.a(replaceAll, "_drawer_" + viewBean.id, Lx.AccessModifier.PRIVATE);
    }

    public final void addImport(String str) {
        if (!g.contains(str)) {
            g.add(str);
        }
    }

    public final void deprecatedMethods(StringBuilder sb) {
        sb.append(a)
                .append("@Deprecated").append(a)
                .append("public void showMessage(String _s) {").append(a)
                .append("Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();").append(a)
                .append("}").append(a)
                .append(a)
                .append("@Deprecated").append(a)
                .append("public int getLocationX(View _v) {").append(a)
                .append("int _location[] = new int[2];").append(a)
                .append("_v.getLocationInWindow(_location);").append(a)
                .append("return _location[0];").append(a)
                .append("}").append(a)
                .append(a)
                .append("@Deprecated").append(a)
                .append("public int getLocationY(View _v) {").append(a)
                .append("int _location[] = new int[2];").append(a)
                .append("_v.getLocationInWindow(_location);").append(a)
                .append("return _location[1];").append(a)
                .append("}").append(a)
                .append(a)
                .append("@Deprecated").append(a)
                .append("public int getRandom(int _min, int _max) {").append(a)
                .append("Random random = new Random();").append(a)
                .append("return random.nextInt(_max - _min + 1) + _min;").append(a)
                .append("}").append(a)
                .append(a)
                .append("@Deprecated").append(a)
                .append("public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {").append(a)
                .append("ArrayList<Double> _result = new ArrayList<Double>();").append(a)
                .append("SparseBooleanArray _arr = _list.getCheckedItemPositions();").append(a)
                .append("for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {").append(a)
                .append("if (_arr.valueAt(_iIdx))").append(a)
                .append("_result.add((double)_arr.keyAt(_iIdx));").append(a)
                .append("}").append(a)
                .append("return _result;").append(a)
                .append("}").append(a)
                .append(a)
                .append("@Deprecated").append(a)
                .append("public float getDip(int _input) {").append(a)
                .append("return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());").append(a)
                .append("}").append(a)
                .append(a)
                .append("@Deprecated").append(a)
                .append("public int getDisplayWidthPixels() {").append(a)
                .append("return getResources().getDisplayMetrics().widthPixels;").append(a)
                .append("}").append(a)
                .append(a)
                .append("@Deprecated").append(a)
                .append("public int getDisplayHeightPixels() {").append(a)
                .append("return getResources().getDisplayMetrics().heightPixels;").append(a)
                .append("}").append(a);
    }

    /**
     * Adds imports to {@link Jx#g}.
     */
    public final void a(ArrayList<String> imports) {
        if (imports != null) {
            for (String value : imports) {
                addImport(value);
            }
        }
    }

    /**
     * @return Definition line for a Variable
     */
    public final String b(int variableType, String name) {
        String variableNameId = mq.c(variableType);
        a(mq.c(variableNameId));
        return Lx.a(variableNameId, name, Lx.AccessModifier.PRIVATE);
    }

    /**
     * @see Lx#b(String, String, String...)
     */
    public final String b(ComponentBean componentBean) {
        return Lx.b(mq.a(componentBean.type), componentBean.componentId, componentBean.param1, componentBean.param2, componentBean.param3);
    }

    public final String b(ViewBean viewBean) {
        String replaceAll = viewBean.convert.replaceAll("\\w*\\..*\\.", "");
        if (replaceAll.equals("")) {
            replaceAll = viewBean.getClassInfo().a();
        }
        a(mq.c(replaceAll));
        return Lx.a(replaceAll, viewBean.id, Lx.AccessModifier.PRIVATE);
    }

    private void handleAppCompat() {
        if (f.g) {
            addImport("androidx.appcompat.app.AppCompatActivity");
            addImport("androidx.annotation.*");
        } else {
            addImport("android.app.Activity");
        }
        if (f.g) {
            if (c.hasActivityOption(ProjectFileBean.OPTION_ACTIVITY_TOOLBAR) && !c.fileName.contains("_fragment")) {
                addImport("androidx.appcompat.widget.Toolbar");
                addImport("androidx.coordinatorlayout.widget.CoordinatorLayout");
                addImport("com.google.android.material.appbar.AppBarLayout");

                i.add("private Toolbar _toolbar;");
                i.add("private AppBarLayout _app_bar;");
                i.add("private CoordinatorLayout _coordinator;");
                m.add(
                        "_app_bar = findViewById(R.id._app_bar);" + a +
                                "_coordinator = findViewById(R.id._coordinator);" + a +
                                "_toolbar = findViewById(R.id._toolbar);" + a +
                                "setSupportActionBar(_toolbar);" + a +
                                "getSupportActionBar().setDisplayHomeAsUpEnabled(true);" + a +
                                "getSupportActionBar().setHomeButtonEnabled(true);" + a +
                                "_toolbar.setNavigationOnClickListener(new View.OnClickListener() {" + a +
                                "@Override" + a +
                                "public void onClick(View _v) {" + a +
                                "onBackPressed();" + a +
                                "}" + a +
                                "});"
                );
            }
            if (c.hasActivityOption(ProjectFileBean.OPTION_ACTIVITY_FAB)) {
                addImport("com.google.android.material.floatingactionbutton.FloatingActionButton");

                i.add("private FloatingActionButton _fab;");
                m.add(
                        (c.fileName.contains("_fragment") ?
                                "_fab = _view.findViewById(R.id._fab);" :
                                "_fab = findViewById(R.id._fab);") + a
                );
            }
            if (c.hasActivityOption(ProjectFileBean.OPTION_ACTIVITY_DRAWER) && !c.fileName.contains("_fragment")) {
                addImport("androidx.core.view.GravityCompat");
                addImport("androidx.drawerlayout.widget.DrawerLayout");
                addImport("androidx.appcompat.app.ActionBarDrawerToggle");

                i.add("private DrawerLayout _drawer;");
                m.add(
                        (c.hasActivityOption(ProjectFileBean.OPTION_ACTIVITY_TOOLBAR) ?
                                "_drawer = findViewById(R.id._drawer);" + a +
                                        "ActionBarDrawerToggle _toggle = new ActionBarDrawerToggle(" +
                                        c.getActivityName() +
                                        ".this, _drawer, _toolbar, R.string.app_name, R.string.app_name);" +
                                        a :
                                "_drawer = findViewById(R.id._drawer);" + a +
                                        "ActionBarDrawerToggle _toggle = new ActionBarDrawerToggle(" +
                                        c.getActivityName() +
                                        ".this, _drawer, R.string.app_name, R.string.app_name);" + a) +
                                "_drawer.addDrawerListener(_toggle);" + a +
                                "_toggle.syncState();" + a +
                                a +
                                "LinearLayout _nav_view = findViewById(R.id._nav_view);" + a
                );
                a(mq.c("LinearLayout"));
            }
        }
        addImport("android.app.*");
        addImport("android.os.*");
        addImport("android.view.*");
        addImport("android.view.View.*");
        addImport("android.widget.*");
        addImport("android.content.*");
        addImport("android.content.res.*");
        addImport("android.graphics.*");
        addImport("android.graphics.drawable.*");
        addImport("android.media.*");
        addImport("android.net.*");
        addImport("android.text.*");
        addImport("android.text.style.*");
        addImport("android.util.*");
        addImport("android.webkit.*");
        addImport("android.animation.*");
        addImport("android.view.animation.*");
        addImport("java.io.*");
        addImport("java.util.*");
        addImport("java.util.regex.*");
        addImport("java.text.*");
        addImport("org.json.*");
        o = new Fx(c.getActivityName(), f, "onCreate_initializeLogic", d.a(c.getJavaName(), "onCreate_initializeLogic")).a();
    }

    public final String c(ViewBean viewBean) {
        String replaceAll = viewBean.convert.replaceAll("\\w*\\..*\\.", "");
        if (replaceAll.equals("")) {
            replaceAll = viewBean.getClassInfo().a();
        }
        return Lx.c(replaceAll, viewBean.id, "_nav_view");
    }

    public final void c() {
        for (ViewBean next : d.f(c.getXmlName())) {
            String xmlName = ProjectFileBean.getXmlName(next.customView);
            this.c.getJavaName();
            String str = next.id + "_onBindCustomView";
            String adapterLogic = new Fx(c.getActivityName(), f, str, d.a(c.getJavaName(), str)).a();
            String adapterCode;
            if (next.type == ViewBeans.VIEW_TYPE_LAYOUT_VIEWPAGER) {
                adapterCode = Lx.pagerAdapter(next.id, next.customView, d.d(xmlName), adapterLogic);
            } else if (next.type == ViewBeans.VIEW_TYPE_WIDGET_RECYCLERVIEW) {
                adapterCode = Lx.recyclerViewAdapter(next.id, next.customView, d.d(xmlName), adapterLogic);
            } else {
                adapterCode = Lx.a(next.id, next.customView, d.d(xmlName), adapterLogic);
            }
            q.add(adapterCode);
        }
    }

    public final String d(ViewBean viewBean) {
        String replaceAll = viewBean.convert.replaceAll("\\w*\\..*\\.", "");
        if (replaceAll.equals("")) {
            replaceAll = viewBean.getClassInfo().a();
        }
        if (c.fileName.contains("_fragment")) {
            return Lx.b(replaceAll, viewBean.id, true);
        }
        return Lx.b(replaceAll, viewBean.id, false);
    }

    /**
     * Handles the Activity's More Blocks and adds them to {@link Jx#p}.
     */
    public final void d() {
        String javaName = this.c.getJavaName();
        ArrayList<Pair<String, String>> pairs = d.i(javaName);
        for (int index = 0, pairsSize = pairs.size(); index < pairsSize; index++) {
            Pair<String, String> next = pairs.get(index);
            String name = next.first + "_moreBlock";
            String code = Lx.a(next.first, next.second, new Fx(c.getActivityName(), f, name, d.a(javaName, name)).a());
            if (index < (pairsSize - 1)) {
                p.add(code);
            } else {
                // Removes unnecessary newline at end of More Block code
                p.add(code.substring(0, code.length() - 2));
            }
        }
    }

    public final void e() {
        e = new Hx(f, c, d);
        a(e.e());
    }

    /**
     * Adds imports for blocks used in the currently generated Activity.
     */
    public final void f() {
        for (Map.Entry<String, ArrayList<BlockBean>> entry : d.b(c.getJavaName()).entrySet()) {
            for (BlockBean blockBean : entry.getValue()) {
                switch (blockBean.opCode) {
                    case "toStringWithDecimal":
                    case "toStringFormat":
                        addImport("java.text.DecimalFormat");
                        break;

                    case "strToListMap":
                    case "strToListStr":
                    case "strToMap":
                    case "GsonStringToListString":
                    case "GsonStringToListNumber":
                        addImport("com.google.gson.Gson");
                        addImport("com.google.gson.reflect.TypeToken");
                        break;

                    case "mapToStr":
                    case "listMapToStr":
                    case "GsonListTojsonString":
                        addImport("com.google.gson.Gson");
                        break;

                    case "setTypeface":
                        addImport("android.graphics.Typeface");
                        break;

                    case "copyToClipboard":
                        addImport("android.content.ClipData");
                        addImport("android.content.ClipboardManager");
                        break;

                    case "fileutilGetLastSegmentPath":
                        addImport("android.net.Uri");
                        break;

                    case "setImageUrl":
                        addImport("com.bumptech.glide.Glide");
                        break;

                    case "interstitialAdLoad":
                        addImport("com.google.android.gms.ads.interstitial.InterstitialAd");
                        addImport("com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback");
                        addImport("com.google.android.gms.ads.AdRequest");
                        addImport("com.google.android.gms.ads.LoadAdError");
                        break;
                }
            }
        }
    }

    /**
     * Handles the Activity's Drawer Views and Components
     */
    public final void g() {
        ArrayList<ViewBean> viewBeans = d.d(c.getXmlName());
        for (ViewBean viewBean : viewBeans) {
            m.add(d(viewBean));
        }
        if (c.hasActivityOption(ProjectFileBean.OPTION_ACTIVITY_DRAWER)) {
            ArrayList<ViewBean> drawerBeans = d.d(c.getDrawerXmlName());
            for (ViewBean viewBean : drawerBeans) {
                m.add(c(viewBean));
            }
        }
        ArrayList<ComponentBean> componentBeans = d.e(c.getJavaName());
        for (ComponentBean componentBean : componentBeans) {
            n.add(b(componentBean));
        }
    }

    /**
     * Handles the file's request code constants.
     */
    public final void h() {
        int startValue = 100;
        for (ComponentBean next : d.e(c.getJavaName())) {
            switch (next.type) {
                case ComponentBean.COMPONENT_TYPE_CAMERA:
                case ComponentBean.COMPONENT_TYPE_FILE_PICKER:
                case 31:
                    int incrementedValue = startValue + 1;
                    r.add(Lx.a(next.componentId, incrementedValue));
                    startValue = incrementedValue;
                    break;
            }
        }
    }

    public final void i() {
        String javaName = c.getJavaName();
        for (Pair<Integer, String> next : d.k(javaName)) {
            int intValue = next.first;
            String str = next.second;
            if (intValue == 9) {
                addImport(str);
            } else {
                i.add(b(intValue, str));
            }
        }
        for (Pair<Integer, String> next2 : d.j(javaName)) {
            j.add(a(next2.first, next2.second));
        }
        for (ViewBean viewBean : d.d(c.getXmlName())) {
            k.add(b(viewBean));
        }
        if (c.hasActivityOption(ProjectFileBean.OPTION_ACTIVITY_DRAWER)) {
            for (ViewBean viewBean : d.d(c.getDrawerXmlName())) {
                k.add(a(viewBean));
            }
        }
        ArrayList<ComponentBean> componentBeans = d.e(javaName);
        for (ComponentBean bean : componentBeans) {
            l.add(a(bean));
        }
        if (componentBeans.size() > 0) {
            boolean hasTimer = false;
            boolean hasFirebaseDB = false;
            boolean hasFirebaseStorage = false;
            boolean hasInterstitialAd = false;
            for (ComponentBean bean : componentBeans) {
                if (bean.type == ComponentBean.COMPONENT_TYPE_TIMERTASK) {
                    hasTimer = true;
                }
                if (bean.type == ComponentBean.COMPONENT_TYPE_FIREBASE) {
                    hasFirebaseDB = true;
                }
                if (bean.type == ComponentBean.COMPONENT_TYPE_FIREBASE_STORAGE) {
                    hasFirebaseStorage = true;
                }
                if (bean.type == ComponentBean.COMPONENT_TYPE_INTERSTITIAL_AD) {
                    hasInterstitialAd = true;
                }
            }
            if (hasTimer) {
                h.add(Lx.d("Timer"));
            }
            if (hasFirebaseDB) {
                h.add(Lx.d("FirebaseDB"));
            }
            if (hasFirebaseStorage) {
                h.add(Lx.d("FirebaseStorage"));
            }
            if (hasInterstitialAd) {
                h.add(Lx.d("InterstitialAd"));
            }
        }
    }

    /**
     * Adds Local libraries' imports
     */
    public final void j() {
        for (String value : mll.getImportLocalLibrary()) {
            addImport(value);
        }
    }
}
