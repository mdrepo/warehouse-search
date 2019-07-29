package nz.co.warehouseandroidtest.searchproducts

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.android.synthetic.main.item_network_state.view.*
import kotlinx.android.synthetic.main.item_product.view.*
import nz.co.warehouseandroidtest.core.entity.NetworkState
import nz.co.warehouseandroidtest.network.models.Product
import nz.co.warehouseandroidtest.testutils.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchResultAdapterTest {

    private val product = FakeProduct.get()
    private val pagedList = pagedListOf(product, product, product)

    private lateinit var adapter: SearchResultAdapter
    private lateinit var activity: TestRecyclerViewActivity
    private lateinit var controller: ActivityControllerUtil<TestRecyclerViewActivity>

    @Before
    fun setUp() {
        controller = ActivityControllerUtil.of(TestRecyclerViewActivity::class.java)
        activity = controller.setup().get()
        adapter = SearchResultAdapter(activity, activity.retryCallback)
    }

    @After
    fun tearDown() {
        controller.tearDown()
    }

    @Test
    fun `list item show correct info`() {
        showAndAssertViewHolder<SearchResultViewHolder>(0) {
            assertThat(iv_product.visibility).isEqualTo(View.VISIBLE)
            assertThat(tv_product_name.text).isEqualTo("Product description")
        }
    }

    @Test
    fun `click search result item`() {
        showAndAssertViewHolder<SearchResultViewHolder>(0) {
            performClick()
            verify(activity.onSearchResultItemClickListener).onSearchResultItemClicked(product, 0)
        }
    }

    @Test
    fun `network state loading to loaded shown properly`() {
        activity.showRecyclerView(adapter) {
            changeNetworkState(NetworkState.LOADING)
            assertThat(adapter?.itemCount).isEqualTo(1)
            assertViewHolder<NetworkStateViewHolder>(0) {
                assertThat(this.visibility).isEqualTo(View.VISIBLE)
                assertThat(progressBar.visibility).isEqualTo(View.VISIBLE)
                assertThat(retryButton.visibility).isEqualTo(View.GONE)
                assertThat(errorText.visibility).isEqualTo(View.GONE)
            }

            // change to loaded
            changeNetworkState(NetworkState.LOADED)
            assertThat(adapter?.itemCount).isEqualTo(0)
        }
    }

    @Test
    fun `network state loading to error shown properly`() {
        activity.showRecyclerView(adapter) {
            changeNetworkState(NetworkState.LOADING)
            assertThat(adapter?.itemCount).isEqualTo(1)
            assertViewHolder<NetworkStateViewHolder>(0) {
                assertThat(this.visibility).isEqualTo(View.VISIBLE)
                assertThat(progressBar.visibility).isEqualTo(View.VISIBLE)
                assertThat(retryButton.visibility).isEqualTo(View.GONE)
                assertThat(errorText.visibility).isEqualTo(View.GONE)
            }

            // change to error
            changeNetworkState(NetworkState.error("Timeout", null))
            onLayout()
            assertViewHolder<NetworkStateViewHolder>(0) {
                assertThat(this.visibility).isEqualTo(View.VISIBLE)
                assertThat(progressBar.visibility).isEqualTo(View.GONE)
                assertThat(retryButton.visibility).isEqualTo(View.VISIBLE)
                assertThat(errorText.visibility).isEqualTo(View.VISIBLE)
                assertThat(errorText.text).isEqualTo("Error. Please try again.")
            }
        }
    }

    private fun changeNetworkState(networkState: NetworkState) {
        adapter.updateNetworkState(networkState)
    }

    private inline fun <reified T : RecyclerView.ViewHolder> showAndAssertViewHolder(
        position: Int,
        body: View.() -> Unit
    ) {
        adapter.submitList(pagedList)
        activity.showRecyclerView(adapter) {
            assertViewHolder<T>(position, body)
        }
    }

    open class TestRecyclerViewActivity : AppCompatActivity(), OnSearchResultItemClickListener {

        val retryCallback: () -> Unit = mock { }

        val onSearchResultItemClickListener: OnSearchResultItemClickListener = mock { }
        override fun onSearchResultItemClicked(product: Product, pos: Int) {
            onSearchResultItemClickListener.onSearchResultItemClicked(product, pos)
        }

        lateinit var rv: RecyclerView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            rv = RecyclerView(this)
            rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            setContentView(rv, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT))
        }

        inline fun showRecyclerView(
            adapter: RecyclerView.Adapter<*>,
            body: RecyclerView.() -> Unit = { }
        ) {
            rv.adapter = adapter
            rv.onLayout()
            rv.body()
        }
    }
}