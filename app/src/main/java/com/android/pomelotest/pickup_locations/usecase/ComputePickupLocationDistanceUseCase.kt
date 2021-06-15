package com.android.pomelotest.pickup_locations.usecase

import android.location.Location
import io.reactivex.rxjava3.core.Single
import com.android.pomelotest.base.model.PickupLocations
import javax.inject.Inject

class ComputePickupLocationDistanceUseCase @Inject constructor() {
    fun execute(
        pickupLocations: PickupLocations,
        currentLocation: Location?
    ): Single<PickupLocations> {
        return Single.fromCallable {
            pickupLocations.pickup.map { location ->
                val distance = location.computeDistance(currentLocation)
                println("ComputePickupLocationDistanceUseCase - $distance")
                location.copy(
                    distance = distance
                )
            }.sortedBy {
                it.distance
            }
        }.map { locationList ->
            pickupLocations.copy(
                pickup = locationList ?: emptyList()
            )
        }
    }
}