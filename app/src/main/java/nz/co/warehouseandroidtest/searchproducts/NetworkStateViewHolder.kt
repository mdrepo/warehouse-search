package nz.co.warehouseandroidtest.searchproducts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_network_state.view.*
import nz.co.warehouseandroidtest.R
import nz.co.warehouseandroidtest.core.entity.NetworkState
import nz.co.warehouseandroidtest.core.entity.Status
import nz.co.warehouseandroidtest.extensions.visible

class NetworkStateViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(networkState: NetworkState?) {
        itemView.visible = networkState?.status != Status.SUCCESS
        itemView.progressBar.visible = networkState?.status == Status.LOADING
        itemView.retryButton.visible = networkState?.status == Status.FAILED
        itemView.errorText.visible = networkState?.status == Status.FAILED
        itemView.errorText.setText(R.string.error)
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkStateViewHolder {
            val vh = NetworkStateViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_network_state, parent, false))
            vh.itemView.retryButton.setOnClickListener { retryCallback() }
            return vh
        }
    }
}