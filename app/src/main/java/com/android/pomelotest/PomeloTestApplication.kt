package com.android.pomelotest

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import com.android.pomelotest.di.DaggerAppComponent
import hu.akarnokd.rxjava3.debug.RxJavaAssemblyTracking
import javax.inject.Inject

class PomeloTestApplication : Application(), HasAndroidInjector {
    companion object {
        @JvmStatic
        private val TAG = PomeloTestApplication::class.java.simpleName
    }

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        RxJavaAssemblyTracking.enable()

        DaggerAppComponent
            .factory()
            .create(
                application = this,
                endpoint = BuildConfig.MOCK_ENDPOINT,
                authorization = BuildConfig.MOCK_API_KEY
            )
            .inject(this)

        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}