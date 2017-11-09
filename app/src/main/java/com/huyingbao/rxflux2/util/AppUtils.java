package com.huyingbao.rxflux2.util;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.huyingbao.rxflux2.inject.component.ApplicationComponent;


/**
 * Created by liujunfeng on 2017/1/1.
 */
public class AppUtils {
    private static ApplicationComponent sApplicationComponent;
    private static Application sApplication;

    public static ApplicationComponent getApplicationComponent() {
        return sApplicationComponent;
    }

    public static void setApplicationComponent(@NonNull ApplicationComponent applicationComponent) {
        sApplicationComponent = applicationComponent;
    }

    public static Application getApplication() {
        return sApplication;
    }

    public static void setApplication(Application application) {
        sApplication = application;
    }

    /**
     * 获取App包 信息版本号
     *
     * @return
     */
    public static PackageInfo getPackageInfo() {
        PackageManager packageManager = sApplication.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(sApplication.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }
}
