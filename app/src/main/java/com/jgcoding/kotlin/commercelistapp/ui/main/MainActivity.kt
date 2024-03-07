package com.jgcoding.kotlin.commercelistapp.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.jgcoding.kotlin.commercelistapp.R
import com.jgcoding.kotlin.commercelistapp.core.Extensions.setStatusBar
import com.jgcoding.kotlin.commercelistapp.core.Network
import com.jgcoding.kotlin.commercelistapp.databinding.ActivityMainBinding
import com.jgcoding.kotlin.commercelistapp.domain.model.Commerce
import com.jgcoding.kotlin.commercelistapp.ui.detail.DetailActivity
import com.jgcoding.kotlin.commercelistapp.ui.main.adapter.CommerceAdapter
import com.jgcoding.kotlin.commercelistapp.ui.main.viewmodel.MainViewModel
import com.jgcoding.kotlin.commercelistapp.ui.main.viewmodel.MainViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        const val ID = "ID"
    }

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var commerceAdapter: CommerceAdapter

    private var coordinates: Location = Location("MyLocation")
    private val locationPermissionCode = 2

    private var list = emptyList<Commerce>()
    private var stationList = emptyList<Commerce>()
    private var foodList = emptyList<Commerce>()
    private var leisureList = emptyList<Commerce>()

    //region LC
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val action: String? = intent?.action
        val data: Uri? = intent?.data
        Log.i(TAG, "onCreate: $data")
        Log.i(TAG, "onCreate: $action")

        if(!Network.checkConnectivity(this)) {
            Toast.makeText(this, getString(R.string.error_connectivity), Toast.LENGTH_SHORT).show()
        }
        getMyCoordinates()
        setUpview()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val action: String? = intent?.action
        val data: Uri? = intent?.data
        Log.i(TAG, "onCreate: $data")
        Log.i(TAG, "onCreate: $action")
    }
    //endregion

    //region PERMISSION
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getMyCoordinates()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    locationPermissionCode
                )
            }
        }
    }
    //endregion

    //region VIEW
    private fun setUpview() {
        setStatusBar()
        setSupportActionBar(binding.toolbar)

        binding.apply {
            cvCategoryAll.setOnClickListener{
                commerceAdapter.updateList(list)
            }

            cvCategory1.setOnClickListener {
                commerceAdapter.updateList(stationList)
            }

            cvCategory2.setOnClickListener {
                commerceAdapter.updateList(foodList)
            }

            cvCategory3.setOnClickListener {
                commerceAdapter.updateList(leisureList)
            }
        }

        initUIState()
    }

    private fun setUpRecyclerView(list: MutableList<Commerce>) {
        commerceAdapter = CommerceAdapter(list) { id ->
            navigateToDetail(id)
        }

        binding.rvCommerces.apply {
            adapter = commerceAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun loadingState() {
        binding.includeProgress.root.isVisible = true
    }

    private fun errorState() {
        binding.includeProgress.root.isVisible = false
    }

    private fun successState(state: MainViewState.Success) {
        var counterOfNearCommerces = 0
        for (commerce in state.commerces) {
            commerce.setDistance(coordinates)
            if (commerce.checkDistance())
                counterOfNearCommerces++
        }

        list = state.commerces.sortedBy {
            it.distance
        }

        binding.includeProgress.root.isVisible = false
        binding.apply {
            txtNumberCommerces.text = state.commerces.size.toString()
            txtNumberCommercesNear.text = counterOfNearCommerces.toString()
        }

        setUpRecyclerView(list.toMutableList())

        prepareFilteredLists(list)
    }
    //endregion

    //region METHODS
    private fun initUIState() {
        lifecycleScope.launch {
            viewModel.uiState.collect {
                when (it) {
                    is MainViewState.Error -> errorState()
                    MainViewState.Loading -> loadingState()
                    is MainViewState.Success -> successState(it)
                }
            }
        }
    }

    private fun getMyCoordinates() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        } else {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    Log.i(TAG, "Lat: ${location?.latitude}, Lon: ${location?.longitude}")
                    coordinates = location!!
                }
        }
    }

    private fun prepareFilteredLists(list: List<Commerce>) {
        stationList = list.filter {
            it.category.uppercase().contains("GAS_STATION")
        }

        foodList = list.filter {
            it.category.uppercase().contains("FOOD")
        }

        leisureList = list.filter {
            it.category.uppercase().contains("LEISURE")
        }
    }

    private fun navigateToDetail(id: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(ID, id)
        startActivity(intent)
    }
    //endregion
}