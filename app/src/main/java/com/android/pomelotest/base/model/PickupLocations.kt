package com.android.pomelotest.base.model

import android.os.Parcelable
import com.android.pomelotest.base.model.Pickup
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PickupLocations(
    @field:[Expose SerializedName("number_of_new_locations")]
    var numberOfNewLocations: Int = 0,
    @field:[Expose SerializedName("pickup")]
    var pickup: List<Pickup> = emptyList()
) : Parcelable