package nz.co.warehouseandroidtest.productdetails

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_product_detail.*
import nz.co.warehouseandroidtest.R
import nz.co.warehouseandroidtest.Utils.PreferenceUtil
import nz.co.warehouseandroidtest.extensions.setTextorHide
import nz.co.warehouseandroidtest.extensions.visible
import nz.co.warehouseandroidtest.features.productdetails.ProductDetailViewModel
import nz.co.warehouseandroidtest.network.models.ProductDetailResponse
import javax.inject.Inject

class ProductDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    lateinit var viewModel: ProductDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        AndroidInjection.inject(this)
        val barCode = intent?.extras?.getString(FLAG_BAR_CODE)
        viewModel = ViewModelProviders.of(this, vmFactory).get(ProductDetailViewModel::class.java)
        if (savedInstanceState == null) {
            viewModel.setProductRequest(PreferenceUtil.getUserId(this), barCode)
        }
        viewModel.networkState.observe(this, Observer {

        })
        viewModel.product.observe(this, Observer {
            show(it)
        })
    }

    private fun show(product: ProductDetailResponse?) {
        if (product != null) {
            product.product?.imageURL?.let {
                Glide.with(applicationContext).load(it).placeholder(R.drawable.ic_box).into(iv_product)
            }
            tv_product.setTextorHide(product.product?.description)
            tv_barcode.setTextorHide(product.product?.price?.price)
            tv_barcode.setTextorHide(product.product?.barcode)
            iv_clearance.visible = product.product?.price?.isClearance() ?: false
        }
    }


    companion object {
        const val FLAG_BAR_CODE = "barCode"
    }
}
