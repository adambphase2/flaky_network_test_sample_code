package com.phase2.networkcalltestproject.Schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

public class CustomSchedulers {
    var iOScheduler: Scheduler
    var mainThreadScheduler: Scheduler

    constructor(){
        iOScheduler = Schedulers.io()
        mainThreadScheduler = AndroidSchedulers.mainThread()
    }
}