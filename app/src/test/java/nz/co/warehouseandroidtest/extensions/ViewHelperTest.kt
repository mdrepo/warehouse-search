package nz.co.warehouseandroidtest.extensions

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import nz.co.warehouseandroidtest.R
import nz.co.warehouseandroidtest.searchproducts.SearchResultFragment
import nz.co.warehouseandroidtest.testutils.ActivityControllerUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@RunWith(RobolectricTestRunner::class)
class ViewHelperTest {

    private lateinit var activity: TestActivity
    private lateinit var controller: ActivityControllerUtil<TestActivity>

    @Before
    fun setUp() {
        controller = ActivityControllerUtil.of(TestActivity::class.java)
        activity = controller.setup().get()
    }

    @After
    fun tearDown() {
        controller.tearDown()
    }

    @Test
    fun `check fragment transaction`() {
        assertNull(activity.supportFragmentManager.findFragmentById(R.id.searchContainer))
        activity.setupFragment("abc")
        assertNotNull(activity.supportFragmentManager.findFragmentById(R.id.searchContainer))
    }

    @Test
    fun `view visibility should change`() {
        val textView = TextView(activity.applicationContext)
        assertThat(textView.visibility).isEqualTo(View.VISIBLE)
        textView.visible = false
        assertThat(textView.visibility).isEqualTo(View.GONE)
        textView.visible = true
        assertThat(textView.visibility).isEqualTo(View.VISIBLE)
    }

    open class TestActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_search_result)
        }

        fun setupFragment(query: String): Fragment {
            var fragment = supportFragmentManager.findFragmentById(R.id.searchContainer)
            if (fragment == null) {
                fragment = SearchResultFragment.newInstance(query)
                supportFragmentManager.transaction {
                    replace(R.id.searchContainer, fragment)
                }
            }
            return fragment
        }
    }
}