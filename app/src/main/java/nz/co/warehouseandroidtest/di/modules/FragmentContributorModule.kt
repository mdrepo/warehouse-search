package nz.co.warehouseandroidtest.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import nz.co.warehouseandroidtest.di.scopes.PerFragment
import nz.co.warehouseandroidtest.features.searchproducts.di.SearchProductModule
import nz.co.warehouseandroidtest.searchproducts.SearchResultFragment

/**
 * Dagger Module for declaring Fragment-Sub-Graph
 */

@Module
abstract class FragmentContributorModule {

    @PerFragment
    @ContributesAndroidInjector(modules = [SearchProductModule::class])
    abstract fun contributeSearchResultsListFragment(): SearchResultFragment
}