package pro.sketchware;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Process;
import android.util.Log;

import com.besome.sketch.tools.CollectErrorActivity;
import com.google.android.material.color.DynamicColors;

import pro.sketchware.utility.theme.ThemeManager;

public class SketchApplication extends Application {
    private static Context mApplicationContext;
    private SharedPreferences dynamic;

    public static Context getContext() {
        return mApplicationContext;
    }

    @Override
    public void onCreate() {
        mApplicationContext = getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                Intent intent = new Intent(getApplicationContext(), CollectErrorActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("error", Log.getStackTraceString(throwable));
                startActivity(intent);
                Process.killProcess(Process.myPid());
                System.exit(1);
            }
        });
        super.onCreate();
        dynamic = getSharedPreferences("dynamic", getContext().MODE_PRIVATE);
        ThemeManager.applyTheme(this, ThemeManager.getCurrentTheme(this));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (dynamic.getString("dynamic", "").equals("true")) {
                DynamicColors.applyToActivitiesIfAvailable(this);
            }
        }
    }
}
