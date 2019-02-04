package com.winnicki.businesssearch.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.winnicki.businesssearch.R

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)

        val businessId = intent?.extras?.getString(EXTRA_BUSINESS_ID)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, DetailsFragment.newInstance(businessId))
                .commitNow()
        }
    }

    companion object {
        const val EXTRA_BUSINESS_ID = "EXTRA_BUSINESS_ID"

        fun getIntent(context: Context?, businessId: String) =
            Intent(context, DetailsActivity::class.java).apply {
                putExtra(EXTRA_BUSINESS_ID, businessId)
            }
    }
}
