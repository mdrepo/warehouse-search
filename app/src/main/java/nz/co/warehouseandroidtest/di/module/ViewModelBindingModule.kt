package nz.co.warehouseandroidtest.di.module

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import nz.co.warehouseandroidtest.Utils.ViewModelFactory

/**
 * Dagger Module for declaring Map of ViewModel Providers
 */
@Module
abstract class ViewModelBindingModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}
