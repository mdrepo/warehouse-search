package nz.co.warehouseandroidtest.features.entity

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import nz.co.warehouseandroidtest.core.entity.NetworkState

data class PagedResource<T>(
    val totalItems: LiveData<Int>,
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>,
    val refresh: () -> Unit,
    val retry: () -> Unit
)
