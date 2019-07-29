package nz.co.warehouseandroidtest.features.searchproducts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import nz.co.warehouseandroidtest.core.entity.SearchCriteriaParam
import nz.co.warehouseandroidtest.features.entity.PagedResource
import nz.co.warehouseandroidtest.network.models.Product
import javax.inject.Inject

class SearchResultsListViewModel @Inject constructor(
    private val useCase: SearchProductsUseCase
) : ViewModel() {

    private val pagedResource: MutableLiveData<PagedResource<Product>> = MutableLiveData()
    val products = switchMap(pagedResource) {
        hasFetched = true
        it.pagedList
    }
    val totalItems = switchMap(pagedResource) { it.totalItems }
    val networkState = switchMap(pagedResource) { it.networkState }

    var hasFetched: Boolean = false
        private set

    public override fun onCleared() {
        super.onCleared()
        useCase.unsubscribe()
        hasFetched = false
    }

    fun fetchProducts(searchCriteria: SearchCriteriaParam?, limit: Int) {
        pagedResource.postValue(useCase.searchProducts(searchCriteria, limit))
    }

    fun refresh() {
        pagedResource.value?.refresh?.invoke()
    }

    fun retry() {
        pagedResource.value?.retry?.invoke()
    }
}
