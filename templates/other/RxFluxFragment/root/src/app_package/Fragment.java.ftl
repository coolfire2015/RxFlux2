package ${packageName};

import android.os.Bundle;

<#if applicationPackage??>
import ${applicationPackage}.R;
</#if>
<#if applicationPackage??>
import ${applicationPackage}.base.fragment.BaseFragment;
</#if>

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

    }
}
