package com.android.pomelotest.di.factory

import androidx.lifecycle.ViewModel

interface ViewModelChildAssistedFactory<T : ViewModel> {
    fun create(): T
}