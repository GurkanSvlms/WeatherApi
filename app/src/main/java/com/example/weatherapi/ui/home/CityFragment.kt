package com.example.weatherapi.ui.home

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Location
import android.os.Build
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapi.MainActivity
import com.example.weatherapi.R
import com.example.weatherapi.databinding.FragmentCityBinding
import com.example.weatherapi.model.cityResponse.city.NearbyCityItem
import com.example.weatherapi.ui.adapter.CitiesAdapter
import com.example.weatherapi.util.base.BaseFragment
import com.example.weatherapi.util.extension.OnItemClickListener
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CityFragment : BaseFragment<FragmentCityBinding,CityViewModel>(
    FragmentCityBinding::inflate
) , OnMapReadyCallback{
    //Google Map
    private lateinit var mMap: GoogleMap

    //Latitude Longitude
    private var latitude: Double = 0.toDouble()
    private var longitude: Double = 0.toDouble()

    //City details
    private lateinit var addressList: List<Address>
    private var cityList : ArrayList<NearbyCityItem> = arrayListOf()

    //Location
    private lateinit var mLastLocation: Location
    private var mMarker: Marker? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    companion object {
        private const val LOCATION_PERMISSION_CODE: Int = 1000
        private const val REQUEST_CHECK_SETTING: Int = 0x1
    }

    override val viewModel by viewModels<CityViewModel>()

    override fun onCreateFinished() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.fCityMapLocation) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        requestRuntimePermission()
        setRecyclerViewAdapter()
    }

    override fun initListeners() {

    }

    override fun observeEvents() {
        with(viewModel){
            cityResponse.observe(viewLifecycleOwner, Observer {
                it?.let { city ->
                    city.forEach {cityItem ->
                        cityList.add(cityItem)
                    }
                }
                (binding.rvCities.adapter as CitiesAdapter).updateList(cityList)
            })

            isLoading.observe(viewLifecycleOwner, Observer {
                handleViewActions()
            })
            onError.observe(viewLifecycleOwner, Observer {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            })
        }
    }

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                    ), LOCATION_PERMISSION_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ), LOCATION_PERMISSION_CODE
                )
                return false
            }
        }
        return true
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest.create()

        locationRequest = LocationRequest()
        locationRequest.priority =
            LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000  //set the interval in which you want to get locations.
        locationRequest.fastestInterval =
            60000  // if a location is available sooner you can get it (i.e. another app is using the location services).
        locationRequest.smallestDisplacement = 10f

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        builder.setAlwaysShow(true)

        val task = LocationServices.getSettingsClient(requireActivity())
            .checkLocationSettings(builder.build())

        task.addOnCompleteListener {
            try {
                val response = task.getResult(ApiException::class.java)
            } catch (e: ApiException) {
                when (e.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        val resolvableApiException = e as ResolvableApiException
                        try {
                            resolvableApiException.startResolutionForResult(
                                requireActivity(),
                                REQUEST_CHECK_SETTING
                            )
                        } catch (e: IntentSender.SendIntentException) {

                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
        }
    }

    private fun buildLocationCallBack() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                mLastLocation = p0.locations[p0.locations.size - 1] //get last location
                if (mMarker != null) {
                    mMarker!!.remove()
                }
                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude

                //Toast.makeText(requireContext(),"$latitude , $longitude",Toast.LENGTH_LONG).show()
                val latLng = LatLng(latitude, longitude)
                cityList.clear()
                viewModel.getNearbyCities(latLngStringBuilder(latitude,longitude))

                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title("Your location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                mMarker = mMap.addMarker(markerOptions)

                //Move camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6.5f))
            }
        }
    }

    private fun requestRuntimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                buildLocationRequest()
                buildLocationCallBack()

                fusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(activity as MainActivity)
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.myLooper()!!
                )
            }
        } else {
            buildLocationRequest()
            buildLocationCallBack()

            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(activity as MainActivity)
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.myLooper()!!
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    )
                        if (checkLocationPermission()) {
                            buildLocationRequest()
                            buildLocationCallBack()

                            fusedLocationProviderClient =
                                LocationServices.getFusedLocationProviderClient(activity as MainActivity)
                            fusedLocationProviderClient.requestLocationUpdates(
                                locationRequest,
                                locationCallback,
                                Looper.myLooper()!!
                            )
                            mMap.isMyLocationEnabled = true
                        }
                } else
                    Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //init Google play services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mMap.isMyLocationEnabled = true
            }
        } else
            mMap.isBuildingsEnabled = true
        //Enable zoom control
        mMap.uiSettings.isZoomControlsEnabled = true

    }

    private fun setRecyclerViewAdapter(){
        val mLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvCities.layoutManager = mLayoutManager
        val mAdapter = CitiesAdapter(object : OnItemClickListener{
            override fun onClick(cityDetail : NearbyCityItem) {
                val action = CityFragmentDirections.actionCityFragmentToCityDetailFragment(cityDetail)
                findNavController().navigate(action)
            }
        })

        binding.rvCities.adapter = mAdapter
    }
    private fun handleViewActions(isLoading: Boolean = false) {
        binding.rvCities.isVisible = !isLoading
        binding.progressBar.isVisible = isLoading
    }

    private fun latLngStringBuilder(latitude: Double, longitude: Double): String {
        return "$latitude,$longitude"
    }
}