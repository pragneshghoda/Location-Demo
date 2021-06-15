package com.android.pomelotest.di

import android.app.Application
import android.content.Context
import com.android.pomelotest.di.qualifier.ApplicationContext
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import dagger.Module
import dagger.Provides
import dagger.Reusable
import pl.charmas.android.reactivelocation2.ReactiveLocationProvider
import java.util.*
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    @ApplicationContext
    fun provideContext(application: Application): Context = application

    @Singleton
    @Provides
    fun provideLocale() = Locale.getDefault()

    @Reusable
    @Provides
    fun provideIntroLocationRequest(): LocationRequest = LocationRequest
        .create()
        .apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            numUpdates = 1
            interval = (1 * 1_000).toLong()
            fastestInterval = (1 * 1_000).toLong()
        }

    @Reusable
    @Provides
    fun provideLocationSettingsRequest(locationRequest: LocationRequest): LocationSettingsRequest {
        return LocationSettingsRequest
            .Builder()
            .addLocationRequest(locationRequest)
            .build()
    }

    @Reusable
    @Provides
    fun provideIntroReactiveLocation(@ApplicationContext context: Context): ReactiveLocationProvider =
        ReactiveLocationProvider(context)
}