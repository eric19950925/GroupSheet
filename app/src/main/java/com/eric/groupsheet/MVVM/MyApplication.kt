package com.eric.groupsheet.MVVM

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class MyApplication : Application() {
    companion object {
        lateinit var instance: MyApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                listOf(
                    viewModelModule,
                    coordinator
                )
            )
        }

    }

}