package com.winnicki.businesssearch.ui.search

import android.Manifest
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.winnicki.businesssearch.R
import com.winnicki.businesssearch.model.Business
import com.winnicki.businesssearch.network.YelpApiClient
import com.winnicki.businesssearch.ui.details.DetailsActivity
import kotlinx.android.synthetic.main.search_fragment.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class SearchFragment : Fragment(), SearchAdapter.OnItemClickListener, EasyPermissions.PermissionCallbacks,
    SearchView.OnQueryTextListener, RadioGroup.OnCheckedChangeListener {

    private lateinit var viewModel: SearchViewModel
    private var lastKnowLocation: Location? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.search_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissions()
        filters.apply {
            check(R.id.filterName)
            setOnCheckedChangeListener(this@SearchFragment)
        }
        searchView.setOnQueryTextListener(this)
    }

    @SuppressLint("MissingPermission")
    private fun search(searchTerm: String?) {
        progressBar.visibility = View.VISIBLE
        lastKnowLocation = (activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
            .getLastKnownLocation(LocationManager.GPS_PROVIDER)

        lastKnowLocation?.let { location ->
            YelpApiClient().searchBusinesses(searchTerm, location)
                .subscribe({
                    onResult(it.businesses.toMutableList())
                }, {
                    Log.e("CRAP", it.message)
                    progressBar.visibility = View.GONE
                })
        }
    }

    private fun onResult(businesses: MutableList<Business>) {
        when (filters.checkedRadioButtonId) {
            R.id.filterName -> businesses.sortBy { it.name }
            R.id.filterRating -> businesses.sortByDescending { it.rating }
            R.id.filterDistance -> businesses.sortBy { it.distance }
        }
        progressBar.visibility = View.GONE
        recyclerView.apply {
            adapter = SearchAdapter(businesses, this@SearchFragment)
            val layoutManager = LinearLayoutManager(context)
            setLayoutManager(layoutManager)
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
    }

    override fun onItemClick(item: Business) {
        startActivity(DetailsActivity.getIntent(context, item.id))
    }

    override fun onQueryTextSubmit(searchTerm: String?): Boolean {
        search(searchTerm)
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return false
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        (recyclerView.adapter as SearchAdapter).apply {
            when (checkedId) {
                R.id.filterName -> sortByName()
                R.id.filterRating -> sortByRating()
                R.id.filterDistance -> sortByDistance()
            }
        }
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(REQUEST_CODE_FINE_LOCATION_PERMISSION)
    private fun checkPermissions() {
        if (EasyPermissions.hasPermissions(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            search(null)
        } else {
            EasyPermissions.requestPermissions(
                this,
                "The app needs to see your location.",
                REQUEST_CODE_FINE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        search(null)
    }

    override fun onPermissionsDenied(requestCode: Int, list: List<String>) {
        checkPermissions()
    }

    companion object {
        const val REQUEST_CODE_FINE_LOCATION_PERMISSION = 1_000

        fun newInstance() = SearchFragment()
    }
}
