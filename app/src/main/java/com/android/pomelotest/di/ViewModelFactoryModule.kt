package com.android.pomelotest.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import com.android.pomelotest.di.factory.ViewModelFactory
import com.android.pomelotest.di.qualifier.ViewModelKey
import com.android.pomelotest.pickup_locations.PickupLocationViewModel

@Module
abstract class ViewModelFactoryModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PickupLocationViewModel::class)
    internal abstract fun photoViewModel(viewModel: PickupLocationViewModel): ViewModel
}