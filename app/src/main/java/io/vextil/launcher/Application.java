package io.vextil.launcher;

import io.paperdb.Paper;
import io.vextil.launcher.managers.AppsManager;
import io.vextil.launcher.managers.WebAppsManager;

public class Application extends android.app.Application {

    public static AppsManager appsManager;
    public static WebAppsManager webAppsManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
        appsManager = new AppsManager(getApplicationContext());
        webAppsManager = new WebAppsManager(getApplicationContext());
    }

}
