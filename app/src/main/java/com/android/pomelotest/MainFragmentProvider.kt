package com.android.pomelotest

import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.android.pomelotest.di.scope.PerFragment
import com.android.pomelotest.pickup_locations.PickupLocationFragment
import com.android.pomelotest.pickup_locations.PickupLocationModule

@Module
abstract class MainFragmentProvider {


    @PerFragment
    @ContributesAndroidInjector(modules = [PickupLocationModule::class])
    abstract fun bindPickupLocationFragment(): PickupLocationFragment
}