package kz.flyingv.remindme

import android.app.Application
import kz.flyingv.remindme.scheduler.RemindScheduler
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ReminderApp: Application() {

    private val appModule = module {
        single { RemindScheduler(androidContext()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@ReminderApp)
            modules(appModule)
        }

    }

}