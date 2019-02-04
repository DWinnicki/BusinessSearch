package com.winnicki.businesssearch.ui.details

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.winnicki.businesssearch.R
import com.winnicki.businesssearch.network.YelpApiClient

class DetailsFragment : Fragment() {

    companion object {
        const val ARG_BUSINESS_ID = "ARG_BUSINESS_ID"

        fun newInstance(businessId: String?) = DetailsFragment().apply {
            val arguments = Bundle().apply {
                putString(ARG_BUSINESS_ID, businessId)
            }
            setArguments(arguments)
        }
    }

    private lateinit var viewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val businessId = arguments?.getString(ARG_BUSINESS_ID)

        businessId?.let {
            YelpApiClient().getBusinessDetails(businessId)
                .subscribe({
                    Log.d("Details", it.name)
                }, { Log.d("Details", it.message) })
        }
    }
}
