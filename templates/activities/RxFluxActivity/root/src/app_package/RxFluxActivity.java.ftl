package ${packageName};

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

<#if applicationPackage??>
import ${applicationPackage}.base.activity.BaseRxFluxToolbarActivity;
</#if>
import ${packageName}.store.${storeClass};
import com.huyingbao.rxflux2.store.RxStore;
import com.huyingbao.rxflux2.store.RxStoreChange;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class ${activityClass} extends BaseRxFluxToolbarActivity {
    @Inject
    ${storeClass} mStore;

    public static Intent newIntent(Context content) {
        Intent intent = new Intent(content, ${activityClass}.class);
        return intent;
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
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

    @Nullable
    @Override
    public List<RxStore> getRxStoreListToUnRegister() {
        return Collections.singletonList(mStore);
    }
}
