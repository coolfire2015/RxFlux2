package ${packageName};

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

<#if applicationPackage??>
import ${applicationPackage}.R;
</#if>
<#if applicationPackage??>
import ${applicationPackage}.base.fragment.BaseRxFluxFragment;
</#if>
import ${packageName}.store.${storeClass};
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.store.RxStoreChange;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class ${fragmentClass} extends BaseRxFluxFragment {
    @Inject
    ${storeClass} mStore;

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

    @Override
    public void onRxStoreChanged(@NonNull RxStoreChange rxStoreChange) {

    }

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToRegister() {
        return Collections.singletonList(mStore);
    }
}
