package io.vextil.launcher;

import io.paperdb.Paper;
import io.vextil.launcher.managers.AppsManager;
import io.vextil.launcher.managers.SettingsManager;
import io.vextil.launcher.managers.WebAppsManager;

public class Helios extends android.app.Application {

    public static SettingsManager settings;
    public static AppsManager apps;
    public static WebAppsManager webApps;

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
        settings = new SettingsManager(getApplicationContext());
        apps = new AppsManager(getApplicationContext());
        webApps = new WebAppsManager(getApplicationContext());
    }

}
