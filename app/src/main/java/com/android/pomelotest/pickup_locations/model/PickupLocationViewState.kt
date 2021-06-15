package com.android.pomelotest.pickup_locations.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.android.pomelotest.base.model.PickupLocations

@Parcelize
data class PickupLocationViewState(
    val pickupLocations: PickupLocations? = null
) : Parcelable