package mod.hilal.saif.activities.tools;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.sketchware.remod.Resources;

import java.util.ArrayList;
import java.util.HashMap;

import mod.SketchwareUtil;
import mod.agus.jcoderz.lib.FileUtil;
import mod.hey.studios.util.Helper;

public class EventsMakerCreator extends Activity {

    private String _code;
    private String _desc;
    private String _icon;
    private String _lis;
    private String _name;
    private String _par;
    private int _pos;
    private String _spec;
    private String _var;
    private MaterialButton cancel;
    private CheckBox check;
    private EditText eventCode;
    private EditText eventDesc;
    private EditText eventIcon;
    private EditText eventListener;
    private EditText eventName;
    private EditText eventParams;
    private EditText eventSpec;
    private EditText eventVar;
    private String event_name = "";
    private LinearLayout hide;
    private boolean isActivityEvent = false;
    private boolean isEdit = false;
    private String lisName;
    private EditText listenerCode;
    private EditText listenerImport;
    private EditText listenerName;
    private MaterialButton save;
    private ScrollView scroll;
    private ImageView selectIcon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Resources.layout.events_creator);
        if (getIntent().hasExtra("lis_name")) {
            lisName = getIntent().getStringExtra("lis_name");
            isActivityEvent = lisName.equals("");
        }
        if (getIntent().hasExtra("event")) {
            event_name = getIntent().getStringExtra("event");
            isEdit = true;
            _pos = Integer.parseInt(getIntent().getStringExtra("_pos"));
            _name = getIntent().getStringExtra("_name");
            _var = getIntent().getStringExtra("_var");
            _lis = getIntent().getStringExtra("_lis");
            _icon = getIntent().getStringExtra("_icon");
            _desc = getIntent().getStringExtra("_desc");
            _par = getIntent().getStringExtra("_par");
            _spec = getIntent().getStringExtra("_spec");
            _code = getIntent().getStringExtra("_code");
        }
        setToolbar();
        getViewsById();
        setupViews();
        if (isEdit) {
            fillUp();
        }
    }

    private void fillUp() {
        eventName.setText(_name);
        eventVar.setText(_var);
        eventIcon.setText(_icon);
        eventDesc.setText(_desc);
        eventParams.setText(_par);
        eventSpec.setText(_spec);
        eventCode.setText(_code);
    }

    private boolean filledIn() {
        return !eventName.getText().toString().equals("")
                && !eventVar.getText().toString().equals("")
                && !eventIcon.getText().toString().equals("")
                && !eventSpec.getText().toString().equals("")
                && !eventCode.getText().toString().equals("");
    }

    private void getViewsById() {
        scroll = findViewById(Resources.id.events_creator_scrollview);
        eventName = findViewById(Resources.id.events_creator_eventname);
        eventVar = findViewById(Resources.id.events_creator_varname);
        eventListener = findViewById(Resources.id.events_creator_listenername);
        ((View) eventListener.getParent().getParent()).setVisibility(View.GONE);
        eventIcon = findViewById(Resources.id.events_creator_icon);
        eventDesc = findViewById(Resources.id.events_creator_desc);
        eventParams = findViewById(Resources.id.events_creator_params);
        eventSpec = findViewById(Resources.id.events_creator_spec);
        eventCode = findViewById(Resources.id.events_creator_code);
        selectIcon = findViewById(Resources.id.events_creator_chooseicon);
        selectIcon.setImageResource(Resources.drawable.add_96_blue);
        check = findViewById(Resources.id.events_creator_checkbox);
        check.setVisibility(View.GONE);
        hide = findViewById(Resources.id.events_creator_hide);
        listenerName = findViewById(Resources.id.events_creator_listenernameA);
        listenerCode = findViewById(Resources.id.events_creator_listenercode);
        listenerImport = findViewById(Resources.id.events_creator_listenercustomimport);
        cancel = findViewById(Resources.id.events_creator_cancel);
        save = findViewById(Resources.id.events_creator_save);
        if (isActivityEvent) {
            eventVar.setText("");
            ((View) eventVar.getParent().getParent()).setVisibility(View.GONE);
            eventIcon.setText("2131165298");
            ((View) eventIcon.getParent().getParent().getParent()).setVisibility(View.GONE);
        }
    }

    private void setupViews() {
        cancel.setOnClickListener(Helper.getBackPressedClickListener(this));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        selectIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIconSelectorDialog();
            }
        });
    }

    private void showIconSelectorDialog() {
        new IconSelectorDialog(this, eventIcon).show();
    }

    private void save() {
        if (!filledIn()) {
            SketchwareUtil.toast("Some required fields are empty!");
            return;
        }
        ArrayList<HashMap<String, Object>> arrayList;
        String concat = FileUtil.getExternalStorageDir().concat("/.sketchware/data/system/events.json");
        if (FileUtil.isExistFile(concat)) {
            arrayList = new Gson().fromJson(FileUtil.readFile(concat), Helper.TYPE_MAP_LIST);
        } else {
            arrayList = new ArrayList<>();
        }
        HashMap<String, Object> hashMap = new HashMap<>();
        if (isEdit) {
            hashMap = arrayList.get(figureP(_name));
        }
        hashMap.put("name", eventName.getText().toString());
        hashMap.put("var", eventVar.getText().toString());
        if (isActivityEvent) {
            hashMap.put("listener", "");
        } else {
            hashMap.put("listener", lisName);
        }
        hashMap.put("icon", eventIcon.getText().toString());
        hashMap.put("description", eventDesc.getText().toString());
        hashMap.put("parameters", eventParams.getText().toString());
        hashMap.put("code", eventCode.getText().toString());
        hashMap.put("headerSpec", eventSpec.getText().toString());
        if (!isEdit) {
            arrayList.add(hashMap);
        }
        FileUtil.writeFile(concat, new Gson().toJson(arrayList));
        SketchwareUtil.toast("Saved");
        finish();
    }

    private int figureP(String str) {
        String concat = FileUtil.getExternalStorageDir().concat("/.sketchware/data/system/events.json");
        if (FileUtil.isExistFile(concat)) {
            ArrayList<HashMap<String, Object>> arrayList = new Gson().fromJson(FileUtil.readFile(concat), Helper.TYPE_MAP_LIST);
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).get("name").equals(str)) {
                    return i;
                }
            }
        }
        return 0;
    }

    private void setToolbar() {
        TextView tx_toolbar_title = findViewById(Resources.id.tx_toolbar_title);
        if (isEdit) {
            tx_toolbar_title.setText(event_name);
        } else if (isActivityEvent) {
            tx_toolbar_title.setText("Create a new Activity event");
        } else {
            tx_toolbar_title.setText(lisName + "Create a new event");
        }
        ImageView back_icon = findViewById(Resources.id.ig_toolbar_back);
        back_icon.setOnClickListener(Helper.getBackPressedClickListener(this));
        Helper.applyRippleToToolbarView(back_icon);
    }
}