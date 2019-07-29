package nz.co.warehouseandroidtest.searchproducts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_product.view.*

import nz.co.warehouseandroidtest.R
import nz.co.warehouseandroidtest.network.models.Product

class SearchResultViewHolder(private val mItemView: View) : RecyclerView.ViewHolder(mItemView) {

    companion object {
        fun create(parent: ViewGroup): SearchResultViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return SearchResultViewHolder(inflater.inflate(R.layout.item_product, parent, false))
        }
    }

    fun bind(product: Product?) {
        if (product == null) return

        if (!product.description.isNullOrBlank()) {
            itemView.tv_product_name.text = product.description
        }

        if (!product.imageURL.isNullOrBlank()) {
            Glide.with(itemView.iv_product.context)
                .load(product.imageURL)
                .placeholder(R.drawable.ic_box)
                .into(itemView.iv_product)
        }
    }

}
