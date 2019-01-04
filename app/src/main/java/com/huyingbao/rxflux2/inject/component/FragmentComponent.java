package com.huyingbao.rxflux2.inject.component;

import com.huyingbao.rxflux2.inject.module.FragmentModule;
import com.huyingbao.rxflux2.inject.scope.PerFragment;
import com.huyingbao.simple.ProductListFragment;

import dagger.Subcomponent;

/**
 * fragment注入器
 * Subcomponent用于拓展原有component，
 * Subcomponent同时具备两种不同生命周期的scope，
 * Subcomponent具备了父Component拥有的Scope，也具备了自己的Scope，
 * 注意子Component的Scope范围小于父Component。
 * <p>
 * Subcomponent其功能效果优点类似component的dependencies，
 * 但是使用@Subcomponent不需要在父component中显式添加子component需要用到的对象，
 * 只需要添加返回子Component的方法即可，
 * 子Component能自动在父Component中查找缺失的依赖。
 * <p>
 * 通过Subcomponent，子Component就好像同时拥有两种Scope，
 * 当注入的元素来自父Component的Module，则这些元素会缓存在父Component，
 * 当注入的元素来自子Component的Module，则这些元素会缓存在子Component中。
 * <p>
 * SubComponent 完全继承Component中的全部依赖，
 * 两个拥有依赖关系的 Component 是不能有相同 @Scope 注解的，
 * 使用@SubComponent 则可以使用相同的@Scope注解。
 * <p>
 * Component dependencies 能单独使用
 * Subcomponent必须由Component调用方法获取。
 * <p>
 * Component dependencies 可以很清楚的得知他依赖哪个Component，
 * 而Subcomponent不知道它自己的谁的孩子
 * Created by liujunfeng on 2017/1/1.
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {
    void inject(ProductListFragment productListFragment);
}
