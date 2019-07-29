package nz.co.warehouseandroidtest.searchproducts

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import nz.co.warehouseandroidtest.R
import nz.co.warehouseandroidtest.core.entity.NetworkState
import nz.co.warehouseandroidtest.core.entity.Status
import nz.co.warehouseandroidtest.network.models.Product

class SearchResultAdapter(
    private val interactionListener: OnSearchResultItemClickListener,
    private val retryCallback: () -> Unit
): PagedListAdapter<Product, RecyclerView.ViewHolder>(LISTING_COMPARATOR) {

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            R.layout.item_product -> {
                val product = getItem(position) ?: return
                (holder as SearchResultViewHolder).bind(product)
            }
            R.layout.item_network_state ->
                (holder as NetworkStateViewHolder).bind(networkState)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.item_product) {
            val vh = SearchResultViewHolder.create(parent)
            vh.itemView.setOnClickListener {
                onClicked(vh.adapterPosition, interactionListener::onSearchResultItemClicked)
            }
            vh
        } else {
            NetworkStateViewHolder.create(parent, retryCallback)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && isNetworkStateShown()) {
            R.layout.item_network_state
        } else {
            R.layout.item_product
        }
    }

    private fun onClicked(vhPosition: Int, callback: (Product, Int) -> Unit) {
        if (vhPosition == RecyclerView.NO_POSITION) {
            return
        }
        val product = getItem(vhPosition) ?: return
        callback(product, vhPosition)
    }

    fun updateNetworkState(newNetworkState: NetworkState?) {
        val prevNetworkState = this.networkState
        val shownNetworkState = isNetworkStateShown()
        this.networkState = newNetworkState
        val showNetworkState = isNetworkStateShown()
        if (shownNetworkState != showNetworkState) {
            if (showNetworkState) {
                notifyItemInserted(itemCount)
            } else {
                notifyItemRemoved(itemCount)
            }
        } else if (showNetworkState && prevNetworkState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    private fun isNetworkStateShown() =
        networkState != null && networkState?.status != Status.SUCCESS

    override fun getItemCount(): Int = super.getItemCount() + if (isNetworkStateShown()) 1 else 0

    companion object {
        val LISTING_COMPARATOR = object : DiffUtil.ItemCallback<Product>() {
            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem.productKey == newItem.productKey
        }
    }
}