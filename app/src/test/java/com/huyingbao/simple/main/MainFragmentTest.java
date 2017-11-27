package com.huyingbao.simple.main;

import com.huyingbao.simple.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

/**
 * Created by liujunfeng on 2017/11/27.
 */
@RunWith(RobolectricTestRunner.class)
public class MainFragmentTest {
    @Test
    public void toGitRepoList() throws Exception {
        MainFragment fragment = MainFragment.newInstance();
        SupportFragmentTestUtil.startFragment(fragment, MainActivity.class);
        fragment.getActivity().findViewById(R.id.btn_main_to_list).performClick();
    }

}