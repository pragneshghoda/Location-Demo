package com.android.pomelotest.pickup_locations

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyItemSpacingDecorator
import com.android.pomelotest.R
import com.android.pomelotest.di.qualifier.ActivityContext
import dagger.Module
import dagger.Provides

@Module
class PickupLocationModule {
    @Provides
    fun provideLayoutManager(@ActivityContext context: Context) = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    @Provides
    fun provideItemDecoration(@ActivityContext context: Context) = EpoxyItemSpacingDecorator(context.resources.getDimensionPixelSize(
        R.dimen.spacing_default))
}