package nz.co.warehouseandroidtest.core.di.module

import dagger.Module
import dagger.Provides
import nz.co.warehouseandroidtest.core.corountines.CoroutineContexts

@Module(includes = [RepositoryModule::class])
abstract class CoreModule {

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun providesCoroutineContexts(): CoroutineContexts =
            CoroutineContexts()
    }
}