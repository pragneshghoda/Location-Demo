package com.android.pomelotest.pickup_locations

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyItemSpacingDecorator
import com.android.pomelotest.R
import com.tbruyelle.rxpermissions3.RxPermissions
import io.reactivex.rxjava3.kotlin.plusAssign
import kotlinx.android.synthetic.main.fragment_pickup_locations.*
import com.android.pomelotest.base.BaseFragment
import com.android.pomelotest.pickup_locations.controller.PickupLocationController
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.android.synthetic.main.app_bar.*
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import javax.inject.Inject
import javax.inject.Provider

class PickupLocationFragment : BaseFragment() {
    companion object {
        @JvmStatic
        private val TAG = PickupLocationFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): PickupLocationFragment {
            val fragment = PickupLocationFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    //Injection
    @Inject
    lateinit var mViewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var mController: PickupLocationController

    @Inject
    lateinit var mLayoutManagerProvider: Provider<LinearLayoutManager>

    @Inject
    lateinit var mItemDecoration: EpoxyItemSpacingDecorator

    @Inject
    lateinit var mRxPermission: RxPermissions

    @Inject
    lateinit var mLocationRequest: LocationRequest

    @Inject
    lateinit var mLocationSettingsRequest: LocationSettingsRequest

    @Inject
    lateinit var mReactiveLocation: ReactiveLocationProvider

    //Data Members
    private val mPickupLocationViewModel by activityViewModels<PickupLocationViewModel> { mViewModelFactory }
    private var mToolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        arguments?.apply { }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_pickup_locations, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance(view, savedInstanceState)

        //Register ViewModel
        mPickupLocationViewModel.pickupLocationViewStateLiveData
            .observe(viewLifecycleOwner) {
                mController.setData(it)
            }

        mPickupLocationViewModel.loadInit()
    }

    private fun initInstance(view: View?, savedInstanceState: Bundle?) {
        //Init View instance
        recyclerView.apply {
            adapter = mController.adapter
            layoutManager = mLayoutManagerProvider.get()
            addItemDecoration(mItemDecoration)
        }

        mToolbar = view?.findViewById(R.id.toolbar)

        mToolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_location -> {
                    requestLocationPermission()
                }
            }
            false
        }

        val coordinatorLayout = view?.findViewById<CoordinatorLayout>(R.id.coordinatorLayout)
        coordinatorLayout?.let { layout ->
            ViewCompat.setOnApplyWindowInsetsListener(layout) { _, windowInsets ->
                val contentView: ViewGroup? = view.findViewById(R.id.recyclerView)
                contentView?.updatePadding(
                    bottom = windowInsets.systemWindowInsetBottom
                )
                windowInsets
            }
        }
    }

    private fun requestLocationPermission() {
        subscriptions += mRxPermission.request(Manifest.permission.ACCESS_FINE_LOCATION)
            .subscribeBy(
                onError = {},
                onNext = { granted ->
                    if (granted) {
                        enableGPS()
                    }

                })
    }


    /**
     * reference: https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
     */
    private fun enableGPS() {
        LocationServices.getSettingsClient(activity)
            .checkLocationSettings(mLocationSettingsRequest)
            .addOnCompleteListener {
                try {
                    it.getResult(ApiException::class.java)
                    fetchLocation()
                } catch (e: ApiException) {
                    when (e.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            val resolvable = e as ResolvableApiException
                            startIntentSenderForResult(
                                resolvable.resolution.intentSender,
                                REQUEST_CODE_ENABLE_GPS,
                                null,
                                0,
                                0,
                                0,
                                null
                            )
                        }
                    }
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocation() {
        subscriptions += mReactiveLocation.getUpdatedLocation(mLocationRequest)
            .`as`(RxJavaBridge.toV3Observable())
            .observeOn(schedulersFacade.ui)
            .doOnError {}
            .subscribeBy(
                onError = { },
                onNext = {
                    it?.let { location ->
                        mPickupLocationViewModel.computePickupLocationDistance(location)
                    }
                }
            )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_ENABLE_GPS) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    fetchLocation()
                }
                Activity.RESULT_CANCELED -> {

                }
            }
        }
    }

}
