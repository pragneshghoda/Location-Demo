package com.android.pomelotest.base.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Feature(
    @field:[Expose SerializedName("type")]
    var type: String = "",

    @field:[Expose SerializedName("description")]
    var description: String = ""
) : Parcelable