package com.huyingbao.rxflux2.login;

import android.content.Context;
import android.widget.EditText;

import com.huyingbao.rxflux2.RxFlux;
import com.huyingbao.rxflux2.BuildConfig;
import com.huyingbao.rxflux2.R;
import com.huyingbao.rxflux2.core.login.LoginActivity;
import com.huyingbao.rxflux2.inject.component.ApplicationComponent;
import com.huyingbao.rxflux2.inject.component.DaggerApplicationComponent;
import com.huyingbao.rxflux2.inject.module.application.ApplicationModule;
import com.huyingbao.rxflux2.inject.module.application.FluxActionModule;
import com.huyingbao.rxflux2.action.ActionCreator;
import com.huyingbao.rxflux2.action.ActionCreatorImpl;
import com.huyingbao.rxflux2.utils.AppUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Created by liujunfeng on 2017/6/28.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class TestLogin {
    @Test
    public void testGetSms() throws Exception {
        //设置application实例对象
        AppUtils.setApplication(RuntimeEnvironment.application);


        //创建一个mockAppModule，这里不能spy(AppModule.class)，因为`AppModule`没有默认无参数的Constructor，
        //也不能mock(AppModule.class),原因是dagger2的约束，Provider方法不能返回null，除非用@Nullable修饰
        ApplicationModule mockAppModule = Mockito.spy(new ApplicationModule(RuntimeEnvironment.application));
        FluxActionModule mockFluxActionModule = Mockito.spy(new FluxActionModule());


        //mock对象
        RxFlux mockRxFlux = Mockito.spy(RxFlux.init(AppUtils.getApplication()));
        //当mockFluxActionModule的provideRxFlux() 方法被调用时，让它返回mockRxFlux
        Mockito.when(mockFluxActionModule.provideRxFlux()).thenReturn(mockRxFlux);

        //mock ActionCreator
        ActionCreator mockActionCreator = Mockito.spy(new ActionCreatorImpl(mockRxFlux.getDispatcher(), mockRxFlux.getSubscriptionManager()));
        Mockito.when(mockFluxActionModule.provideActionCreator(Mockito.any(RxFlux.class))).thenReturn(mockActionCreator);

        //用mockAppModule来创建DaggerApplicationComponent
        ApplicationComponent appComponent = DaggerApplicationComponent.builder()
                .applicationModule(mockAppModule)
                .fluxActionModule(mockFluxActionModule)
                .build();
        //记得放到AppUtils里面，这样LoginActivity#onCreate()里面通过ComponentHolder.getAppComponent()获得的就是这里创建的appComponent
        AppUtils.setApplicationComponent(appComponent);

        //启动LoginActivity，onCreate方法会得到调用，里面的mLoginPresenter通过dagger2获得的，将是mockLoginPresenter
        LoginActivity loginActivity = Robolectric.setupActivity(LoginActivity.class);
        ((EditText) loginActivity.findViewById(R.id.et_employee_code)).setText("002758");
        ((EditText) loginActivity.findViewById(R.id.et_password)).setText("123456");
        ((EditText) loginActivity.findViewById(R.id.et_verification_code)).setText("123456");
        loginActivity.findViewById(R.id.btn_login).performClick();
        //判断是否调用该方法
        Mockito.verify(mockActionCreator).login(Mockito.any(Context.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyString());  //pass!
    }
}
