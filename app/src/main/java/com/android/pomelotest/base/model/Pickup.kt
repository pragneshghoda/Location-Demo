package com.android.pomelotest.base.model

import android.location.Location
import android.os.Parcelable
import arrow.core.Either
import arrow.core.getOrElse
import arrow.core.getOrHandle
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.runBlocking

@Parcelize
data class Pickup(
    @SerializedName("feature")
    @Expose
    var feature: String? = null,

    @SerializedName("id_country")
    @Expose
    var idCountry: Int = 0,

    @SerializedName("id_zone")
    @Expose
    var idZone: Int = 0,

    @SerializedName("id_carrier")
    @Expose
    var idCarrier: Int = 0,

    @SerializedName("alias")
    @Expose
    var alias: String = "",

    @SerializedName("address1")
    @Expose
    var address1: String = "",

    @SerializedName("address2")
    @Expose
    var address2: String = "",

    @SerializedName("city")
    @Expose
    var city: String = "",

    @SerializedName("description")
    @Expose
    var description: String = "",

    @SerializedName("is_new_location")
    @Expose
    var isNewLocation: Boolean = false,

    @SerializedName("is_featured")
    @Expose
    var isFeatured: Boolean = false,

    @SerializedName("type")
    @Expose
    var type: String = "",

    @SerializedName("active")
    @Expose
    var active: Boolean = false,

    @SerializedName("status")
    @Expose
    var status: String = "",

    @SerializedName("postcode")
    @Expose
    var postcode: String = "",

    @SerializedName("latitude")
    @Expose
    var latitude: Double? = 0.0,
    @SerializedName("longitude")
    @Expose
    var longitude: Double? = 0.0,

    @SerializedName("features")
    @Expose
    var features: List<Feature> = emptyList(),

    val distance: Double = Double.NaN
) : Parcelable {

    fun computeDistance(currentLocation: Location?): Double {
        val lat = latitude ?: 0.0
        val lng = longitude ?: 0.0

        return currentLocation?.let {
            when {
                lat != 0.0 && lng != 0.0 -> runBlocking {
                    Either.catch {
                        val newLocation = Location("newlocation")
                        newLocation.latitude = lat
                        newLocation.longitude = lng
                        it.distanceTo(newLocation).toDouble() / 1000.0
                    }.getOrHandle { Double.NaN }
                }
                else -> Double.NaN
            }
        } ?: Double.NaN
    }
}