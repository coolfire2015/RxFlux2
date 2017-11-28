package com.huyingbao.simple.main;

import com.huyingbao.rxflux2.base.fragment.BaseFragment;
import com.huyingbao.simple.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

/**
 * Created by liujunfeng on 2017/11/27.
 */
@RunWith(RobolectricTestRunner.class)
public class MainFragmentTest {
    private BaseFragment fragment;

    //    @Rule
//    public final DaggerMockRule<ApplicationComponent> appRule =
//            new DaggerMockRule<>(ApplicationComponent.class, new ApplicationModule(RuntimeEnvironment.application))
//                    .set(component -> AppUtils.setApplicationComponent(component));
    @Before
    public void newInstance() {
        fragment = MainFragment.newInstance();
    }




    @Test
    public void toGitRepoList() throws Exception {
        SupportFragmentTestUtil.startFragment(fragment, MainActivity.class);
        fragment.getActivity().findViewById(R.id.btn_main_to_list).performClick();
    }

}