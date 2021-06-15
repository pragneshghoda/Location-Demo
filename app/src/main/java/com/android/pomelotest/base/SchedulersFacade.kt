package com.android.pomelotest.base

import android.os.Looper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

open class SchedulersFacade {
    val single: Scheduler
        get() = Schedulers.single()

    val io: Scheduler
        get() = Schedulers.io()

    val computation: Scheduler
        get() = Schedulers.computation()

    val ui: Scheduler
        get() = AndroidSchedulers.from(Looper.getMainLooper(), true)
}
