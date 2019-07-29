package nz.co.warehouseandroidtest.features.productdetails.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import nz.co.warehouseandroidtest.features.di.annotations.ViewModelKey
import nz.co.warehouseandroidtest.features.productdetails.ProductDetailViewModel
import nz.co.warehouseandroidtest.features.searchproducts.SearchResultsListViewModel

@Module
abstract class ProductDetailModule {
    @Binds
    @IntoMap
    @ViewModelKey(ProductDetailViewModel::class)
    abstract fun bindProductDetailsViewModel(viewModel: ProductDetailViewModel): ViewModel
}