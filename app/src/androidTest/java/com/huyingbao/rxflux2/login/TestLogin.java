package com.huyingbao.rxflux2.login;

import android.content.Context;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.huyingbao.rxflux2.R;
import com.huyingbao.rxflux2.androidtest.inject.component.TestApplicationComponent;
import com.huyingbao.rxflux2.core.login.LoginActivity;
import com.huyingbao.rxflux2.action.ActionCreator;
import com.huyingbao.rxflux2.net.HttpApi;
import com.huyingbao.rxflux2.utils.AppUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.inject.Inject;

/**
 * Created by liujunfeng on 2017/6/26.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestLogin {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Inject
    ActionCreator mActionCreator;

    @Inject
    HttpApi mHttpApi;

    @Before
    public void setUp() {
        ((TestApplicationComponent) AppUtils.getApplicationComponent()).inject(this);
    }

    @Test
    public void testClickGetSmsCode() throws Exception {
        //初始化
        Espresso.onView(ViewMatchers.withId(R.id.et_employee_code))
                //清除员工号
                .perform(ViewActions.clearText())
                //输入员工号
                .perform(ViewActions.typeText("002758"));
        //进行操作
        Espresso.onView(ViewMatchers.withId(R.id.tv_get_sms_code))
                //点击发送验证码
                .perform(ViewActions.click());
        //验证结果
        Mockito.verify(mActionCreator).getSmsCode(Mockito.any(Context.class), Mockito.anyString());
    }

    @Test
    public void testResponse() throws Exception {
        mActionCreator.getSmsCode(Mockito.any(Context.class), "002758");
    }
}