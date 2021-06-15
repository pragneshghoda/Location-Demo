package com.android.pomelotest.di

import com.android.pomelotest.MainActivity
import com.android.pomelotest.MainFragmentProvider
import com.android.pomelotest.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.android.pomelotest.di.scope.PerActivity

@Module
abstract class BuilderModule {
    @PerActivity
    @ContributesAndroidInjector(modules = [MainModule::class, MainFragmentProvider::class])
    abstract fun bindMainActivity(): MainActivity
}