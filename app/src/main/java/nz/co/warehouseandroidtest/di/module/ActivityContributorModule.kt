package nz.co.warehouseandroidtest.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import nz.co.warehouseandroidtest.di.scopes.PerActivity
import nz.co.warehouseandroidtest.features.productdetails.di.module.ProductDetailModule
import nz.co.warehouseandroidtest.productdetails.ProductDetailActivity

@Module
abstract class ActivityContributorModule {
    @PerActivity
    @ContributesAndroidInjector(modules = [ProductDetailModule::class])
    abstract fun contributeProductDetailActivity(): ProductDetailActivity
}