/*
 * Created by 나인채 on 5/26/2019
 * Copyright (c) 2019. All rights reversed
 */

package com.marahoney.tasque

import android.app.Application
import com.marahoney.tasque.di.Modules
import com.marahoney.tasque.di.ViewModelModules
import com.squareup.leakcanary.LeakCanary
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(
                    ViewModelModules.viewModelModule,
                    Modules.apiModule,
                    Modules.dataModule
            )
        }

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)

    }
}