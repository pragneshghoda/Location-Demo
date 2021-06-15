package com.android.pomelotest.pickup_locations.controller

import com.airbnb.epoxy.TypedEpoxyController
import com.android.pomelotest.base.epoxy.view.epoxyLoadingView
import com.android.pomelotest.pickup_locations.controller.model.pickupLocation
import com.android.pomelotest.pickup_locations.model.PickupLocationViewState
import javax.inject.Inject

class PickupLocationController @Inject constructor() :
    TypedEpoxyController<PickupLocationViewState>() {
    companion object {
        @JvmStatic
        private val TAG = PickupLocationController::class.java.simpleName
    }

    override fun buildModels(data: PickupLocationViewState?) {
        data?.run {
            pickupLocations?.pickup?.forEachIndexed { index, location ->
                pickupLocation {
                    id("location_$index")
                    location(location)
                }
            }
        } ?: epoxyLoadingView {
            id("loading")
            spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
        }
    }

}