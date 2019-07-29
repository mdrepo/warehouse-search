package nz.co.warehouseandroidtest.searchproducts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.empty_search_results.*
import kotlinx.android.synthetic.main.fragment_search_result.*
import nz.co.warehouseandroidtest.productdetails.ProductDetailActivity
import nz.co.warehouseandroidtest.R
import nz.co.warehouseandroidtest.Utils.PreferenceUtil
import nz.co.warehouseandroidtest.core.entity.SearchCriteriaParam
import nz.co.warehouseandroidtest.extensions.visible
import nz.co.warehouseandroidtest.features.searchproducts.SearchResultsListViewModel
import nz.co.warehouseandroidtest.network.models.Product
import javax.inject.Inject

class SearchResultFragment: Fragment(), OnSearchResultItemClickListener {

    private var queryText: String? = null
    private lateinit var adapter: SearchResultAdapter

    @Inject
    internal lateinit var vmFactory: ViewModelProvider.Factory
    private val viewModel: SearchResultsListViewModel by lazy(mode = LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, vmFactory)
            .get(SearchResultsListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        queryText = arguments?.getString(EXTRA_SEARCH_CRITERIA_QUERY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search_result, container, false)

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        if (!viewModel.hasFetched) {
            fetchProducts()
        }
    }

    private fun setup() {
        recyclerView.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        adapter = SearchResultAdapter(this) { fetchProducts() }
        recyclerView.adapter = adapter

        swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
            swipeRefresh.isRefreshing = false
        }

        viewModel.products.observe(this, Observer {
            adapter.submitList(it)
        })
        viewModel.totalItems.observe(this, Observer {
            if (it == 0) {
                showEmptyView()
            }
        })
        viewModel.networkState.observe(this, Observer {
            adapter.updateNetworkState(it)
        })
    }

    private fun showEmptyView() {
        empty_view_stub.layoutResource = R.layout.empty_search_results
        val emptyView = empty_view_stub.inflate()
        emptyView.visible = true
        button_search.setOnClickListener { activity?.finish() }
    }

    private fun fetchProducts() = viewModel.fetchProducts(
        SearchCriteriaParam(queryText, userId = PreferenceUtil.getUserId(context)),
        ITEMS_PER_PAGE
    )

    override fun onSearchResultItemClicked(product: Product, pos: Int) {
        val intent = Intent()
        intent.setClass(context, ProductDetailActivity::class.java)
        intent.putExtra(ProductDetailActivity.FLAG_BAR_CODE, product.barcode)
        context?.startActivity(intent)
    }

    companion object {
        const val ITEMS_PER_PAGE = 10
        private val EXTRA_SEARCH_CRITERIA_QUERY = "${SearchResultFragment::class.java}.EXTRA_SEARCH_CRITERIA_QUERY"

        @JvmStatic
        fun newInstance(query: String? = null): SearchResultFragment {
            val fragment = SearchResultFragment()
            fragment.arguments = Bundle().apply {
                putString(EXTRA_SEARCH_CRITERIA_QUERY, query)
            }
            return fragment
        }
    }
}