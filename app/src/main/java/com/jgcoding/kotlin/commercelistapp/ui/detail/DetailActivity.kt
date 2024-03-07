package com.jgcoding.kotlin.commercelistapp.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jgcoding.kotlin.commercelistapp.R
import com.jgcoding.kotlin.commercelistapp.core.Extensions.setStatusBar
import com.jgcoding.kotlin.commercelistapp.databinding.ActivityDetailBinding
import com.jgcoding.kotlin.commercelistapp.domain.model.Commerce
import com.jgcoding.kotlin.commercelistapp.ui.detail.viewmodel.DetailViewModel
import com.jgcoding.kotlin.commercelistapp.ui.detail.viewmodel.DetailViewState
import com.jgcoding.kotlin.commercelistapp.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DetailActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val TAG = "DetailActivity"
    }

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    private lateinit var map: GoogleMap


    //region LC
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setPrincipalViewAndListeners()
        checkDeepLink()
        getIntentFromMain()
        collectData()
        setMap()
    }
    //endregion


    //region Methods
    private fun getIntentFromMain() {
        intent.getIntExtra(MainActivity.ID, 0).let { id ->
            Log.i(TAG, "getIntentFromMain: $id")
            viewModel.getCommerceId(id)
        }
    }

    private fun collectData() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is DetailViewState.Error -> errorState()
                    DetailViewState.Loading -> loadingState()
                    is DetailViewState.Success -> successState(state)
                }
            }
        }
    }

    private fun checkDeepLink() {
        intent.data?.let { data ->
            Log.i(TAG, "checkDeepLink: $data")
            val deepLinkId = data.path?.replace("/", "")
            Log.i(TAG, "checkDeepLink path: $deepLinkId")
            if(!deepLinkId.isNullOrEmpty() && deepLinkId.isDigitsOnly()){
                viewModel.getCommerceId(deepLinkId.toInt())
            }
        }
    }

    private fun setMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mvMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }
    //endregion

    //region VIEW
    private fun setPrincipalViewAndListeners() {
        setStatusBar()
        setSupportActionBar(binding.toolbarDetail)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.title = ""

        binding.apply {
            ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }
    }

    private fun loadingState() {
        binding.includeProgress.root.isVisible = true
    }

    private fun errorState() {
        binding.includeProgress.root.isVisible = false
    }

    private fun successState(state: DetailViewState.Success) {
        val commerce = state.commerce
        binding.apply {
            includeProgress.root.isVisible = false

            toolbarTitle.text = commerce.name

            Glide.with(applicationContext)
                .load(commerce.photo)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .transform( CenterCrop(), RoundedCorners(16))
                .into(ivRefresh)

            tvDetail1.text = "${getString(R.string.category_info)} ${commerce.category}"
            tvDetail2.text = "${getString(R.string.hour_info)}  ${commerce.cashback}"
            tvDetail3.text = "${getString(R.string.cashback_info)}  ${commerce.openingHours}"

            tvABout.text = "${commerce.address} con coordenadas: ${commerce.location.first} ${commerce.location.second}"

            tvMap.setOnClickListener {
                openGoogleMaps(commerce)
            }

            createMarkerMap(commerce)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }

    private fun createMarkerMap(commerce: Commerce) {
        val place = LatLng(commerce.location.second, commerce.location.first)
        map.addMarker(MarkerOptions().position(place).title("Destino"))
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(place, 18f),
            2000,
            null
        )
    }

    private fun openGoogleMaps(commerce: Commerce) {
        val geoUri = "http://maps.google.com/maps?q=loc:${commerce.location.second},${commerce.location.first} (${commerce.name})"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
        startActivity(intent)
    }
    //endregion
}

