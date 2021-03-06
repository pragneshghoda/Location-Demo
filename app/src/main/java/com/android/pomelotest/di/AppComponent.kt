package com.android.pomelotest.di

import android.app.Application
import com.android.pomelotest.PomeloTestApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        BuilderModule::class,
        ViewModelFactoryModule::class,
        NetworkModule::class,
        AssistedModule::class
    ]
)
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
            @BindsInstance @Named("MOCK_ENDPOINT") endpoint: String,
            @BindsInstance @Named(NetworkModule.MOCK_AUTHORIZATION) authorization: String
        ): AppComponent
    }

    fun inject(pomeloTestApplication: PomeloTestApplication)
}