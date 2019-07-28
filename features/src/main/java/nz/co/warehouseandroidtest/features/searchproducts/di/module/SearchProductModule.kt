package nz.co.warehouseandroidtest.features.searchproducts.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import nz.co.warehouseandroidtest.features.di.annotations.ViewModelKey
import nz.co.warehouseandroidtest.features.searchproducts.SearchResultsListViewModel

@Module
abstract class SearchProductModule {
    @Binds
    @IntoMap
    @ViewModelKey(SearchResultsListViewModel::class)
    abstract fun bindSearchResultsListViewModel(viewModel: SearchResultsListViewModel): ViewModel
}