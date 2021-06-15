package com.android.pomelotest.di

import android.content.Context
import com.android.pomelotest.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.android.pomelotest.base.SchedulersFacade
import com.android.pomelotest.base.service.MockService
import com.android.pomelotest.di.qualifier.ApplicationContext
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {
    companion object {
        @JvmStatic
        private val TAG = NetworkModule::class.java.simpleName

        private const val HEADER_API_KEY = "x-api-key"
        private const val HEADER_CONTENT_TYPE = "Content-Type"
        const val MOCK_AUTHORIZATION = "MOCK_API_KEY"
    }

    @Singleton
    @Provides
    fun provideGson() = GsonBuilder()
        .excludeFieldsWithoutExposeAnnotation()
        .create()

    @Singleton
    @Provides
    fun provideInterceptor() = LoggingInterceptor.Builder()
        .loggable(BuildConfig.DEBUG)
        .setLevel(Level.HEADERS)
        .request(TAG)
        .response(TAG)
        .log(Platform.INFO)
        .executor(Executors.newSingleThreadExecutor())
        .build()

    @Singleton
    @Provides
    fun provideCache(@ApplicationContext context: Context) =
        Cache(File(context.cacheDir, "http"), 30 * 1024 * 1024)

    @Singleton
    @Provides
    fun provideHttpClient(
        cache: Cache,
        loggingInterceptor: LoggingInterceptor,
        @Named(MOCK_AUTHORIZATION) authorization: String
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .followRedirects(false)
            .cache(cache)
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val chainBuilder = chain.request().newBuilder()

                chainBuilder.addHeader(HEADER_API_KEY, authorization)
                chainBuilder.addHeader(HEADER_CONTENT_TYPE, "application/json")

                val request = chainBuilder.build()
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
        return builder.build()
    }

    @Singleton
    @Named("MOCK_RETROFIT")
    @Provides
    fun provideMockRetrofit(
        @Named("MOCK_ENDPOINT") endpoint: String,
        gson: Gson,
        okHttpClient: OkHttpClient,
        rxJava3CallAdapterFactory: RxJava3CallAdapterFactory
    ) = Retrofit.Builder()
        .baseUrl(endpoint)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(rxJava3CallAdapterFactory)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideMockService(@Named("MOCK_RETROFIT") retrofit: Retrofit) =
        retrofit.create(MockService::class.java)

    @Singleton
    @Provides
    fun provideSchedulerFacade() = SchedulersFacade()

    @Singleton
    @Provides
    fun provideRxJava3CallAdapterFactory() = RxJava3CallAdapterFactory.createSynchronous()
}