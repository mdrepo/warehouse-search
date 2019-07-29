package nz.co.warehouseandroidtest.searchproducts

import nz.co.warehouseandroidtest.network.models.Product

interface OnSearchResultItemClickListener {

    fun onSearchResultItemClicked(product: Product, pos: Int)
}