package nz.co.warehouseandroidtest.features.entity

import androidx.lifecycle.LiveData
import nz.co.warehouseandroidtest.core.entity.NetworkState

data class DataResource<T>(
    val networkState: LiveData<NetworkState>,
    val data: LiveData<T>
)