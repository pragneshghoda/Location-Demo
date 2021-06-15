package com.android.pomelotest.base.service

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import com.android.pomelotest.base.model.PickupLocations

interface MockService {
    /**
     * Get list of pickup-locations from mock api
     */
    @GET("v3/pickup-locations")
    fun pickupLocations(
    ): Single<PickupLocations>
}