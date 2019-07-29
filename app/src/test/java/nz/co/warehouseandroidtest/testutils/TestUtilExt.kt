package nz.co.warehouseandroidtest.testutils

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.nhaarman.mockitokotlin2.mock
import java.util.concurrent.Executor

fun <T> liveDataOf(value: T): LiveData<T> {
    val liveData = MutableLiveData<T>()
    liveData.value = value
    return liveData
}

fun RecyclerView.onLayout() {
    this.measure(0, 0)
    this.layout(0, 0, 100, 10000)
}


fun <T> pagedListOf(vararg elements: T): PagedList<T> {
    val dataSource = object : PageKeyedDataSource<Int, T>() {
        override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Int, T>
        ) {
            val list: MutableList<T> = mutableListOf()
            list.addAll(elements)
            callback.onResult(list, null, 2)
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        }
    }
    val executor: Executor = mock { }
    return PagedList.Builder(dataSource, mock<PagedList.Config> { })
            .setFetchExecutor(executor)
            .setNotifyExecutor(executor)
            .build()
}

inline fun <reified T : RecyclerView.ViewHolder> RecyclerView.assertViewHolder(
    position: Int,
    body: View.() -> Unit
) {
    val vh = findViewHolderForAdapterPosition(position) as T
    vh.itemView.body()
}