package com.android.pomelotest.pickup_locations.usecase

import io.reactivex.rxjava3.core.Single
import com.android.pomelotest.base.model.PickupLocations
import com.android.pomelotest.base.provider.MockServiceProvider
import javax.inject.Inject

class LoadPickupLocationsUseCase @Inject constructor(
    private val mockServiceProvider: MockServiceProvider
) {
    fun execute(): Single<PickupLocations> {
        return mockServiceProvider.pickupLocations()
            .map { pickupLocations ->
                pickupLocations.copy(
                    pickup = pickupLocations.pickup.filter { it.active && it.alias.isNotEmpty() && it.latitude != 0.0 }.take(20)
                )
            }
    }
}