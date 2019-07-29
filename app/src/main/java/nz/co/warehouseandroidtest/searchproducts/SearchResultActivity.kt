package nz.co.warehouseandroidtest.searchproducts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import nz.co.warehouseandroidtest.R
import nz.co.warehouseandroidtest.extensions.transaction

class SearchResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        val query = intent?.extras?.getString(EXTRA_SEARCH_CRITERIA_QUERY)
        if (!query.isNullOrEmpty()) {
            setupFragment(query)
            supportActionBar?.title = query
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        } else {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupFragment(query: String): Fragment {
        var fragment = supportFragmentManager.findFragmentById(R.id.searchContainer)
        if (fragment == null) {
            fragment = SearchResultFragment.newInstance(query)
            supportFragmentManager.transaction {
                replace(R.id.searchContainer, fragment)
            }
        }
        return fragment
    }

    companion object {
        private val EXTRA_SEARCH_CRITERIA_QUERY = "${SearchResultActivity::class.java}.EXTRA_SEARCH_CRITERIA_QUERY"

        @JvmStatic
        fun newIntent(context: Context, query: String?): Intent {
            return Intent(context, SearchResultActivity::class.java).
                putExtra(EXTRA_SEARCH_CRITERIA_QUERY, query)
        }
    }
}
