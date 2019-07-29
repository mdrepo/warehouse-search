package nz.co.warehouseandroidtest.di.components

import dagger.Component
import dagger.android.AndroidInjector
import nz.co.warehouseandroidtest.WarehouseTestApp
import nz.co.warehouseandroidtest.core.di.components.CoreComponent
import nz.co.warehouseandroidtest.di.module.FragmentContributorModule
import nz.co.warehouseandroidtest.di.module.ViewModelBindingModule
import nz.co.warehouseandroidtest.di.scopes.AppScope

@AppScope
@Component(modules = [
    ViewModelBindingModule::class,
    FragmentContributorModule::class
], dependencies = [
    CoreComponent::class
])
interface AppComponent : AndroidInjector<WarehouseTestApp>