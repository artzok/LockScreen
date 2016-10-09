package com.art.zok.lock;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author 赵坤
 * @email artzok@163.com
 */
public class MainActivity extends AppCompatActivity {

    private static final String ACTION_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    private static final String ACTION_LOCK = "com.art.zok.lockscreen.lock";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createShortCut(getApplicationContext());
        lockScreen();
        finish();
    }

    public static void createShortCut(Context context) {
        // create shortcut Intent
        Intent shortCutIntent = new Intent(ACTION_SHORTCUT);
        // request can't duplicate
        shortCutIntent.putExtra("duplicate", false);
        // shortcut label
        shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
        // shortcut icon
        Parcelable icon = Intent.ShortcutIconResource.fromContext(
                context.getApplicationContext(), R.mipmap.icon);
        shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        // shortcut response Intent
        Intent value = new Intent(ACTION_LOCK);
        shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, value);
        // send shortcut broadcast
        context.sendBroadcast(shortCutIntent);
    }

    private void lockScreen() {
        // determine app whether be grand admin permission
        DevicePolicyManager dpm = (DevicePolicyManager)
                getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName mDeviceAdmin = new ComponentName(this, SafeAdminReceiver.class);
        if (dpm.isAdminActive(mDeviceAdmin)) {
            dpm.lockNow();
        } else {
            // authoritative page
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdmin);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, R.string.admin_desc);
            startActivity(intent);
            finish();
        }
    }
}
