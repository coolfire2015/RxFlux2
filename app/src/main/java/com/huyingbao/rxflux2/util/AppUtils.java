package com.huyingbao.rxflux2.util;

import android.app.Application;
import android.support.annotation.NonNull;

import com.huyingbao.rxflux2.inject.component.ApplicationComponent;


/**
 * Created by liujunfeng on 2017/1/1.
 */
public class AppUtils {
    private static ApplicationComponent sApplicationComponent;// 注意是静态，全局性质
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
}
