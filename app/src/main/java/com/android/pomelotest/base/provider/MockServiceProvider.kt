package com.android.pomelotest.base.provider

import io.reactivex.rxjava3.core.Single
import com.android.pomelotest.base.model.PickupLocations
import com.android.pomelotest.base.service.MockService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockServiceProvider @Inject constructor(
        private val mockService: MockService
) {
    companion object {
        @JvmStatic
        private val TAG = MockServiceProvider::class.java.simpleName
    }

    fun pickupLocations(): Single<PickupLocations> {
        return mockService.pickupLocations()
    }
}