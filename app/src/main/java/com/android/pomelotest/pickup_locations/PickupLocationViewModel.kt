package com.android.pomelotest.pickup_locations

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationRequest
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import com.android.pomelotest.base.SchedulersFacade
import com.android.pomelotest.base.model.PickupLocations
import com.android.pomelotest.pickup_locations.model.PickupLocationViewState
import com.android.pomelotest.pickup_locations.usecase.ComputePickupLocationDistanceUseCase
import com.android.pomelotest.pickup_locations.usecase.LoadPickupLocationsUseCase
import javax.inject.Inject

class PickupLocationViewModel @Inject constructor(
    application: Application,
    private val loadPickupLocationsUseCase: LoadPickupLocationsUseCase,
    private val computePickupLocationDistanceUseCase: ComputePickupLocationDistanceUseCase,
    private val mReactiveLocationProvider: ReactiveLocationProvider,
    private val mLocationRequest: LocationRequest,
    private val schedulersFacade: SchedulersFacade
) : AndroidViewModel(application) {
    companion object {
        private val TAG = PickupLocationViewModel::class.java.simpleName
    }

    // View State
    val pickupLocationViewStateLiveData: MediatorLiveData<PickupLocationViewState> by lazy {
        MediatorLiveData<PickupLocationViewState>().apply {
            addSource(pickupLocationLiveData) { source ->
                pickupLocationViewStateLiveData.value = pickupLocationViewStateLiveData.value?.copy(
                    pickupLocations = source
                ) ?: PickupLocationViewState(pickupLocations = source)
            }
        }
    }

    // Live Data
    val pickupLocationLiveData by lazy { MutableLiveData<PickupLocations>() }

    // Data Members
    private val disposables by lazy { CompositeDisposable() }

    fun loadInit() {
        loadPickupLocations()
    }

    private fun getPickupLocations(): PickupLocations {
        return pickupLocationLiveData.value ?: PickupLocations()
    }

    fun loadPickupLocations() {
        disposables += loadPickupLocationsUseCase.execute()
            .subscribeOn(schedulersFacade.io)
            .observeOn(schedulersFacade.ui)
            .subscribeBy(
                onError = {
                    pickupLocationLiveData.value = null
                },
                onSuccess = {
                    pickupLocationLiveData.value = it
                }
            )
    }

    @SuppressWarnings("MissingPermission")
    fun fetchLocationCoordinates() {
        disposables += mReactiveLocationProvider.getUpdatedLocation(mLocationRequest)
            .`as`(RxJavaBridge.toV3Observable())
            .subscribeOn(schedulersFacade.io)
            .observeOn(schedulersFacade.ui)
            .subscribeBy(
                onError = {},
                onNext = { location: Location ->
                    computePickupLocationDistance(
                        currentLocation = location
                    )
                }
            )
    }

    fun computePickupLocationDistance(
        currentLocation: Location
    ) {
        disposables += computePickupLocationDistanceUseCase.execute(
            getPickupLocations(),
            currentLocation
        )
            .subscribeOn(schedulersFacade.io)
            .observeOn(schedulersFacade.ui)
            .subscribeBy(
                onError = { },
                onSuccess = { pickupLocation ->
                    pickupLocationLiveData.value = pickupLocation
                }
            )
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}