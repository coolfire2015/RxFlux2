package ${packageName};

import android.os.Bundle;

<#if applicationPackage??>
import ${applicationPackage}.R;
</#if>
import com.huyingbao.rxflux2.base.base.fragment.BaseFragment;

public class ${fragmentClass} extends BaseFragment {

    public static ${fragmentClass} newInstance() {
        ${fragmentClass} fragment = new ${fragmentClass}();
        return fragment;
    }

    @Override
    public void initInjector() {
        mFragmentComponent.inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.${layoutName};
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initActionBar();
    }
}
