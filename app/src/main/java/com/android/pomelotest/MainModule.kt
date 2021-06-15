package com.android.pomelotest

import android.content.Context
import dagger.Module
import dagger.Provides
import com.android.pomelotest.di.qualifier.ActivityContext
import com.tbruyelle.rxpermissions3.RxPermissions

@Module
class MainModule {
    @Provides
    @ActivityContext
    fun provideContext(mainActivity: MainActivity): Context = mainActivity

    @Provides
    fun provideRxPermissions(mainActivity: MainActivity) = RxPermissions(mainActivity)
}